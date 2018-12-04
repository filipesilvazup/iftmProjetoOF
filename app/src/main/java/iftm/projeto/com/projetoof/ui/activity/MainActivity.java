package iftm.projeto.com.projetoof.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.activity.interfaces.OnEventoListenner;
import iftm.projeto.com.projetoof.ui.activity.interfaces.OnMenuItens;
import iftm.projeto.com.projetoof.ui.adapter.AdapterEvento;
import iftm.projeto.com.projetoof.ui.model.Evento;
import iftm.projeto.com.projetoof.ui.model.User;
import iftm.projeto.com.projetoof.ui.utils.Constantes;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMenuItens, OnEventoListenner {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private Context context;
    private boolean admin, payment, free;
    private OnMenuItens onMenuItens;
    private List<Evento> eventos;
    private List<Evento> eventosFiltro;
    private OnEventoListenner onEventoListenner;
    private ListView lista;
    private AdapterEvento adapter;
    private EditText busca;
    private boolean filtro;
    private Button cadastroEvento;

    @Override
    public void onResume() {
        super.onResume();

        myRef.child("evento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventos = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("DEU CERTO");
                    Evento e = new Evento(noteDataSnapshot.child("titulo").getValue(String.class), noteDataSnapshot.child("endereco").getValue(String.class), noteDataSnapshot.child("hora").getValue(String.class),
                            noteDataSnapshot.child("data").getValue(String.class), noteDataSnapshot.child("imagem").getValue(String.class), noteDataSnapshot.child("valorIngresso").getValue(String.class), noteDataSnapshot.getKey(),
                            (noteDataSnapshot.child("classificacao").getValue(String.class)));
                    eventos.add(e);

                }
                onEventoListenner.onEventoListener();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        cadastroEvento = findViewById(R.id.btn_cadastrar_evento);
        setSupportActionBar(toolbar);
        onMenuItens = this;
        onEventoListenner = this;
        context = this;
        admin = false;
        payment = false;
        free = false;
        filtro = false;
        lista = findViewById(R.id.list_eventos);
        busca = findViewById(R.id.et_busca);
        adapter = new AdapterEvento(eventos, this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        cadastroEvento.setVisibility(View.GONE);

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();

        eventosFiltro = new ArrayList<>();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Evento evento = (Evento) parent.getItemAtPosition(position);

                Intent it = new Intent(context, EventoActivity.class);
                it.putExtra("evento", (Serializable) evento);
                startActivity(it);
            }
        });


        getAllUsers();

    }

    @Override
    public void onBackPressed() {


    }


    private void getAllUsers() {
        myRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    User u = new User(noteDataSnapshot.child("email").getValue(String.class), noteDataSnapshot.child("senha").getValue(String.class), noteDataSnapshot.child("tipo").getValue(String.class),
                            noteDataSnapshot.child("uid").getValue(String.class));
                    if (u.getUid().equals(user.getUid())) {
                        if (u.getTipo().equals(Constantes.USER_SEM_PERMISSAO)) {
                            startActivity(new Intent(context, UserNotPermissionActivity.class));
                            MainActivity.this.finish();
                            break;
                        } else if (u.getTipo().equals(Constantes.USER_ADMIN)) {
                            //botaoCadastrarVisivel, tela aceita usuarios (altera tipo usuarios)
                            admin = true;
                            payment = false;
                            free = false;
                            onMenuItens.AdminsItens();
                            //altera, exclui e cria eventos
                        } else if (u.getTipo().equals(Constantes.USER_FREE)) {
                            admin = false;
                            payment = false;
                            free = true;
                            onMenuItens.AdminsItens();
                            //somente eventos
                        } else if (u.getTipo().equals(Constantes.USER_PAGO)) {
                            //eventos e altera eventos
                            admin = false;
                            payment = true;
                            free = false;
                            onMenuItens.AdminsItens();

                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Inicio
        }  else if (id == R.id.nav_slideshow) {
            //Gerenciar Usuarios
            startActivity(new Intent(this, GerenciarUsuariosActivity.class));

        } else if (id == R.id.nav_logout) {
            //
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cadastrarEvento(View view) {
        startActivity(new Intent(this, CadastrarEventoActivity.class));
    }

    @Override
    public void AdminsItens() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem gerenciarUsuarios = menu.findItem(R.id.nav_slideshow);
        //userLogado.setText(user.getEmail());
        if (payment || free) {
            gerenciarUsuarios.setVisible(false);
            cadastroEvento.setVisibility(View.GONE);

        } else {
            cadastroEvento.setVisibility(View.VISIBLE);
            gerenciarUsuarios.setVisible(true);
        }
    }

    @Override
    public void onEventoListener() {
        adapter = new AdapterEvento(eventos, this);
        lista.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        filtro = false;
    }

    public void buscaIdade(View view) {

        String buscaFiltro = busca.getText().toString();
        if (!buscaFiltro.isEmpty())
            adapter.getFilter(eventos).filter(buscaFiltro);
        else
            adapter.getFilter(eventos).filter("99");
    }
}

