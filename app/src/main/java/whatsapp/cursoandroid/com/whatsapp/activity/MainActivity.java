package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.whatsappandroid.cursoandroid.whatsapp.R;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference referenciaFirebase = FirebaseDatabase.getInstance().getReference();
    private Button botaoSair;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoSair = (Button) findViewById(R.id.button_sair);

        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
