package com.example.goal.views.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.goal.R;
import com.example.goal.controllers.InputErrors;
import com.example.goal.controllers.ManagerKeyboard;
import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SingUpActivity extends AppCompatActivity {

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";

    private TextView errorOptionUser;
    private TextView errorTermsUse;
    private TextView progress_page;

    private TextInputEditText editName;
    private TextInputEditText editNickname;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirmPassword;

    private RadioButton opClient, opSeller;
    private CheckBox checkTermsUse;
    private Button createAcount;
    private Button see_termsUse;
    private Button next_stage;
    private Button back_stage;

    private ConstraintLayout layout_personal, layout_login;

    private ManagerKeyboard managerKeyboard;
    private InputErrors inputErrors;
    private User userSingUp;

    private int position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        instanceItens();
        configTypeUser();

        // Configura o Texto das Etapas
        progress_page.setText(getString(R.string.status_page, 1, 2));

        // Listeners dos Botões
        listenerNextStage();
        listenerBackStage();
        see_termsUse.setOnClickListener(v -> {
            // TODO IMPLEMENTAR LEITURA DO TERMO DE USO ---> NOVA ACTIVITY});
        });
        listenerSingUp();
    }

    private void instanceItens() {

        userSingUp = new User();
        managerKeyboard = new ManagerKeyboard(getApplicationContext());
        inputErrors = new InputErrors(this);

        layout_personal = findViewById(R.id.layout_dataPersonal);
        layout_login = findViewById(R.id.layout_dataLogin);

        editName = findViewById(R.id.edittext_name);
        editNickname = findViewById(R.id.edittext_nickname);
        editEmail = findViewById(R.id.edittext_email);
        editPassword = findViewById(R.id.edittext_password);
        editConfirmPassword = findViewById(R.id.edittext_confirmPassword);

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);
        progress_page = findViewById(R.id.txt_statusProgress);

        next_stage = findViewById(R.id.button_nextSingUp);
        back_stage = findViewById(R.id.button_backStage);
        createAcount = findViewById(R.id.button_createAcount);
        see_termsUse = findViewById(R.id.see_termsUse);
        checkTermsUse = findViewById(R.id.cbx_termsUse);
        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);
    }

    //Recupera os valores colocados no Array (dentro do strings.xml) e coloca nas Opções
    private void configTypeUser() {
        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);
        opSeller.setText(optionsUsers[0]);
        opClient.setText(optionsUsers[1]);
    }

    // Listener do Botão Inferiro "Proxima Etapa"
    private void listenerNextStage() {
        next_stage.setOnClickListener(v -> {

            if (position == 1 && validationPersonalInfo()) {
                back_stage.setVisibility(View.VISIBLE);
                layout_personal.setVisibility(View.GONE);
                layout_login.setVisibility(View.VISIBLE);
                position = 2;
            }

            managerKeyboard.closeKeyboard(this);
            progress_page.setText(getString(R.string.status_page, position, 2));

        });
    }

    // Listener do Botão Inferior "Voltar"
    private void listenerBackStage() {
        back_stage.setOnClickListener(v -> {

            if (position == 2) {
                // Volta p/ os Dados Pessoais
                back_stage.setVisibility(View.INVISIBLE);
                layout_login.setVisibility(View.GONE);
                layout_personal.setVisibility(View.VISIBLE);
                position = 1;
            }

            managerKeyboard.closeKeyboard(this);
            progress_page.setText(getString(R.string.status_page, position, 2));
        });
    }

    // Validação do Nome, Nickname e Tipo do Usuario
    private boolean validationPersonalInfo() {
        // Obtem os Valores dos Inputs
        User user = new User();
        user.setName(Objects.requireNonNull(editName.getText()).toString());
        user.setNickname(Objects.requireNonNull(editNickname.getText()).toString());

        // Obtem a String de Validação ---> Ok = Validado, se não = Mensagem de Erro
        String validationName = user.validationName(user.getName());
        String validationNickname = user.validationNickname(user.getNickname());

        if (!validationName.equals(User.OK)) {
            inputErrors.errorInputEditText(editName, validationName);
            return false;
        } else if (!validationNickname.equals(User.OK)) {
            inputErrors.errorInputEditText(editNickname, validationNickname);
            return false;
        } else if (!opClient.isChecked() && !opSeller.isChecked()) {
            errorOptionUser.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorOptionUser.setVisibility(View.GONE);
            userSingUp.setName(editName.getText().toString());
            userSingUp.setNickname(editNickname.getText().toString());
            userSingUp.setSeller(opSeller.isChecked());
            return true;
        }
    }

    // Validação do Email e Senha
    private boolean validationLoginInfo() {
        User user = new User();

        // Obtem os Valores dos Inputs
        user.setEmail(Objects.requireNonNull(editEmail.getText()).toString());
        user.setPassword(Objects.requireNonNull(editPassword.getText()).toString());
        user.setConfirmPassword(Objects.requireNonNull(editConfirmPassword.getText()).toString());

        // Obtem a String de Validação ---> Ok = Validado, se não = Mensagem de Erro
        String validationEmail = user.validationEmail(user.getEmail());
        String validationPassword = user.validationPassword(user.getPassword());
        String validationConfirmPassword = user.validationConfirmPassword(user);

        if (!validationEmail.equals(User.OK)) {
            inputErrors.errorInputEditText(editEmail, validationEmail);
            return false;
        } else if (!validationPassword.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(editPassword, validationPassword);
            return false;
        } else if (!validationConfirmPassword.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(editConfirmPassword, validationConfirmPassword);
            return false;
        } else if (!checkTermsUse.isChecked()) {
            errorTermsUse.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorTermsUse.setVisibility(View.GONE);
            userSingUp.setEmail(editEmail.getText().toString());
            userSingUp.setPassword(editPassword.getText().toString());
            userSingUp.setConfirmPassword(editConfirmPassword.getText().toString());
            userSingUp.setCheckedTermsUse(true);
            return true;
        }
    }

    // Cadastra o Usuario
    public void listenerSingUp() {

        createAcount.setOnClickListener(v -> {

            // Valida Novamente o Cadatro --->  Insere o Cadastro na API
            if (userSingUp.isCheckedTermsUse() && validationPersonalInfo() && validationLoginInfo()) {

                managerKeyboard.closeKeyboard(this);

                // Define TRUE para login Realizado
                HandleSharedPreferences preferences = new HandleSharedPreferences(
                        getSharedPreferences(PREFERENCE_LOGIN, 0));
                preferences.setLogin(true);

                // TODO RETIRAR e implementar POST p/ API
                Log.e("SING UP", "Nome: " + userSingUp.getName() + "\nEmail: " +
                        userSingUp.getEmail() + "\nNickname:" + userSingUp.getNickname() +
                        "\nSenha: " + userSingUp.getPassword() + "\nConfirmar Senha: " +
                        userSingUp.getConfirmPassword() + "\nOpção Usuario: " +
                        userSingUp.isSeller() + "\nTermos de Uso: " + userSingUp.getConfirmPassword());

                // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
                startActivity(new Intent(this, RegisterForPurchases.class));
                finish();
                return;
            }

            // Erro no Cadastro
            SnackBarPersonalized snackBar = new SnackBarPersonalized(findViewById(R.id.layouts_register));
            snackBar.makeDefaultSnackBar(R.string.error_singup).show();
        });
    }

}