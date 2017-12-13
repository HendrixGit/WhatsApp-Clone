package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.com.whatsapp.model.Usuario;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button validar;
    private Button recomercar;
    private FirebaseAuth mAuth;
    private String verificationID;
    private String code;
    private Boolean loged = false;
    private SQLiteDatabase bancoDados;
    private String numero;
    private String id;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidacao = (EditText) findViewById(R.id.edit_cod_validacao);
        validar         = (Button) findViewById(R.id.button_validar);
        recomercar      = (Button) findViewById(R.id.button_recomecar);


        Util util = new Util();
        codigoValidacao.addTextChangedListener(util.GenerateMask("NNNNNN", codigoValidacao));

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCod(v);
            }
        });

        recomercar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser usuarioFirebase = task.getResult().getUser();
                            usuario = new Usuario();
                            String identificadorUsuario = Base64Custom.codificarBase64(usuarioFirebase.getPhoneNumber());
                            usuario.setId(identificadorUsuario);
                            usuario.setNome(getIntent().getStringExtra("nome"));
                            usuario.setNumero(usuarioFirebase.getPhoneNumber());
                            usuario.salvar();
                            Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                            String identificadorUsuarioLogado = Base64Custom.codificarBase64(usuarioFirebase.getPhoneNumber());
                            preferencias.salvarDados(identificadorUsuarioLogado);
                            abrirLoginUsuario();
                        }
                        else {
                            Log.w("Token", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ValidadorActivity.this,"Código de verificação inválido",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void abrirLoginUsuario() {
        Intent intent = new Intent(ValidadorActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void verifyCod(View view){
        try {
            if (!codigoValidacao.getText().toString().equals("")) {
                mAuth = FirebaseAuth.getInstance();
                verificationID = getIntent().getStringExtra("verificationID");
                code           = codigoValidacao.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
                signInWithPhoneAuthCredential(credential);
            }
            else{
                Toast.makeText(ValidadorActivity.this,"Favor preencha o código",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            Log.d("Error verifiCod", e.toString());
        }
    }

    public void saveDataSQLLite(String numero, String verification, String code){
        try {
            bancoDados = openOrCreateDatabase("appAuth", MODE_PRIVATE, null);
            bancoDados.execSQL("INSERT INTO autenticacao(numero, verificationID, code) " +
                    "VALUES (" + numero + "," + verification + "," + code + ") ");
        }
        catch (Exception e){
            Log.i("ErroWriteDatabase",e.toString());
        }
    }

    public void saveDataonSharedPreferences(String numero, String verification, String code, String id){
        SharedPreferences sharedPreferences = getSharedPreferences("auth",0);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        editor.putString("verification",verification);
        editor.putString("code",code);
        editor.putString("numero",numero);
        editor.putString("id",id);
        editor.commit();
    }

    public void checkCredential(){
        Intent intent = getIntent();
        PhoneAuthCredential credential = (PhoneAuthCredential) intent.getSerializableExtra("credential");
        if (credential != null){
            signInWithPhoneAuthCredential(credential);
        }
    }

    }


