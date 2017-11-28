package whatsapp.cursoandroid.com.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences sharedPreferences;
    private String NOME_ARQUIVO = "whatasapp.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";


    public Preferencias(Context contextoParametro){
        contexto = contextoParametro;
        sharedPreferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);//cria um arquivo com o modo que so o aplicativo podera manipulalo nao sera compartilhado
        editor = sharedPreferences.edit();//edita o arquivo de preferencias
    }

    public void salvarUsuarioPreferencias(String nome, String telefone, String token){
        editor.putString(CHAVE_NOME,nome);
        editor.putString(CHAVE_TELEFONE,telefone);
        editor.putString(CHAVE_TOKEN,token);
        editor.commit();//salva os dados no arquivo de preferencia criado
    }

    public HashMap<String, String> getDadosusuario(){
        HashMap<String, String> dadosUsuario = new HashMap<>();
        dadosUsuario.put(CHAVE_NOME,sharedPreferences.getString(CHAVE_NOME, null));
        dadosUsuario.put(CHAVE_TELEFONE,sharedPreferences.getString(CHAVE_TELEFONE, null));
        dadosUsuario.put(CHAVE_TOKEN,sharedPreferences.getString(CHAVE_TOKEN, null));
        return  dadosUsuario;
    }

}
