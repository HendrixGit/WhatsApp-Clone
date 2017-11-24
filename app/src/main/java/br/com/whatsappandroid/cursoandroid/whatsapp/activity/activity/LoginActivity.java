package br.com.whatsappandroid.cursoandroid.whatsapp.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;

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
    }
}
