package whatsapp.cursoandroid.com.whatsapp.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

final public class ConfiguracaoFirebase {//nao pode ser extendia final

    private static DatabaseReference referenciaDatabase;//static mesmo valor independente das instancias

    public static DatabaseReference getFirebaseDatabase(){
        if (referenciaDatabase == null){ referenciaDatabase = FirebaseDatabase.getInstance().getReference(); }
        return referenciaDatabase;
    }
} 