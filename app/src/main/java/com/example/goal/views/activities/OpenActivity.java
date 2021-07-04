package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.goal.HandleSharedPreferences;
import com.example.goal.R;

public class OpenActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Handler handler = new Handler();

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";
    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando dados");
        progressDialog.setMessage("\n" + getString(R.string.title_index));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();


        new Thread(() -> {

            // Lê o arquivo da SharedPreferences ---> Existe ou não Login
            HandleSharedPreferences preferences = new HandleSharedPreferences(
                    getSharedPreferences(PREFERENCE_LOGIN, 0));

            while (progressStatus <100){

                progressStatus++;
                handler.post(() -> progressDialog.setProgress(progressStatus));

                try{
                    // Implementa +1 no Contador a cada 50 milisegundos
                    Thread.sleep(50);
                } catch (InterruptedException i){
                    i.printStackTrace();
                }

            }

            progressDialog.dismiss();

            // return true = Já foi feito Login; false = não foi feito Login
            if(preferences.existLogin()){
                Intent indexPage = new Intent(getApplicationContext(), IndexActivity.class);
                startActivity(indexPage);
            } else{
                Intent optionPage = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(optionPage);
            }

            // Encerra o cliclo de vida da atividade atual
            finish();
        }).start();

    }

}