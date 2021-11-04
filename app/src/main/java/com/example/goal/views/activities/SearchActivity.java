package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;

/**
 * Activity SearchActivity: Activity que irá realizar buscas na API de Produtos/Categorias
 */
public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // todo: remover. Apenas para testes
        Button button_home = findViewById(R.id.button_test6);
        button_home.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, IndexActivity.class));
            finish();
        });
    }
}