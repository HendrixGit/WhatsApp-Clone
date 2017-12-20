package whatsapp.cursoandroid.com.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context contexto;
    private SharedPreferences sharedPreferences;
    private String NOME_ARQUIVO = "whatasapp.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private String CHAVE_NOME = "nomeUsuarioLogado";


    public Preferencias(Context contextoParametro){
        contexto = contextoParametro;
        sharedPreferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);//cria um arquivo com o modo que so o aplicativo podera manipulalo nao sera compartilhado
        editor = sharedPreferences.edit();//edita o arquivo de preferencias
    }

    public void salvarDados(String identificadorUsuario, String nomeUsuario){
        editor.putString(CHAVE_IDENTIFICADOR ,identificadorUsuario);
        editor.putString(CHAVE_NOME ,nomeUsuario);
        editor.commit();//salva os dados no arquivo de preferencia criado
    }

    public String getIdentificador(){
        return sharedPreferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return sharedPreferences.getString(CHAVE_NOME, null);
    }

}
