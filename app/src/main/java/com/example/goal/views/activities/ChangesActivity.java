package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;

/**
 * Activity ChangesActivity: Activity que será alterada dinamicamente. Será possivel alterar os dados
 * caso seja validado e alterado na API
 */
public class ChangesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);

        // todo: remover. Apenas para testes
        Button button_home = findViewById(R.id.btn_save);
        button_home.setOnClickListener(v -> {
            startActivity(new Intent(ChangesActivity.this, IndexActivity.class));
            finish();
        });
    }
}