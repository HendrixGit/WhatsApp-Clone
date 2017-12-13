package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.adapter.TabAdapter;
import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referenciaFirebase = FirebaseDatabase.getInstance().getReference();
    private Button botaoSair;
    private FirebaseAuth usuarioFirebase;
    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String identificadorContato;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);//preenche toda a largura
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //configurando adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//sobreescrevendo o metodo para exibir menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_sair : deslogarUsuario(); return true;
            case R.id.item_configuracoes: return true;
            case R.id.item_adicionar: abrirCadastroContato(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadastroContato(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("Telefone: ");
        alertDialog.setCancelable(false);// nao e possivel fechar a janela ao clicar fora

        final EditText editText = new EditText(getApplicationContext());
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String telefone = editText.getText().toString();
                if (telefone.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Preencha o numero",Toast.LENGTH_SHORT).show();
                }
                else{//verificar se o usuario esta cadastrado no app
                    identificadorContato = Base64Custom.codificarBase64(telefone);
                    //recuperar instancia firebase
                    firebase = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios").child(identificadorContato);
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {//consulta somente uma vez e recupera os dados
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                firebase = ConfiguracaoFirebase.getFirebaseDatabase();
                                //firebase.child("contatos").child(Base64Custom.codificarBase64();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Usuário não possui cadastro",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    public void deslogarUsuario(){
        usuarioFirebase.signOut();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
