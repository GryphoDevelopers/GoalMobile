package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.goal.R;

public class RegisterForPurchases extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_purchases);
    }

    //Metodo para pular o Cadastro Completo
    public void skipStep(View view){
        // Obtem o Context da View passada
        context = view.getContext();
        Intent intentIndex = new Intent(context, IndexActivity.class);
        context.startActivity(intentIndex);
    }

}