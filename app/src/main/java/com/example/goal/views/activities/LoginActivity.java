package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerKeyboard;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * Activity LoginActivity: Activity que realiza o Login ou Redireciona para o Cadastro ou Pula
 * direto para entrar no sistema sem Login
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edit_email;
    private TextInputEditText edit_password;
    private MaterialCheckBox checkBox_remember;
    private Button btn_login;
    private User userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Insntacia os Itens que serão usados
        instanceItens();

        Button btn_register = findViewById(R.id.btn_singup);
        Button btn_nextStage = findViewById(R.id.bnt_ignore);

        // Caso o Usuario opte por se Cadastrar
        btn_register.setOnClickListener(v -> startActivity(new
                Intent(this, SingUpActivity.class)));

        btn_nextStage.setOnClickListener(v -> {
            // O Usuario sempre será Redirecionado à tela de Login/Cadastro antes de ir para a Index
            new ManagerSharedPreferences(this, ManagerSharedPreferences.NAME_PREFERENCE)
                    .rememberLogin(false);
            startActivity(new Intent(this, IndexActivity.class));
            finishAffinity();
        });

        // Listener do Botão Login
        loginUser();
    }

    /**
     * Instancia os Itens que serão Usados na Activity
     */
    private void instanceItens() {
        // Classe do Usuario que será realizada o Login
        userLogin = new User(this);

        edit_email = findViewById(R.id.editTxt_emailLogin);
        edit_password = findViewById(R.id.editTxt_passwordLogin);
        checkBox_remember = findViewById(R.id.checkbox_remember);
        btn_login = findViewById(R.id.btn_login);
    }

    /**
     * Clique no Botão "Entrar". Caso os Dados sejam Validados, realiza o Login
     */
    public void loginUser() {
        btn_login.setOnClickListener(v -> {
            if (validationInputs()) {
                new ManagerKeyboard(LoginActivity.this).closeKeyboard(this);

                // TODO RETIRAR e implementar POST p/ API ---> Recebimento do Web Json Token
                Log.e("LOGIN", "Email: " + userLogin.getEmail() + "\nSenha: " + userLogin.getPassword());

                // Define o Valor do  "Lembrar Usuario" para as seções futuras
                ManagerSharedPreferences preferences = new ManagerSharedPreferences(this,
                        ManagerSharedPreferences.NAME_PREFERENCE);
                preferences.rememberLogin(checkBox_remember.isChecked());

                // Inicia a Pagina Index (Produtos) e Finaliza essa Activity
                startActivity(new Intent(this, IndexActivity.class));
                finishAffinity();
            } else {
                new SnackBarPersonalized(findViewById(R.id.layout_initial))
                        .defaultSnackBar(getString(R.string.error_login)).show();
            }
        });
    }

    /**
     * Valida os Dados Inseridos nos Inputs pelo Usuario
     *
     * @return true/false
     */
    private boolean validationInputs() {
        // Instancia as Classes que serão usadas
        ManagerInputErrors managerInputErrors = new ManagerInputErrors(LoginActivity.this);
        User user = new User(LoginActivity.this);

        // Obtem os Dados do Input
        user.setEmail(Objects.requireNonNull(edit_email.getText()).toString());
        user.setPassword(Objects.requireNonNull(edit_password.getText()).toString());

        if (!user.validationEmail(user.getEmail())) {
            managerInputErrors.errorInputEditText(edit_email, user.getError_validation(), false);
            return false;
        } else if (!user.validationPassword(user.getPassword())) {
            managerInputErrors.errorInputEditText(edit_password, user.getError_validation(), false);
            return false;
        } else {
            userLogin.setEmail(user.getEmail());
            userLogin.setPassword(user.getPassword());
            return true;
        }
    }

}