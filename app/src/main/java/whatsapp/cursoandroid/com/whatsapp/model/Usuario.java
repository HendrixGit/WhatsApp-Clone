package whatsapp.cursoandroid.com.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;

public class Usuario {
    private String id;
    private String numero;
    private String nome;

    public Usuario() {

    }

    public void salvar(){
        DatabaseReference referenciaDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        referenciaDatabase.child("usuarios").child(getId()).setValue(this);

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}