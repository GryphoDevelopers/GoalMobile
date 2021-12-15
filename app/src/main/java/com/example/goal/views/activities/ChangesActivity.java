package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerSharedPreferences.NAME_PREFERENCE;

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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity ChangesActivity: Activity que será alterada dinamicamente. Será possivel alterar os dados
 * caso seja validado e alterado na API
 */
public class ChangesActivity extends AppCompatActivity {

    private TextInputEditText edit_email;
    private TextInputEditText edit_password;
    private TextInputEditText edit_lastName;
    private TextInputEditText edit_name;
    private User user;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);

        Button button_home = findViewById(R.id.bnt_abortChanges);
        button_home.setOnClickListener(v -> finish());

        edit_email = findViewById(R.id.edit_changeEmail);
        edit_password = findViewById(R.id.edit_changePassword);
        edit_lastName = findViewById(R.id.edit_changeLastName);
        edit_name = findViewById(R.id.edit_changeName);
        context = ChangesActivity.this;
        user = new ManagerDataBase(context).getUserDatabase();
        user.setToken_user(new ManagerSharedPreferences(context, NAME_PREFERENCE).getJsonWebTokenUser());

        listenerChangeUser();
        changeUser();
    }

    private TextInputEditText validationInputs() {
        // Obtem os Dados do Input
        user.setEmail(Objects.requireNonNull(edit_email.getText()).toString());
        user.setPassword(Objects.requireNonNull(edit_password.getText()).toString());
        user.setLast_name(Objects.requireNonNull(edit_lastName.getText()).toString());
        user.setName(Objects.requireNonNull(edit_name.getText()).toString());

        if (!user.validationEmail(user.getEmail())) return edit_email;
        else if (!user.validationPassword(user.getPassword())) return edit_password;
        else if (!user.validationName(user.getName())) return edit_name;
        else if (!user.validationName(user.getLast_name())) return edit_lastName;
        else return null;
    }

    /**
     * Clique no Botão "Entrar". Caso os Dados sejam Validados, realiza o Login
     */
    public void changeUser() {
        // Instancia e Obtem o Listener do Botão
        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v -> {
            ManagerServices managerServices = new ManagerServices(context);
            AlertDialogPersonalized dialogPersonalized = new AlertDialogPersonalized(context);

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
                    handlerMain.post(() -> new ManagerInputErrors(ChangesActivity.this).errorInputEditText(
                            inputWrong, user.getError_validation(), false));
                    return;
                }

                // Obtem o Token do Usuario (Caso o Usuario não exista, o Token será "")
                UserAPI userAPI = new UserAPI(context);
                User userChanged = userAPI.updateUserAPI(executorService, user, user.getToken_user());
                if (userChanged == null) {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        dialogPersonalized.defaultDialog(getString(R.string.title_error_api),
                                getString(R.string.error_exception)).show();
                    });
                    return;
                }

                // Salva o Usuario no Banco de Dados
                ManagerDataBase managerDataBase = new ManagerDataBase(context);
                userChanged.setPassword(user.getPassword());
                if (!managerDataBase.insertUser(userChanged)) {
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
                        // Inicia a Pagina Index (Produtos) e Finaliza essa Activity
                        startActivity(new Intent(context, IndexActivity.class));
                        finishAffinity();
                    });
                }
            });
        });
    }

    public void listenerChangeUser() {
        SwitchMaterial switchSeller = findViewById(R.id.switch_seller);
        switchSeller.setChecked(user.isSeller());
        switchSeller.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                UserAPI productsAPI = new UserAPI(context);
                User user_changed = productsAPI.changeLevelUser(Executors.newCachedThreadPool(), user.getId_user(),
                        user.getToken_user());
                if (user_changed != null) {
                    user.setId_seller(user_changed.getId_seller());
                    user.setSeller(true);
                } else {
                    new AlertDialogPersonalized(context).defaultDialog(
                            getString(R.string.title_no_register_api),
                            getString(R.string.error_exception)).show();
                }
            } else {
                new AlertDialogPersonalized(context).defaultDialog(
                        getString(R.string.title_error_api), getString(R.string.error_returnUser)).show();
                switchSeller.setChecked(true);
            }
        });
    }
}