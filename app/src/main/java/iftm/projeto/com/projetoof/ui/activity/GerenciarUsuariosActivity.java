package iftm.projeto.com.projetoof.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.activity.interfaces.OnAddListenner;
import iftm.projeto.com.projetoof.ui.adapter.AdapterUsuario;
import iftm.projeto.com.projetoof.ui.model.User;
import iftm.projeto.com.projetoof.ui.utils.Constantes;

public class GerenciarUsuariosActivity extends AppCompatActivity implements OnAddListenner {

    private DatabaseReference myRef;
    private List<User> users;
    private OnAddListenner onAddListenner;
    private AdapterUsuario adapter;
    private ListView lista;
    private RelativeLayout toolbar;

    @Override
    public void onResume() {
        super.onResume();
        myRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("DEU CERTO");
                    User u = new User(noteDataSnapshot.child("email").getValue(String.class), noteDataSnapshot.child("senha").getValue(String.class),
                            noteDataSnapshot.child("tipo").getValue(String.class), noteDataSnapshot.child("uid").getValue(String.class));
                    if(u.getTipo().equals(Constantes.USER_SEM_PERMISSAO)) u.setTipo("Sem Permissão");
                    if(u.getTipo().equals(Constantes.USER_ADMIN)) u.setTipo("Administrador");
                    if(u.getTipo().equals(Constantes.USER_PAGO)) u.setTipo("Usuário Vip");
                    if(u.getTipo().equals(Constantes.USER_FREE)) u.setTipo("Usuário Free");
                    users.add(u);

                }
                onAddListenner.onUserlistenner();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_usuarios);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        lista = findViewById(R.id.list_usuarios);

        onAddListenner = this;
        users = new ArrayList<>();
        adapter = new AdapterUsuario(users, this);
        myRef = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public void onUserlistenner() {
        adapter = new AdapterUsuario(users, this);
        lista.setAdapter(adapter);

    }

    @Override
    public void onEventoListenner() {

    }

    public void onBackActivity(View view) {
        toolbar.setVisibility(View.GONE);
        finish();
    }
}
