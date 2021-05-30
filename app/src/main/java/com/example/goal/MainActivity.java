package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Instancia a Pagina Login
    public void login(View view){
        Intent intentLogin = new Intent(this, LoginActivity.class);
        startActivity(intentLogin);
    }

    //Instancia a Pagina de Cadstro
    public void singIn(View view){
        Intent intentSignIn = new Intent(this, SingInActivity.class);
        startActivity(intentSignIn);
    }

    //Instancia a Pagina Index
    public void nextStage(View view) {
        Intent intentIndex = new Intent(this, windowIndex.class);
        startActivity(intentIndex);
    }

}