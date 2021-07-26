package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.goal.R;
import com.example.goal.controller.ManagerKeyboard;
import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.textfield.TextInputEditText;

public class OptionActivity extends AppCompatActivity {

    private TextInputEditText edit_email;
    private TextInputEditText edit_password;

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";
    private ManagerKeyboard managerKeyboard;
    private SnackBarPersonalized snackBarPersonalized;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        managerKeyboard = new ManagerKeyboard(getApplicationContext());

        edit_email = findViewById(R.id.editTxt_emailLogin);
        edit_password = findViewById(R.id.editTxt_passwordLogin);

        Button singUp = findViewById(R.id.btn_singUp);
        Button btn_register = findViewById(R.id.btn_singup);
        Button btn_nextStage = findViewById(R.id.bnt_ignore);

        btn_register.setOnClickListener(v -> startActivity(new
                Intent(this, SingUpActivity.class)));

        btn_nextStage.setOnClickListener(v -> startActivity(new
                Intent(this, IndexActivity.class)));

        singUp.setOnClickListener(v -> validationLogin());

    }

    //Metodo acionado pelo botão 'Entrar' --> Recupera e Valida os Dados
    public void validationLogin(){
        if (filledFields()) {
            managerKeyboard.closeKeyboard(this);

            Log.e("LOGIN","Email: " + email + "\nSenha: " + password);

            HandleSharedPreferences preferences = new HandleSharedPreferences(
                    getSharedPreferences(PREFERENCE_LOGIN,0));

            // Login Realizado ---> Define nas SharedPreferences do Login como True
            preferences.setLogin(true);

            // Inicia a Pagina Index (Produtos) e Finaliza essa Activity
            startActivity(new Intent(this, IndexActivity.class));
            finish();
        } else {
            snackBarPersonalized = new SnackBarPersonalized(findViewById(R.id.layout_initial));
            snackBarPersonalized.makeDefaultSnackBar(R.string.error_login);
        }
    }

    // Verifica se os Campos estão preenchidos
    public boolean filledFields(){
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();

        if (email.equals("")) {
            edit_email.setError(getString(R.string.errorInputs));
            edit_email.requestFocus();
            managerKeyboard.openKeyboard(edit_email);
            return false;
        } else if (password.equals("")) {
            edit_password.setError(getString(R.string.errorInputs), null);
            edit_password.requestFocus();
            managerKeyboard.openKeyboard(edit_password);
            return false;
        } else return true;
    }

}