package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import whatsapp.cursoandroid.com.whatsapp.helper.Util;
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
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nome     = (EditText) findViewById(R.id.edit_nome);
        telefone = (EditText) findViewById(R.id.edit_telefone);
        codPais  = (EditText) findViewById(R.id.edit_cod_pais);
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
                    verifyPhoneNumber(telefoneSemFormatacao);
                }
            }
        });

    }

    public void verifyPhoneNumber(String phoneNumber){
        phoneVerificarionSMS();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    public void phoneVerificarionSMS(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(LoginActivity.this,"Token Enviado",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginActivity.this,"Codigo não Enviado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                token = s.toString();
                abrirCadastroUsuarios();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Token", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
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


    public void abrirCadastroUsuarios() {
        Intent intent = new Intent(LoginActivity.this, ValidadorActivity.class);
        intent.putExtra("token",token);
        startActivity(intent);
    }
}
