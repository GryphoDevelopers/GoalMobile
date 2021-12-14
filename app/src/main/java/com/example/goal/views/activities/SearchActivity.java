package com.example.goal.views.activities;

import android.os.Bundle;

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
    }
}