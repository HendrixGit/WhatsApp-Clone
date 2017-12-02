package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.whatsappandroid.cursoandroid.whatsapp.R;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidacao = (EditText) findViewById(R.id.edit_cod_validacao);
        validar         = (Button)   findViewById(R.id.button_validar);

        final Util util = new Util();
        codigoValidacao.addTextChangedListener(util.GenerateMask("NNNNNN",codigoValidacao));

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperando dados do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);

                String token = getIntent().getStringExtra("token");
                PhoneAuthCredential credential =
                        PhoneAuthProvider.getCredential(getIntent().getStringExtra("token"),
                        codigoValidacao.getText().toString());

                Util util = new Util();
                util.signInWithPhoneAuthCredential(credential,ValidadorActivity.this);
            }
        });
     }
}
