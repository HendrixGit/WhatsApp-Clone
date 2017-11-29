package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import whatsapp.cursoandroid.com.whatsapp.helper.Permissao;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.whatsappandroid.cursoandroid.whatsapp.R;


public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText telefone;
    private EditText codPais;
    private EditText codArea;
    private Button   botaoCadastrar;
    private  String   mensagemEnvio = "";
    private String[]  permissoesNecessarias = new String[]{
            android.Manifest.permission.SEND_SMS
    };
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1, this, permissoesNecessarias);

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
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto = codPais.getText().toString() + codArea.getText().toString() + telefone.getText().toString();
                String telefoneSemFormatacao = telefoneCompleto.replace("+","");
                telefoneSemFormatacao = telefoneCompleto.replace("-", "");

                Preferencias preferencias = new Preferencias(LoginActivity.this);

                if ((!nome.getText().toString().equals("")) && (!telefone.getText().toString().equals(""))
                   && (!codArea.getText().toString().equals("")) && (!codPais.getText().toString().equals("")) ) {

                    mensagemEnvio = generateToken();
                    preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemFormatacao, mensagemEnvio);

                    //Envio de sms(+5517991515141)
                    telefoneSemFormatacao = "5554";//teste no emulador
                    if (enviaSMS("+" + telefoneSemFormatacao,mensagemEnvio) == true){
                        Toast.makeText(getApplicationContext(),"SMS Enviado com sucesso",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Falha no envio do SMS ",Toast.LENGTH_SHORT).show();
                    }
                    HashMap<String, String> usuario = preferencias.getDadosusuario();
                    Log.i("Nome", "Nome: " + usuario.get("nome") + " TELEFONE: " +  usuario.get("telefone") + " Token: " + usuario.get("token"));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Favor Preencha os dados",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private static String generateToken() {
        //Gerar Token no servidor por questao de segurança
        Random randomico = new Random();//gerar toque de 4 digitos
        int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;//garante que sera gerado um numero aleatorio de 4 digitos
        String token = String.valueOf(numeroRandomico);
        return token;
    }
    private boolean enviaSMS(String telefone, String mensagem){
        try {
            SmsManager smsManager = SmsManager.getDefault();//classe para o envio de SMS
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissoes, int[] grantResults){//verificar se as opcoes foi negada
        super.onRequestPermissionsResult(requestCode, permissoes, grantResults);
        for(int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                util.alertaValidacaoPermissao(this, "Permissões Negadas", "Para utilizar o app é preciso aceitar as permissões");
            }
        }
    }
}
