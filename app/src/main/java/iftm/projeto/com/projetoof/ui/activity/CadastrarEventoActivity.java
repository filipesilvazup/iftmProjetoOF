package iftm.projeto.com.projetoof.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.model.Evento;

public class CadastrarEventoActivity extends AppCompatActivity {

    EditText nome;
    EditText horaIni;
    EditText dataIni;
    EditText imagem;
    EditText endereco;
    EditText valorIngresso;
    EditText classificacao;
    TextView tvTitulo;
    Evento evento;
    DatabaseReference myRef;
    LinearLayout linearLayout;
    Button cadastro;
    boolean editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_evento);
        linearLayout = findViewById(R.id.layout_cadastrar);
        nome = findViewById(R.id.tv_nome_evento);
        horaIni = findViewById(R.id.tv_hora_inicio_evento);
        dataIni = findViewById(R.id.tv_data_inicio_evento);
        imagem = findViewById(R.id.tv_url_evento);
        endereco = findViewById(R.id.tv_local_evento);
        classificacao = findViewById(R.id.tv_menor_idade);
        valorIngresso = findViewById(R.id.tv_valor_ingresso_evento);
        cadastro = findViewById(R.id.bt_cadastro);
        tvTitulo = findViewById(R.id.tv_titulo);
        myRef = FirebaseDatabase.getInstance().getReference();
        editar = false;
        cadastro.setText("Cadastrar Evento");

        if (getIntent().hasExtra("evento")) {
            evento = (Evento) getIntent().getExtras().get("evento");
            cadastro.setText("Alterar Evento");
            tvTitulo.setText("Alterar Evento");
            preencher();
        }


    }

    private void preencher() {
        nome.setText(evento.getTitulo());
        horaIni.setText(evento.getHora());
        dataIni.setText(evento.getData());
        imagem.setText(evento.getImagem());
        endereco.setText(evento.getEndereco());
        classificacao.setText(evento.getClassificacao() + "");
        valorIngresso.setText(evento.getValorIngresso());
        editar = true;
    }

    public void cadastrar(View view) {

        if (!editar) {
            evento = new Evento(nome.getText().toString(), endereco.getText().toString(), horaIni.getText().toString(), dataIni.getText().toString(),
                    imagem.getText().toString(), valorIngresso.getText().toString(), "", classificacao.getText().toString());

            if (evento != null) {


                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Tem certeza que deseja cadastrar esse evento?");
                // alert.setMessage("Message");

                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        myRef.child("evento").push().setValue(evento);
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "Evento cadastrado com sucesso!!!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();


            }
        } else {


            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Tem certeza que deseja Alterar o evento?");
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    myRef = FirebaseDatabase.getInstance().getReference().child("evento").child(evento.getKey());
                    evento = new Evento(nome.getText().toString(), endereco.getText().toString(), horaIni.getText().toString(), dataIni.getText().toString(),
                            imagem.getText().toString(), valorIngresso.getText().toString(), "", classificacao.getText().toString());
                    Map<String, String> map = new HashMap<>();
                    map.put("classificacao", evento.getClassificacao() + "");
                    map.put("data", evento.getData());
                    map.put("endereco", evento.getEndereco());
                    map.put("imagem", evento.getImagem());
                    map.put("key", evento.getKey());
                    map.put("titulo", evento.getTitulo());
                    map.put("valorIngresso", evento.getValorIngresso());
                    myRef.updateChildren((HashMap) map);
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Evento Alterado!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
            alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getContext(), MainActivity.class));
    }
}
