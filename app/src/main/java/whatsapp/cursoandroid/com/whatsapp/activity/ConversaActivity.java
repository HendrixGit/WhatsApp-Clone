package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;

    //dados destinatario
    private String idUsuarioDestinatario;
    private String nomeUsuarioDestinatario;

    //dados remetente
    private String idUsuarioRemetente;

    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem   = (ImageButton) findViewById(R.id.bt_enviar);
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            nomeUsuarioDestinatario  = extra.getString("nome");
            String emailDestinatario = extra.getString("numero");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailDestinatario);
        }
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("page",1);
                startActivity(intent);
                finish();
            }
        });

        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = editMensagem.getText().toString();
                if (!textoMensagem.isEmpty()){
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);
                    salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);
                    editMensagem.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Digite a mensagem!",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try {
            firebase = ConfiguracaoFirebase.getFirebaseDatabase().child("mensagens");
            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .push()//criado no com o identificador unico
                    .setValue(mensagem);

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
