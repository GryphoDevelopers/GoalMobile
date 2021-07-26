package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.R;

public class OpenActivity extends AppCompatActivity {

    private HandleSharedPreferences preferences;
    private final Handler handler = new Handler();

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        // Lê o arquivo da SharedPreferences ---> Existe ou não Login
        preferences = new HandleSharedPreferences(getSharedPreferences(PREFERENCE_LOGIN, 0));

        handler.postDelayed(() ->{
            if(preferences.existLogin()){
                startActivity(new Intent(getApplicationContext(), IndexActivity.class));
                finish();
            } else{
                startActivity(new Intent(getApplicationContext(), OptionActivity.class));
                finish();
            }
        }, 2000);

    }

}