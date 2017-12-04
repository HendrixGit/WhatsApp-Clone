package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.whatsappandroid.cursoandroid.whatsapp.R;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button validar;
    private FirebaseAuth mAuth;
    private String verificationID;
    private String code;
    private Boolean loged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidacao = (EditText) findViewById(R.id.edit_cod_validacao);
        validar = (Button) findViewById(R.id.button_validar);

        Util util = new Util();
        codigoValidacao.addTextChangedListener(util.GenerateMask("NNNNNN", codigoValidacao));
        mAuth = FirebaseAuth.getInstance();

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperando dados do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                verifyCod(v);
            }
        });

    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Token", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(ValidadorActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.w("Token", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                            Toast.makeText(ValidadorActivity.this,"Login inválido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verifyCod(View view){
        try {
            if (!codigoValidacao.getText().toString().equals("")) {
                verificationID = getIntent().getStringExtra("verificationID");
                code = codigoValidacao.getText().toString();
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

    }


