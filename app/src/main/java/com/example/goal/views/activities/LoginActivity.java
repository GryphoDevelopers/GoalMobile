package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerServices;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * Activity LoginActivity: Activity que realiza o Login ou Redireciona para o Cadastro ou Pula
 * direto para entrar no sistema sem Login
 */
public class LoginActivity extends AppCompatActivity {

    private User userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instancia dos Itens que serão Usados
        Button btn_register = findViewById(R.id.btn_singup);
        Button btn_nextStage = findViewById(R.id.bnt_ignore);
        userLogin = new User(LoginActivity.this);

        // Caso o Usuario opte por se Cadastrar
        btn_register.setOnClickListener(v -> startActivity(new
                Intent(LoginActivity.this, SingUpActivity.class)));

        btn_nextStage.setOnClickListener(v -> {
            // O Usuario sempre será Redirecionado à tela de Login/Cadastro antes de ir para a Index
            new ManagerSharedPreferences(LoginActivity.this, ManagerSharedPreferences.NAME_PREFERENCE)
                    .rememberLogin(false);
            startActivity(new Intent(LoginActivity.this, IndexActivity.class));
            finishAffinity();
        });

        // Listener do Botão Login
        loginUser();
    }

    /**
     * Clique no Botão "Entrar". Caso os Dados sejam Validados, realiza o Login
     */
    public void loginUser() {
        // Instancia e Obtem o Listener do Botão
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            if (!new ManagerServices(LoginActivity.this).availableInternet()) {
                new AlertDialogPersonalized(LoginActivity.this).defaultDialog(
                        getString(R.string.title_no_internet),
                        Html.fromHtml(getString(R.string.error_network)).toString()).show();
            } else if (validationInputs()) {
                new ManagerServices(LoginActivity.this).closeKeyboard(this);

                // TODO RETIRAR e implementar POST p/ API ---> Recebimento do Web Json Token
                Log.e("LOGIN", "Email: " + userLogin.getEmail() + "\nSenha: " + userLogin.getPassword());

                // Define o Valor do  "Lembrar Usuario" para as seções futuras
                MaterialCheckBox checkBox_remember = findViewById(R.id.checkbox_remember);
                ManagerSharedPreferences preferences = new ManagerSharedPreferences(LoginActivity.this,
                        ManagerSharedPreferences.NAME_PREFERENCE);
                preferences.rememberLogin(checkBox_remember.isChecked());

                // Inicia a Pagina Index (Produtos) e Finaliza essa Activity
                startActivity(new Intent(LoginActivity.this, IndexActivity.class));
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
        TextInputEditText edit_email = findViewById(R.id.editTxt_emailLogin);
        TextInputEditText edit_password = findViewById(R.id.editTxt_passwordLogin);
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