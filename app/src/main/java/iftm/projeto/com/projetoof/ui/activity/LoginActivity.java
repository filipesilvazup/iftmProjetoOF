package iftm.projeto.com.projetoof.ui.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import iftm.projeto.com.projetoof.R;
import iftm.projeto.com.projetoof.ui.model.User;
import iftm.projeto.com.projetoof.ui.utils.Constantes;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText senha;
    private LinearLayout linearLayout;
    private ProgressBar loading;
    private DatabaseReference myRef;
    private User usuario;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linearLayout = findViewById(R.id.linear_login);
        email = findViewById(R.id.edt_email);
        senha = findViewById(R.id.edt_senha);
        loading = findViewById(R.id.loading);
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

    }

    public void login(View view) {
        loading.setVisibility(View.VISIBLE);
        if (!email.getText().toString().equals("") && !senha.getText().toString().equals("")) {


            mAuth.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                loading.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Falha ao fazer login, tente novamente! :(", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    });
        } else {
            loading.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "E-mail e/ou senha inválidos! :(", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }

    public void createAccount(View view) {
        loading.setVisibility(View.VISIBLE);
        if (!email.getText().toString().equals("") && !senha.getText().toString().equals("")) {

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loading.setVisibility(View.GONE);
                                user = mAuth.getCurrentUser();
                                usuario = new User(email.getText().toString(), senha.getText().toString(), Constantes.USER_SEM_PERMISSAO, user.getUid()); //0 admin - 1 não pagante - 2 pagante
                                myRef.child("user").push().setValue(usuario);
                                Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Conta Criada com Sucesso! Faça o Login! :D", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                loading.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Falha ao criar a conta, tente novamente! :(", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                        }
                    });
        } else {
            loading.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "E-mail e/ou senha inválidos! :(", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void updateUI(FirebaseUser user) {
        loading.setVisibility(View.GONE);
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("USER", user);
        startActivity(it);
    }
}
