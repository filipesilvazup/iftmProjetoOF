package iftm.projeto.com.projetoof.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.model.Evento;
import iftm.projeto.com.projetoof.ui.model.UserDatabase;

public class AdapterEvento extends BaseAdapter {
    private List<Evento> eventos;
    private  List<Evento> eventosFilters;
    private final Activity act;

    public AdapterEvento(List<Evento> eventos, Activity act) {
        this.eventos = eventos;
        this.act = act;
        eventosFilters = new ArrayList<>();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return this.eventos.size();
    }

    @Override
    public Evento getItem(int position) {
        return this.eventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater()
                .inflate(R.layout.item_list_evento, parent, false);

        ImageView imagem = view.findViewById(R.id.img_evento);
        TextView tituloEvento = view.findViewById(R.id.tv_titulo_evento);
        TextView localEvento = view.findViewById(R.id.tv_local);
        TextView horarioEvento = view.findViewById(R.id.tv_horario);

        Picasso.get()
                .load(eventos.get(position).getImagem())
                .resize(300, 300)
                .centerCrop()
                .into(imagem);

        tituloEvento.setText(eventos.get(position).getTitulo());
        localEvento.setText(eventos.get(position).getEndereco());
        horarioEvento.setText(eventos.get(position).getHora());



        return view;    }


    public Filter getFilter(final List<Evento> listas) {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                eventos = listas;
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = eventos.size();
                    results.values = eventos;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<Evento> itens_filtrados = new ArrayList<Evento>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < eventos.size(); i++) {
                        Evento data = eventos.get(i);

                        filtro = filtro.toString();
                        int condicao = Integer.parseInt(data.getClassificacao());

                        if (condicao<= Integer.parseInt((String) filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            itens_filtrados.add(data);
                        }
                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = itens_filtrados.size();
                    results.values = itens_filtrados;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
               eventosFilters = eventos;
                eventos = (List<Evento>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

        };
        return filter;
    }
}
