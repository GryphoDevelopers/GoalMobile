package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email;
    private EditText edit_password;
    private Button singUp;

    private String email, password;

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.editTxt_emailLogin);
        edit_password = findViewById(R.id.editTxt_passwordLogin);
        singUp = findViewById(R.id.btn_singUp);

        singUp.setOnClickListener(v -> validationSingUp(v));

    }

    //Metodo acionado pelo botão 'Entrar'
    //Recupera os valores e realiza uma Validação
    public void validationSingUp(View view){

        if (filledFields()) {
            // Validação retornou True
            System.out.println("Email: " + email + "\nSenha: " + password);

            HandleSharedPreferences preferences = new HandleSharedPreferences(
                    getSharedPreferences(PREFERENCE_LOGIN,0));
            // Define TRUE para login Realizado
            preferences.setLogin();

            // Inicia a Pagina Index (Produtos) e Finaliza essa Activity
            Intent indexPage = new Intent(this, IndexActivity.class);
            startActivity(indexPage);
            finish();
        } else {
            System.out.println("Não foi Possivel Logar o Usuario");
        }
    }

    // Verifica se os Campos estão preenchidos
    public boolean filledFields(){
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();

        if (email.equals("")) {
            edit_email.setError("Campo obrigatório");
            return false;
        } else if (password.equals("")) {
            //TODO - É possivel adicionar um Icone apos a msg de erro
            edit_password.setError("Campo obrigatório");
            return false;
        } else{
            return true;
        }
    }

}