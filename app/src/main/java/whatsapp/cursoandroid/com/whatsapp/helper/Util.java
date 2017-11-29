package whatsapp.cursoandroid.com.whatsapp.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class Util {

    public static MaskTextWatcher GenerateMask(String mask, TextView component){
        SimpleMaskFormatter simpleMask = new SimpleMaskFormatter(mask);//no construtor da classe simpleMaskFormarter e passado a mascara que voce deseja ser implementada N = numeros
        MaskTextWatcher maskWatcher = new MaskTextWatcher(component,simpleMask);
        return maskWatcher;
    }

    public void alertaValidacaoPermissao(Context context, String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("CONFIRMA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    System.exit(0);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
