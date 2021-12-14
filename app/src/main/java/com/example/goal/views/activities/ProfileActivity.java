package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;

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

        Button button_details = findViewById(R.id.button_change);
        button_details.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ChangesActivity.class));
            finish();
        });

        User user = new ManagerDataBase(ProfileActivity.this).getUserDatabase();
        Log.e("Teste", user.getName() + "\n" + user.getLast_name() + "\n" + user.getEmail() +
                "\n" + user.getPassword() + "\n" + user.getId_user() + "\n" + user.getId_seller() + "\n"
                + new ManagerSharedPreferences(ProfileActivity.this,
                ManagerSharedPreferences.NAME_PREFERENCE).getJsonWebTokenUser());
    }
}