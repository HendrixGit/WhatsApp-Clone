package whatsapp.cursoandroid.com.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

final public class ConfiguracaoFirebase {//nao pode ser extendia final

    private static DatabaseReference referenciaDatabase;//static mesmo valor independente das instancias
    private static FirebaseAuth autenticacao;
    private static PhoneAuthProvider autenticacaoSMS;

    public static DatabaseReference getFirebaseDatabase(){
        if (referenciaDatabase == null){ referenciaDatabase = FirebaseDatabase.getInstance().getReference(); }
        return referenciaDatabase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static PhoneAuthProvider getFirebaseAutenticacaoSMS(){
        if (autenticacaoSMS == null){
            autenticacaoSMS = PhoneAuthProvider.getInstance();
        }
        return autenticacaoSMS;
    }


} 