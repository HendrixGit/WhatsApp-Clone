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
import whatsapp.cursoandroid.com.whatsapp.helper.Util;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;

public class ConversaAdapter extends ArrayAdapter {

    private ArrayList<Conversa> conversas;
    private Context context;

    public ConversaAdapter(Context context,  ArrayList<Conversa> objects) {
        super(context, 0, objects);
        this.context   = context;
        this.conversas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (conversas != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);//interface que permite utilizar servicos globais

            view = inflater.inflate(R.layout.lista_conversas, parent, false);

            TextView nome = (TextView) view.findViewById(R.id.tv_titulo);
            TextView ultimaMensagem = (TextView) view.findViewById(R.id.tv_subtitulo);
            TextView dataHora  = (TextView) view.findViewById(R.id.tv_datahora);

            Conversa conversa = conversas.get(position);
            nome.setText(conversa.getNome());
            ultimaMensagem.setText(conversa.getMensagem());
            Util util = new Util();
            try {
                dataHora.setText(util.returnDataString(conversa.getData()));
            } catch (ParseException e) {

            }
        }

        return view;
    }
}