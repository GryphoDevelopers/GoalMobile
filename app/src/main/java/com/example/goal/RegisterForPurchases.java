package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterForPurchases extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_purchases);
    }

    //Metodo para pular o Cadastro Completo
    public void SkipStep(View view){
        Intent intentIndex = new Intent(this, windowIndex.class);
        startActivity(intentIndex);
    }

}