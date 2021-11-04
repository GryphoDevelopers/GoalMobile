package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;

/**
 * Activity DetailsActivity: Activity que irá exibir os detalhes de um Produto. Será utilizada de
 * forma dinamica, obtendo dados a partir de um Bundle de uma outra Activity/Fragment
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // todo: remover. Apenas para testes
        Button button_home = findViewById(R.id.button_test2);
        button_home.setOnClickListener(v -> {
            startActivity(new Intent(DetailsActivity.this, IndexActivity.class));
            finish();
        });
    }
}