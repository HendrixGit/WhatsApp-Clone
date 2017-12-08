package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.com.whatsapp.helper.Permissao;
import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.com.whatsapp.model.Usuario;
import whatsapp.cursoandroid.whatsappandroid.cursoandroid.whatsapp.R;


public class LoginActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText nome;
    private EditText telefone;
    private EditText codPais;
    private EditText codArea;
    private Button botaoCadastrar;
    private String telefoneSemFormatacao;
    private Util util;
    private FirebaseAuth mAuth;
    private String verificationID = "";
    private SQLiteDatabase bancoDados;
    private Cursor cursor;
    private String[]  permissoesNecessarias = new String[]{
            android.Manifest.permission.SEND_SMS
    };
    private DatabaseReference referencia;
    private Usuario usuario;
    private PhoneAuthProvider autenticacaoSMS;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1, this, permissoesNecessarias);
        usuario = new Usuario();
        autenticacao    = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacaoSMS = ConfiguracaoFirebase.getFirebaseAutenticacaoSMS();

        telefone = (EditText) findViewById(R.id.edit_telefone);
        codPais  = (EditText) findViewById(R.id.edit_cod_pais);
        nome     = (EditText) findViewById(R.id.edit_nome);
        codArea  = (EditText) findViewById(R.id.edit_cod_area);
        botaoCadastrar = (Button) findViewById(R.id.button_Cadastrar);

        util = new Util();
        codPais.addTextChangedListener(util.GenerateMask("+NN",codPais));
        codArea.addTextChangedListener(util.GenerateMask("NN",codArea));
        telefone.addTextChangedListener(util.GenerateMask("NNNNN-NNNN",telefone));

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!nome.getText().toString().equals("")) && (!telefone.getText().toString().equals(""))
                        && (!codArea.getText().toString().equals("")) && (!codPais.getText().toString().equals("")) ) {
                    String nomeUsuario = nome.getText().toString();
                    String telefoneCompleto = codPais.getText().toString() + codArea.getText().toString() + telefone.getText().toString();
                    telefoneSemFormatacao = telefoneCompleto.replace("+","");
                    telefoneSemFormatacao = telefoneCompleto.replace("-", "");
                    //telefoneSemFormatacao = "+5554";
                    String tel = telefoneSemFormatacao;
                    sendSMS(telefoneSemFormatacao);
                }
                else{
                    Toast.makeText(LoginActivity.this,"Favor insira os dados",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendSMS(String phoneNumber){
        try {
            phoneVerificationSMS();
            autenticacaoSMS.verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    LoginActivity.this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
        catch (Exception e){
            Log.d("ErroFirebaseSMS: ", e.toString());
        }
    }

    private void phoneVerificationSMS(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(LoginActivity.this,"Token Enviado",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginActivity.this, "Numero de telefone Inválido: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
                else if (e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(LoginActivity.this, "Tempo de envio excedido, ou envio de códigos sucetivos: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
                else if (e instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(LoginActivity.this, "Numero de telefone ja cadstrado: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Erro: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
                Log.d("ErroFirebase: ", e.toString());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID = s;
                verifyCode();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                verificationID = s;
                verifyCode();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Token", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            logedUser();
                            finish();
                        }
                        else{
                            // Sign in failed, display a message and update the UI
                            Log.w("Token", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void logedUser() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }


    private void verifyCode() {
        Intent intent = new Intent(LoginActivity.this, ValidadorActivity.class);
        intent.putExtra("verificationID",verificationID);
        intent.putExtra("nome",nome.getText().toString());
        intent.putExtra("numero",telefoneSemFormatacao);
        startActivity(intent);
        finish();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissoes, int[] grantResults){//verificar se as opcoes foi negada
        super.onRequestPermissionsResult(requestCode, permissoes, grantResults);
        for(int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
               alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negagas: ");
        builder.setMessage("Para utilizar o app é preciso aceitar as permissões");
        builder.setPositiveButton("CONFIRMA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createDatabase(){
        try {
            bancoDados = openOrCreateDatabase("appAuth", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS autenticacao" +
                                    "(numero VARCHAR PRIMARY KEY NOT NULL, " +
                                    "verificationID VARCHAR NOT NULL, " +
                                    "code VARCHAR NOT NULL" +
                                    ") ");
        }
        catch (Exception e){
            Log.i("ErroDatabse",e.toString());
        }
    }
}
