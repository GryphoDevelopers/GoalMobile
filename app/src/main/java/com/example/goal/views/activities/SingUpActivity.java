package com.example.goal.views.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.goal.R;
import com.example.goal.controllers.InputErrors;
import com.example.goal.controllers.ManagerKeyboard;
import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SingUpActivity extends AppCompatActivity {

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";

    private TextView errorOptionUser;
    private TextView errorTermsUse;

    private TextInputEditText editName;
    private TextInputEditText editNickname;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirmPassword;

    private RadioButton opClient, opSeller;
    private CheckBox checkTermsUse;
    private Button createAcount;
    private Button see_termsUse;

    //private ConstraintLayout layout_personal, layout_login;
    private MaterialCardView card_dataPersonal, card_dataLogin, card_terms;

    private ManagerKeyboard managerKeyboard;
    private User userSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        instanceItens();
        setTypeUser();

        // Listeners dos Botões
        see_termsUse.setOnClickListener(v -> {
            // TODO IMPLEMENTAR LEITURA DO TERMO DE USO ---> NOVA ACTIVITY});
        });
        listenerSingUp();
    }

    private void instanceItens() {

        userSingUp = new User();
        managerKeyboard = new ManagerKeyboard(getApplicationContext());

        editName = findViewById(R.id.edittext_name);
        editNickname = findViewById(R.id.edittext_nickname);
        editEmail = findViewById(R.id.edittext_email);
        editPassword = findViewById(R.id.edittext_password);
        editConfirmPassword = findViewById(R.id.edittext_confirmPassword);

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);

        card_dataPersonal = findViewById(R.id.card_personalData);
        card_dataLogin = findViewById(R.id.card_loginData);
        card_terms = findViewById(R.id.card_terms_use);

        createAcount = findViewById(R.id.button_createAcount);
        see_termsUse = findViewById(R.id.see_termsUse);
        checkTermsUse = findViewById(R.id.cbx_termsUse);
        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);
    }

    //Recupera os valores colocados no Array (dentro do strings.xml) e coloca nas Opções
    private void setTypeUser() {
        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);
        opSeller.setText(optionsUsers[0]);
        opClient.setText(optionsUsers[1]);
    }

    // Validação do Nome, Nickname, Tipo do Usuario, Email, Senhas e Termo de Uso
    private boolean validationsSingUp() {
        User user = new User();
        InputErrors inputErrors = new InputErrors(this);

        // Obtem a String de Validação ---> Ok = Validado, se não = Mensagem de Erro
        user.setName(Objects.requireNonNull(editName.getText()).toString());
        user.setNickname(Objects.requireNonNull(editNickname.getText()).toString());
        user.setEmail(Objects.requireNonNull(editEmail.getText()).toString());
        user.setPassword(Objects.requireNonNull(editPassword.getText()).toString());
        user.setConfirmPassword(Objects.requireNonNull(editConfirmPassword.getText()).toString());
        String validationName = user.validationName(user.getName());
        String validationNickname = user.validationNickname(user.getNickname());
        String validationEmail = user.validationEmail(user.getEmail());
        String validationPassword = user.validationPassword(user.getPassword());
        String validationConfirmPassword = user.validationConfirmPassword(user);

        if (!validationName.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(editName, validationName);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!validationNickname.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(editNickname, validationNickname);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!opClient.isChecked() && !opSeller.isChecked()) {
            errorOptionUser.setVisibility(View.VISIBLE);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else card_dataPersonal.setStrokeColor(getResources().getColor(R.color.lime_green));

        if (!validationEmail.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(editEmail, validationEmail);
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!validationPassword.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(editPassword, validationPassword);
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!validationConfirmPassword.equals(User.OK)) {
            inputErrors.errorInputWithoutIcon(editConfirmPassword, validationConfirmPassword);
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else card_dataLogin.setStrokeColor(getResources().getColor(R.color.lime_green));

        if (!checkTermsUse.isChecked()) {
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.lime_green));
            errorTermsUse.setVisibility(View.VISIBLE);
            card_terms.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            card_terms.setStrokeColor(getResources().getColor(R.color.lime_green));
            errorTermsUse.setVisibility(View.GONE);
            errorOptionUser.setVisibility(View.GONE);

            userSingUp.setEmail(editEmail.getText().toString());
            userSingUp.setPassword(editPassword.getText().toString());
            userSingUp.setConfirmPassword(editConfirmPassword.getText().toString());
            userSingUp.setName(editName.getText().toString());
            userSingUp.setNickname(editNickname.getText().toString());
            userSingUp.setSeller(opSeller.isChecked());
            userSingUp.setCheckedTermsUse(true);
            return true;
        }
    }


    // Cadastra o Usuario
    public void listenerSingUp() {
        createAcount.setOnClickListener(v -> {

            // Valida o Cadatro e Insere o Cadastro na API
            if (validationsSingUp()) {

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
            SnackBarPersonalized snackBar = new SnackBarPersonalized(findViewById(R.id.layout_singup));
            snackBar.makeDefaultSnackBar(R.string.error_singup).show();
        });
    }

}