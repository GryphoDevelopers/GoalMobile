package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;

/**
 * Activity WishesActivity: Actiity que irá Listar os Produtos que o Usuario Adicionou à Lista de Desejos
 */
public class WishesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishes);

        // todo: remover. Apenas para testes
        Button button_home = findViewById(R.id.button_test5);
        button_home.setOnClickListener(v -> {
            startActivity(new Intent(WishesActivity.this, IndexActivity.class));
            finish();
        });
        // Caso não Tenha nehum item salvo ---> Mostrar mensagem de nenhum item salvo
        // Mostrar Itens pelo Recycler View (Obter id do SQLite, buscar e serializar da API)
    }
}