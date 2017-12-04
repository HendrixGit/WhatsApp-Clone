package whatsapp.cursoandroid.com.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Autenticacao {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private Activity currentActivity;
    private Activity targetActivity;
    private Context context;
    private Boolean loged = false;
    private String  verificationID = "";
    private int estado = -1;

    public Autenticacao(Activity activity) {

        currentActivity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public int sendSMS(String phoneNumber){
        try {
            phoneVerificationSMS();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    currentActivity,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
        catch (Exception e){
            Log.d("ErroFirebaseSMS: ", e.toString());
        }
        return estado;
    }

    public void phoneVerificationSMS(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(currentActivity,"Token Enviado",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(currentActivity,"Codigo não Enviado: " + e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("ErroFirebase: ", e.toString());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID = s;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                verificationID = s;
            }
        };
    }

    public boolean signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Token", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            loged = true;
                        }
                        else {
                            Log.w("Token", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                            loged = false;
                        }
                    }
                });
            return loged;
        }
    }
