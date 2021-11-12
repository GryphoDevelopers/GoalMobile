package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerServices;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.models.UserAPI;
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
    private TextInputEditText edit_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.editTxt_emailLogin);
        userLogin = new User(LoginActivity.this);

        // Verifica se há um Usuario salvo no Banco de Dados
        checkAccounts();

        // Caso o Usuario opte por se Cadastrar
        Button btn_register = findViewById(R.id.btn_singup);
        btn_register.setOnClickListener(v -> startActivity(new
                Intent(LoginActivity.this, SingUpActivity.class)));

        Button btn_nextStage = findViewById(R.id.bnt_ignore);
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
     * Verifica se há uma Conta de um Usuario salva no Banco de Dados Local (SQLite)
     */
    private void checkAccounts() {
        ManagerDataBase managerDataBase = new ManagerDataBase(LoginActivity.this);
        User user = managerDataBase.getUserDatabase();

        if (user != null) edit_email.setText(user.getEmail());
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

    /**
     * Clique no Botão "Entrar". Caso os Dados sejam Validados, realiza o Login
     */
    public void loginUser() {
        // Instancia e Obtem o Listener do Botão
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {

            ManagerServices managerServices = new ManagerServices(LoginActivity.this);

            if (!managerServices.availableInternet()) {
                new AlertDialogPersonalized(LoginActivity.this).defaultDialog(
                        getString(R.string.title_no_internet),
                        Html.fromHtml(getString(R.string.error_network)).toString()).show();

            } else if (validationInputs()) {

                managerServices.closeKeyboard(this);
                // Obtem o Token do Usuario da API para obter mais Informações do Usuario
                //todo obter mais informações do Usuario, como Endereço, Metodos de Pagamento e Lista de Desejo
                //todo armazenar token da api nas sharedpreferences
                String token = UserAPI.getTokenUser(userLogin.getEmail(), userLogin.getPassword());
                User userAPI = token.equals("") ? null : UserAPI.getInfoUserAPI(userLogin.getEmail(),
                        userLogin.getPassword(), token, LoginActivity.this);

                if (userAPI == null) {
                    new AlertDialogPersonalized(LoginActivity.this).defaultDialog(
                            getString(R.string.title_input_invalid, "Usuario"),
                            Html.fromHtml(getString(R.string.error_login_api)).toString()).show();
                    return;
                }

                // Define o Valor do  "Lembrar Usuario" para as seções futuras
                MaterialCheckBox checkBox_remember = findViewById(R.id.checkbox_remember);
                ManagerSharedPreferences preferences = new ManagerSharedPreferences(LoginActivity.this,
                        ManagerSharedPreferences.NAME_PREFERENCE);
                preferences.rememberLogin(checkBox_remember.isChecked());

                // Salva o Usuario no Banco de Dados
                ManagerDataBase managerDataBase = new ManagerDataBase(LoginActivity.this);
                if (!managerDataBase.insertUser(userAPI)) {
                    new AlertDialogPersonalized(LoginActivity.this).defaultDialog(
                            getString(R.string.title_no_register_api),
                            managerDataBase.getError_operation()).show();
                } else {
                    // Inicia a Pagina Index (Produtos) e Finaliza essa Activity
                    startActivity(new Intent(LoginActivity.this, IndexActivity.class));
                    finishAffinity();
                }
            } else {
                new SnackBarPersonalized(findViewById(R.id.layout_initial))
                        .defaultSnackBar(getString(R.string.error_login)).show();
            }
        });
    }

}