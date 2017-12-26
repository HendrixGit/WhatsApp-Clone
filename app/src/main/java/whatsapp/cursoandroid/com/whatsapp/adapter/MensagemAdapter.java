package whatsapp.cursoandroid.com.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.com.whatsapp.model.Mensagem;

public class MensagemAdapter extends ArrayAdapter {

    private ArrayList<Mensagem> mensagens;
    private Context context;

    public MensagemAdapter(Context context, ArrayList<Mensagem> objects) {
        super(context, 0, objects);
        this.mensagens = objects;
        this.context   = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(mensagens != null){
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Mensagem mensagem = mensagens.get(position);

            if(idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            }
            else{
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }
            TextView textoMensagem = (TextView) view.findViewById(R.id.tv_mensagem);
            TextView textoHoraData = (TextView) view.findViewById(R.id.tv_horamensagem);
            textoMensagem.setText(mensagem.getMensagem());
            Util util = new Util();
            try {
                textoHoraData.setText(util.returnDataString(mensagem.getData()));
            } catch (ParseException e) {

            }
        }

        return view;
    }
}

