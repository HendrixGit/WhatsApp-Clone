package whatsapp.cursoandroid.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

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

        Util util = new Util();
        codigoValidacao.addTextChangedListener(util.GenerateMask("NNNN",codigoValidacao));

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperando dados do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosusuario();
                String tokenGerado = usuario.get("token");
                if (codigoValidacao.getText().toString().equals(tokenGerado)){
                    Toast.makeText(getApplicationContext(),"Token Validado Bem vindo ao WhatsApp",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Token não Validado",Toast.LENGTH_SHORT).show();
                }
            }
        });
     }
}
