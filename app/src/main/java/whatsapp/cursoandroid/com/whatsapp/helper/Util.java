package whatsapp.cursoandroid.com.whatsapp.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private FirebaseAuth mAuth;
    private String[]  permissoesNecessarias = new String[]{
            android.Manifest.permission.SEND_SMS
    };

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



    public void changeActivity(Context context, Class classes){
        Intent intent = new Intent(context, classes);
        context.startActivity(intent);
    }

    public String returnDataString(Date data) throws ParseException {
        long now = System.currentTimeMillis();
        Date dataAtual = new Date(now);
        String dataFormatada = "";
        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataAtual = sdf.parse(sdf.format(dataAtual));
        Date dataParametro = sdf.parse(sdf.format(data));

        if (dataAtual.compareTo(dataParametro) <= 0 ) {
            sdf = new SimpleDateFormat("HH:mm");
        }
        else{
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        }
        dataFormatada = sdf.format(data);
        return dataFormatada;
    }

}
