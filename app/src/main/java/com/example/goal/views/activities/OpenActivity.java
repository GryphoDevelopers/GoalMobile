package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerResources.isNullOrEmpty;
import static com.example.goal.managers.ManagerSharedPreferences.NAME_PREFERENCE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.models.api.UserAPI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * OpenActivity: Activity que sempre controlará a primeira tela app
 */
public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        // Classes que serão utilizadas
        Context context = OpenActivity.this;
        ManagerSharedPreferences managerPreferences = new ManagerSharedPreferences(context, NAME_PREFERENCE);


        // Tarefa Assincrona de Busca do Usuario na API
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            if (managerPreferences.isRememberLogin()) {
                // Obtem o Usuario do Banco de Dados
                ManagerDataBase dataBase = new ManagerDataBase(context);
                User user_database = dataBase.getUserDatabase();

                // Obtem p Usuario do Banco de Dados par obter o TOken
                if (user_database != null) {
                    UserAPI userAPI = new UserAPI(context);
                    String token = userAPI.getTokenUser(executorService, user_database.getEmail(),
                            user_database.getPassword());

                    if (!isNullOrEmpty(token)) {
                        // Caso o Token Seja Valido, salva e Abre a Activity Principal
                        //todo: obter os dados do usuario da API
                        managerPreferences.setJsonWebTokenUser(token);
                        runOnUiThread(() -> {
                            startActivity(new Intent(context, IndexActivity.class));
                            finish();
                        });
                        return;
                    }
                }
            }

            // Caso o Usuario não tenha Internet, ou Login, ou a seleção do "Lembrar Login
            runOnUiThread(() -> {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            });
        });
    }

}