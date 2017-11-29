package whatsapp.cursoandroid.com.whatsapp.helper;

import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import android.support.v7.app.AppCompatActivity;

public class Util {

    public static MaskTextWatcher GenerateMask(String mask, TextView component){
        SimpleMaskFormatter simpleMask = new SimpleMaskFormatter(mask);//no construtor da classe simpleMaskFormarter e passado a mascara que voce deseja ser implementada N = numeros
        MaskTextWatcher maskWatcher = new MaskTextWatcher(component,simpleMask);
        return maskWatcher;
    }

}
