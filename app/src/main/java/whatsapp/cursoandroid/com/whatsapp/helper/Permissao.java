package whatsapp.cursoandroid.com.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){//permissoes android marshmellow ou superior
        if (Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<>();
            for(String permissao : permissoes){//percorre todas as posicoes do array de permissoes
              if(ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED == false){
                  listaPermissoes.add(permissao);
              }
            }
            //com a lista vazia nao e preciso verificar as permissoes
            if (listaPermissoes.isEmpty()) return true;

            //Solicita Permisssao
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity, novasPermissoes, 1);//requestCode de onde foi requisitado a permissao Origem
        }
        return true;
    }
}
