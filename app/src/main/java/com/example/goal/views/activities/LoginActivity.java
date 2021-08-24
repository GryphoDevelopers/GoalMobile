package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.controller.InputErrors;
import com.example.goal.controller.ManagerKeyboard;
import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";
    private TextInputEditText edit_email;
    private TextInputEditText edit_password;
    private Button btn_login;
    private InputErrors inputErrors;
    private ManagerKeyboard managerKeyboard;
    private SnackBarPersonalized snackBarPersonalized;
    private User userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        managerKeyboard = new ManagerKeyboard(getApplicationContext());
        inputErrors = new InputErrors(this);
        userLogin = new User();

        edit_email = findViewById(R.id.editTxt_emailLogin);
        edit_password = findViewById(R.id.editTxt_passwordLogin);

        btn_login = findViewById(R.id.btn_login);
        Button btn_register = findViewById(R.id.btn_singup);
        Button btn_nextStage = findViewById(R.id.bnt_ignore);

        btn_register.setOnClickListener(v -> startActivity(new
                Intent(this, SingUpActivity.class)));

        // Pula a parte do Login
        btn_nextStage.setOnClickListener(v -> {
            // todo: Implementar metodo para salvar que o usuario escolher pular essa etapa
            startActivity(new Intent(this, IndexActivity.class));
            finish();
        });

        btn_login.setOnClickListener(v -> loginUser());

    }

    //Metodo acionado pelo botão 'Entrar' --> Recupera e Valida os Dados
    public void loginUser() {
        btn_login.setOnClickListener(v -> {

            if (validationInputs()) {
                managerKeyboard.closeKeyboard(this);

                // TODO RETIRAR e implementar POST p/ API ---> Recebimento do Web Json Token
                Log.e("LOGIN", "Email: " + userLogin.getEmail() + "\nSenha: " + userLogin.getPassword());

                // Login Realizado ---> Define nas SharedPreferences do Login como True
                HandleSharedPreferences preferences = new HandleSharedPreferences(
                        getSharedPreferences(PREFERENCE_LOGIN, 0));
                preferences.setLogin(true);

                // Inicia a Pagina Index (Produtos) e Finaliza essa Activity
                startActivity(new Intent(this, IndexActivity.class));
                finish();

            } else {
                snackBarPersonalized = new SnackBarPersonalized(findViewById(R.id.layout_initial));
                snackBarPersonalized.makeDefaultSnackBar(R.string.error_login);
            }

        });
    }

    private boolean validationInputs() {
        User user = new User();

        String validationEmail = user.validationEmail(Objects.requireNonNull(
                edit_email.getText()).toString());
        String validationPassword = user.validationPassword(Objects.requireNonNull(
                edit_password.getText()).toString());

        if (!validationEmail.equals(User.OK)) {
            inputErrors.errorInputEditText(edit_email, validationEmail);
            return false;
        } else if (!validationPassword.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(edit_password, validationPassword);
            return false;
        } else {
            user.setEmail(edit_email.getText().toString());
            user.setPassword(edit_password.getText().toString());
            return true;
        }
    }

}