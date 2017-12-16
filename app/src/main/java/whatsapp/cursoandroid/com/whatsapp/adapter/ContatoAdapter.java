package whatsapp.cursoandroid.com.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.model.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatoAdapter(Context context, ArrayList<Contato> objects) {
        super(context, 0, objects);
        this.contatos = objects;
        this.context  = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (contatos != null){//verifica se a lista esta vazia

            //inicializa o objeto para a montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);//interface que permite utilizar servicos globais

            //montagem da view a partir do xml
            view = inflater.inflate(R.layout.lista_contato, parent, false);

            //recupera o elemento para exibicao
            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome);
            TextView numero = (TextView) view.findViewById(R.id.tv_numero);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            numero.setText(contato.getNumero());

        }
        return view;
    }
}