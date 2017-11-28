package whatsapp.cursoandroid.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.whatsappandroid.cursoandroid.whatsapp.R;


public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText telefone;
    private EditText codPais;
    private EditText codArea;
    private Button   botaoCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nome     = (EditText) findViewById(R.id.edit_nome);
        telefone = (EditText) findViewById(R.id.edit_telefone);
        codPais  = (EditText) findViewById(R.id.edit_cod_pais);
        codArea  = (EditText) findViewById(R.id.edit_cod_area);
        botaoCadastrar = (Button) findViewById(R.id.button_Cadastrar);


        Util util = new Util();
        codPais.addTextChangedListener(util.GenerateMask("+NN",codPais));
        codArea.addTextChangedListener(util.GenerateMask("NN",codArea));
        telefone.addTextChangedListener(util.GenerateMask("NNNNN-NNNN",telefone));

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telefoneCompleto = codPais.getText().toString() + codArea.getText().toString() + telefone.getText().toString();
                String telefoneSemFormatacao = telefoneCompleto.replace("+", "");
                telefoneSemFormatacao = telefoneCompleto.replace("-", "");

                generateToken(telefoneSemFormatacao);


            }
        });
    }

    private static String generateToken(String telefone) {
        //Gerar Token no servidor por questao de segurança
        Random randomico = new Random();//gerar toque de 4 digitos
        int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;//garante que sera gerado um numero aleatorio de 4 digitos
        String token = String.valueOf(numeroRandomico);
        return token;
    }
}
