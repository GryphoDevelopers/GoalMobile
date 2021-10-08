package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.models.HandlerSharedPreferences;

/**
 * OpenActivity: Activity que sempre controlará a primeira tela app
 */
public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        // Instancia o Controlador de Preferences para verificar se existe ou não Login no APP
        HandlerSharedPreferences preferences = new HandlerSharedPreferences(this,
                HandlerSharedPreferences.NAME_PREFERENCE);

        // Deixa nessa Activity por 2 Segundos
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Inicia a Activity de Produtos ou Login
            startActivity(new Intent(getApplicationContext(),
                    preferences.existLogin() ? IndexActivity.class : LoginActivity.class));
            finish();
        }, 2000);
    }
}