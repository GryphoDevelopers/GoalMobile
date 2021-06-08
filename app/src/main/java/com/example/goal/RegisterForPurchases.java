package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.List;

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
        Intent intentIndex = new Intent(context, windowIndex.class);
        context.startActivity(intentIndex);
    }

}