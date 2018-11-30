package iftm.projeto.com.projetoof.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import iftm.projeto.com.projetoof.R;

public class UserNotPermissionActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_not_permission);
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

    }

    public void logOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
    }
}
