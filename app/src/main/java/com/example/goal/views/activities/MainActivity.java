package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.goal.R;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_register, btn_nextStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_cadastro);
        btn_nextStage = findViewById(R.id.bnt_ignore);

        btn_login.setOnClickListener(v -> login(v));

        btn_register.setOnClickListener(v -> register(v));

        btn_nextStage.setOnClickListener(v -> nextStage(v));


    }

    //Instancia a Pagina Login
    public void login(View view){
        Intent intentLogin = new Intent(this, LoginActivity.class);
        startActivity(intentLogin);
    }

    //Instancia a Pagina de Cadstro
    public void register(View view){
        Intent intentSignIn = new Intent(this, SingUpActivity.class);
        startActivity(intentSignIn);
    }

    //Instancia a Pagina Index
    public void nextStage(View view) {
        Intent intentIndex = new Intent(this, IndexActivity.class);
        startActivity(intentIndex);
    }

}