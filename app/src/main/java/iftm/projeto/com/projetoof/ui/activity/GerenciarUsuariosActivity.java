package iftm.projeto.com.projetoof.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
import iftm.projeto.com.projetoof.ui.model.UserDatabase;
import iftm.projeto.com.projetoof.ui.utils.Constantes;

public class GerenciarUsuariosActivity extends AppCompatActivity implements OnAddListenner {

    private DatabaseReference myRef;
    private List<UserDatabase> users;
    private OnAddListenner onAddListenner;
    private AdapterUsuario adapter;
    private ListView lista;
    private RelativeLayout relativeToolbar;
    private Context context;

    @Override
    public void onResume() {
        super.onResume();
        myRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("DEU CERTO");
                    UserDatabase u = new UserDatabase( noteDataSnapshot.getKey(), noteDataSnapshot.child("email").getValue(String.class), noteDataSnapshot.child("senha").getValue(String.class),
                            noteDataSnapshot.child("tipo").getValue(String.class), noteDataSnapshot.child("uid").getValue(String.class));

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
        getSupportActionBar().hide();
        context = this;

        relativeToolbar = findViewById(R.id.relative_toolbar);
        lista = findViewById(R.id.list_usuarios);

        relativeToolbar.setVisibility(View.VISIBLE);
        onAddListenner = this;
        users = new ArrayList<>();
        adapter = new AdapterUsuario(users, this);
        myRef = FirebaseDatabase.getInstance().getReference();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDatabase user = (UserDatabase) parent.getItemAtPosition(position);

                Intent it = new Intent(context, AlterarUsuarioActivity.class);
                it.putExtra("user", user);
                startActivity(it);
            }
        });
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
        relativeToolbar.setVisibility(View.GONE);
        finish();
    }
}
