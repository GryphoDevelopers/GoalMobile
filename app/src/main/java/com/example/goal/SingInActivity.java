package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.RadioButton;

public class SingInActivity extends AppCompatActivity {

    private RadioButton opClient;
    private RadioButton opSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);

        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);

        opClient.setText(optionsUsers[0]);
        opSeller.setText(optionsUsers[1]);

    }
}