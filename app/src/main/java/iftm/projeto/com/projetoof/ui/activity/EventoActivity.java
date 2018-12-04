package iftm.projeto.com.projetoof.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.model.Evento;
import iftm.projeto.com.projetoof.ui.model.User;
import iftm.projeto.com.projetoof.ui.utils.Constantes;

public class EventoActivity extends AppCompatActivity {

    private Evento evento;

    private ImageView imagemEvento;
    private TextView titulo;
    private TextView endereco;
    private TextView data;
    private TextView hora;
    private TextView valor;
    private TextView classificacao;
    private RelativeLayout relativeToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference myRef;

    private FloatingActionButton deletar;
    private FloatingActionButton editar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        getSupportActionBar().hide();
        relativeToolbar = findViewById(R.id.relative_toolbar);

        imagemEvento = findViewById(R.id.img_evento_selecionado);
        titulo = findViewById(R.id.tv_titulo_evento_selecionado);
        endereco = findViewById(R.id.tv_local_evento_selecionado);
        data = findViewById(R.id.tv_data_evento_selecionado);
        hora = findViewById(R.id.tv_hora_evento_selecionado);
        valor = findViewById(R.id.tv_valor_ingresso_selecionado);
        classificacao = findViewById(R.id.tv_classificacao_evento_selecionado);
        deletar = findViewById(R.id.fab_remove);
        editar = findViewById(R.id.fab_editar);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        relativeToolbar.setVisibility(View.VISIBLE);


        evento = (Evento) getIntent().getExtras().getSerializable("evento");
        if(evento != null){
            myRef = FirebaseDatabase.getInstance().getReference().child("evento").child(evento.getKey());

            Picasso.get()
                    .load(evento.getImagem())
                    .resize(300, 300)
                    .centerCrop()
                    .into(imagemEvento);

            titulo.setText(evento.getTitulo());
            endereco.setText(evento.getEndereco());
            data.setText(evento.getData());
            hora.setText(evento.getHora());
            valor.setText(evento.getValorIngresso());
            classificacao.setText(evento.getClassificacao()+" anos");
        }

        deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Tem certeza que deseja remover o evento?");
                // alert.setMessage("Message");

                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        myRef.removeValue();
                        Snackbar snackbar = Snackbar
                                .make(relativeToolbar, "Evento removido!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();



            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), CadastrarEventoActivity.class);
                it.putExtra("evento", evento);
                startActivity(it);
            }
        });


        getAllUsers();


    }

    private Context getContext() {
        return this;
    }

    private void getAllUsers() {
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    User u = new User(noteDataSnapshot.child("email").getValue(String.class), noteDataSnapshot.child("senha").getValue(String.class), noteDataSnapshot.child("tipo").getValue(String.class),
                            noteDataSnapshot.child("uid").getValue(String.class));
                    if (u.getUid().equals(user.getUid())) {
                         if (u.getTipo().equals(Constantes.USER_ADMIN)) {
                            //botaoCadastrarVisivel, tela aceita usuarios (altera tipo usuarios)
                             deletar.setVisibility(View.VISIBLE);
                             editar.setVisibility(View.VISIBLE);
                            //altera, exclui e cria eventos
                        } else if (u.getTipo().equals(Constantes.USER_FREE)) {
                             deletar.setVisibility(View.GONE);
                             editar.setVisibility(View.GONE);
                            //somente eventos
                        } else if (u.getTipo().equals(Constantes.USER_PAGO)) {
                            //eventos e altera eventos
                             deletar.setVisibility(View.GONE);
                             editar.setVisibility(View.VISIBLE);
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onBackActivity(View view) {
        relativeToolbar.setVisibility(View.GONE);
        finish();
    }
}
