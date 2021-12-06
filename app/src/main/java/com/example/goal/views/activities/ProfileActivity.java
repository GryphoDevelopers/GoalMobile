package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;

/**
 * Activity ProfileActivity: Activity que irá mostrar os dados de Cadatro do Usuario
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // todo: remover. Apenas para testes
        Button button_home = findViewById(R.id.button_test03);
        button_home.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, IndexActivity.class));
            finish();
        });

        Button button_details = findViewById(R.id.button_test3);
        button_details.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, DetailsActivity.class));
            finish();
        });
    }
}