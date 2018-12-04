package iftm.projeto.com.projetoof.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.model.User;
import iftm.projeto.com.projetoof.ui.model.UserDatabase;
import iftm.projeto.com.projetoof.ui.utils.Constantes;

public class AlterarUsuarioActivity extends AppCompatActivity {

    private UserDatabase user;
    private TextView email;
    private Spinner spinner;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_usuario);

        email = findViewById(R.id.tv_email);
        spinner = findViewById(R.id.tipos);
        linearLayout = findViewById(R.id.alterar_layout);

        user = (UserDatabase) getIntent().getExtras().getSerializable("user");

        if (user != null) {
            email.setText(user.getEmail());

            if (user.getTipo().equals(Constantes.USER_SEM_PERMISSAO)) {
                spinner.setSelection(Constantes.USER_SEM_PERMISSAO_INT);
            } else if (user.getTipo().equals(Constantes.USER_ADMIN)) {
                spinner.setSelection(Constantes.USER_ADMIN_INT);
            } else if (user.getTipo().equals(Constantes.USER_FREE)) {
                spinner.setSelection(Constantes.USER_FREE_INT);
            } else if (user.getTipo().equals(Constantes.USER_PAGO)) {
                spinner.setSelection(Constantes.USER_PAGO_INT);
            }
        }

    }

    public void alterarUsuario(View view) {
        String tipo = (String) spinner.getSelectedItem();
        if (tipo.equals("Sem permissão")) {
            user.setTipo(Constantes.USER_SEM_PERMISSAO);
        } else if (tipo.equals("Administrador")) {
            user.setTipo(Constantes.USER_ADMIN);
        } else if (tipo.equals("Usuário Free")) {
            user.setTipo(Constantes.USER_FREE);
        } else if (tipo.equals("Usuário VIP")) {
            user.setTipo(Constantes.USER_PAGO);
        }
        Map<String, String> map = new HashMap<>();
        map.put("tipo", user.getTipo());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(user.getKey());
        databaseReference.updateChildren((HashMap) map);

        Snackbar snackbar = Snackbar
                .make(linearLayout, "Usuário Alterado com sucesso!!!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getContext(), MainActivity.class));
    }

    private Context getContext() {
        return this;
    }
}
