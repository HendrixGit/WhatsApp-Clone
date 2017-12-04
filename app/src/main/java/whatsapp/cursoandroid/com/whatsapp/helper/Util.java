package whatsapp.cursoandroid.com.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;


public class Util {

    private FirebaseAuth mAuth;

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


}
