package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerResources.isNullOrEmpty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerServices;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.models.api.UserAPI;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity LoginActivity: Activity que realiza o Login ou Redireciona para o Cadastro ou Pula
 * direto para entrar no sistema sem Login
 */
public class LoginActivity extends AppCompatActivity {

    private Context context;
    private User userLogin;
    private TextInputEditText edit_email;
    private TextInputEditText edit_password;
    private Button btn_login;
    private AlertDialogPersonalized dialogPersonalized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instancia os Itens que serão usados
        instanceItems();

        // Verifica se há um Usuario salvo no Banco de Dados
        checkAccounts();

        // Caso o Usuario opte por se Cadastrar
        Button btn_register = findViewById(R.id.btn_singup);
        btn_register.setOnClickListener(v -> startActivity(new
                Intent(context, SingUpActivity.class)));

        // Listener do Botão Login
        loginUser();
    }

    /**
     * Instancia os Itens que serão usados na Activity
     */
    private void instanceItems() {
        context = LoginActivity.this;
        userLogin = new User(context);
        dialogPersonalized = new AlertDialogPersonalized(context);

        edit_email = findViewById(R.id.editTxt_emailLogin);
        edit_password = findViewById(R.id.editTxt_passwordLogin);
        btn_login = findViewById(R.id.btn_login);
    }

    /**
     * Verifica se há uma Conta de um Usuario salva no Banco de Dados Local (SQLite)
     */
    private void checkAccounts() {
        ManagerDataBase managerDataBase = new ManagerDataBase(context);
        User user = managerDataBase.getUserDatabase();

        if (user != null) edit_email.setText(user.getEmail());
    }

    /**
     * Valida os Dados Inseridos nos Inputs pelo Usuario
     *
     * @return {@link TextInputEditText}|null
     */
    private TextInputEditText validationInputs() {
        // Obtem os Dados do Input
        userLogin.setEmail(Objects.requireNonNull(edit_email.getText()).toString());
        userLogin.setPassword(Objects.requireNonNull(edit_password.getText()).toString());

        if (!userLogin.validationEmail(userLogin.getEmail())) return edit_email;
        else if (!userLogin.validationPassword(userLogin.getPassword())) return edit_password;
        else return null;
    }

    /**
     * Clique no Botão "Entrar". Caso os Dados sejam Validados, realiza o Login
     */
    public void loginUser() {
        // Instancia e Obtem o Listener do Botão
        btn_login.setOnClickListener(v -> {
            ManagerServices managerServices = new ManagerServices(context);

            // Fecha o Teclado (Caso esteja aberto)
            managerServices.closeKeyboard(this);

            // Cria um AlertDialog do Estilo "Carregando..."
            AlertDialog dialogLoading = dialogPersonalized.loadingDialog(
                    getString(R.string.message_loadingSingIn), false);
            dialogLoading.show();

            // Exibirá os Resultados sempre na Thread Principal
            Handler handlerMain = new Handler(Looper.getMainLooper());

            // Executa as Validações e Cadatros em uma Tarefa Assincrona
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(() -> {

                // Valida os Inputs para Verificar se há algum erro
                TextInputEditText inputWrong = validationInputs();
                if (inputWrong != null) {
                    // Remove o Dialog "Carregando..." e Mostra o Erro do Input
                    dialogLoading.dismiss();
                    handlerMain.post(() -> new ManagerInputErrors(context).errorInputEditText(
                            inputWrong, userLogin.getError_validation(), false));
                    return;
                }

                // Obtem o Token do Usuario (Caso o Usuario não exista, o Token será "")
                UserAPI userAPI = new UserAPI(context);
                User user_token = userAPI.getTokenUser(executorService, userLogin.getEmail(),
                        userLogin.getPassword());
                if (user_token == null || isNullOrEmpty(user_token.getToken_user())) {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        dialogPersonalized.defaultDialog(
                                getString(R.string.title_input_invalid, "Usuario"),
                                userAPI.getError_operation()).show();
                    });
                    return;
                }

                // todo obter informações do Usuario (Extrato)
                User user_receivedAPI = userAPI.getInfoUserAPI(executorService,
                        user_token.getId_user(), user_token.getToken_user());
                if (user_receivedAPI == null) {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        dialogPersonalized.defaultDialog(getString(R.string.title_error_api),
                                userAPI.getError_operation()).show();
                    });
                    return;
                }

                // Armazena os Valores Simples que serão utilizados depois no APP
                ManagerSharedPreferences preferences = new ManagerSharedPreferences(
                        context, ManagerSharedPreferences.NAME_PREFERENCE);
                preferences.setJsonWebTokenUser(user_token.getToken_user());

                // Define o Valor do "Lembrar Usuario
                MaterialCheckBox checkBox_remember = findViewById(R.id.checkbox_remember);
                preferences.setRememberLogin(checkBox_remember.isChecked());

                // Salva o Usuario no Banco de Dados
                ManagerDataBase managerDataBase = new ManagerDataBase(context);
                user_receivedAPI.setPassword(userLogin.getPassword());
                if (!managerDataBase.insertUser(user_receivedAPI)) {
                    // Erro ao salvar o Usuario no Banco de Dados Local (SQLite)
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        dialogPersonalized.defaultDialog(
                                getString(R.string.title_no_register_api),
                                managerDataBase.getError_operation()).show();
                    });
                } else {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        dialogPersonalized.messageWithCloseWindow(this,
                                getString(R.string.message_ok),
                                getString(R.string.message_updateUser)).show();
                    });
                }
            });
        });

    }

}