package iftm.projeto.com.projetoof.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.model.User;
import iftm.projeto.com.projetoof.ui.model.UserDatabase;
import iftm.projeto.com.projetoof.ui.utils.Constantes;

public class AdapterUsuario extends BaseAdapter {

    private final List<UserDatabase> users;
    private final Activity act;

    public AdapterUsuario(List<UserDatabase> users, Activity act) {
        this.users = users;
        this.act = act;
    }



    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public UserDatabase getItem(int position) {
        return this.users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.item_list_usuario, parent, false);

        TextView email = view.findViewById(R.id.tv_email);
        TextView tipo = view.findViewById(R.id.tv_tipo_usuario);

        email.setText(users.get(position).getEmail());

        if(users.get(position).getTipo().equals(Constantes.USER_SEM_PERMISSAO)) tipo.setText("Sem Permissão");
        if(users.get(position).getTipo().equals(Constantes.USER_ADMIN)) tipo.setText("Administrador");
        if(users.get(position).getTipo().equals(Constantes.USER_PAGO)) tipo.setText("Usuário Vip");
        if(users.get(position).getTipo().equals(Constantes.USER_FREE)) tipo.setText("Usuário Free");


        return view;
    }


}
