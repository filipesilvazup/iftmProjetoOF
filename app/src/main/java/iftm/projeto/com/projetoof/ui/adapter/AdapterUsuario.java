package iftm.projeto.com.projetoof.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.model.User;

public class AdapterUsuario extends BaseAdapter {

    private final List<User> users;
    private final Activity act;

    public AdapterUsuario(List<User> users, Activity act) {
        this.users = users;
        this.act = act;
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public User getItem(int position) {
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
        tipo.setText(users.get(position).getTipo());

        return view;
    }
}
