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

    public void login(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void singIn(View view){
        Intent intent = new Intent(this, SingInActivity.class);
        startActivity(intent);
    }

    public void nextStage(View view) {
       /* Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/
        System.out.println("Pular Etapa");
    }



}