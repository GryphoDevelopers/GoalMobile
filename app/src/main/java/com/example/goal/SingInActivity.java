package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class SingInActivity extends AppCompatActivity {

    private RadioButton opClient, opSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        //Recupera os valores colocados no Array (dentro do strings.xml)
        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);

        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);

        //Coloca os valores do Array nas respectivas opções
        opClient.setText(optionsUsers[0]);
        opSeller.setText(optionsUsers[1]);

    }

    //Metodo para pular o Cadastro Completo
    public void SkipStep(View view){
        Intent intentIndex = new Intent(this, windowIndex.class);
        startActivity(intentIndex);
    }



}




