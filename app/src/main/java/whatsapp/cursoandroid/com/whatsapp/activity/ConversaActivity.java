package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.adapter.MensagemAdapter;
import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;
import whatsapp.cursoandroid.com.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;

    //dados destinatario
    private String idUsuarioDestinatario;
    private String nomeUsuarioDestinatario;

    //dados remetente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetene;

    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ArrayAdapter<Conversa> adapterConversa;
    private ValueEventListener valueEventListenerMensagem;
    private Date data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        data = Calendar.getInstance().getTime();
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetene = preferencias.getNome();
        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem   = (ImageButton) findViewById(R.id.bt_enviar);
        listView = (ListView) findViewById(R.id.lv_conversas);
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

        //Monta a list view e adapter
        mensagens = new ArrayList<>();
        adapter   = new MensagemAdapter(ConversaActivity.this,mensagens);
        listView.setAdapter(adapter);

        //recuperar mensagens fiebase
        firebase = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListenerMensagem);


        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = editMensagem.getText().toString();
                if (!textoMensagem.isEmpty()){
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setData(data);

                    //salvamos mensagem para o remetente
                    Boolean retornoMensagemRemetente    = salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);

                    if (!retornoMensagemRemetente) {
                        Toast.makeText(
                                getApplicationContext(),
                                "Erro ao salvar mensagem tente novamente",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    else{
                        //salvamos mensagem para o destinatario
                        Boolean retornoMensagemDestinatario = salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);
                        if (!retornoMensagemDestinatario) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Erro ao salvar mensagem para o destinatario, tente novamente",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                    //salvar conversa remetente
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMensagem);
                    conversa.setData(data);
                    Boolean retornoConversRemetente = salvarConversa(idUsuarioRemetente,idUsuarioDestinatario,conversa);
                    if (!retornoConversRemetente){
                        Toast.makeText(
                                getApplicationContext(),
                                "Problema ao salvar a conversa, tente novamente",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    else{//salvar conversa destinatario
                        conversa = new Conversa();
                        conversa.setIdUsuario(idUsuarioRemetente);
                        conversa.setNome(nomeUsuarioRemetene);
                        conversa.setMensagem(textoMensagem);
                        conversa.setData(data);
                        salvarConversa(idUsuarioDestinatario,idUsuarioRemetente,conversa);
                    }
                    editMensagem.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Digite a mensagem!",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){
        try {
            firebase = ConfiguracaoFirebase.getFirebaseDatabase().child("conversas");
            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);//remove o listener caso o usuario nao esteja na activity de conversas
    }
}
