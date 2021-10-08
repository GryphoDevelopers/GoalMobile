package com.example.goal.views.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.controllers.InputErrors;
import com.example.goal.controllers.ManagerKeyboard;
import com.example.goal.models.HandlerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.views.AlertDialogPersonalized;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SingUpActivity extends AppCompatActivity {

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

    private MaterialCardView card_dataPersonal, card_dataLogin, card_terms;
    private ScrollView scrollView;

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

    /**
     * Instancia Itens que serão usados (Classes, Widgets)
     */
    private void instanceItens() {
        userSingUp = new User(this);
        managerKeyboard = new ManagerKeyboard(getApplicationContext());
        scrollView = findViewById(R.id.scrollView_singUp);

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

    /**
     * Recupera os valores (string.xml) do Array e coloca nas Opções
     */
    private void setTypeUser() {
        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);
        opSeller.setText(optionsUsers[0]);
        opClient.setText(optionsUsers[1]);
    }

    /**
     * Validação do Usuario (Nome, Nickname, Tipo do Usuario, Email, Senhas e Termo de Uso)
     */
    private boolean validationsSingUp() {
        User user = new User(this);
        InputErrors inputErrors = new InputErrors(this);

        // Obtem os Dados Inseridos nos Inputs
        user.setName(Objects.requireNonNull(editName.getText()).toString());
        user.setNickname(Objects.requireNonNull(editNickname.getText()).toString());
        user.setEmail(Objects.requireNonNull(editEmail.getText()).toString());
        user.setPassword(Objects.requireNonNull(editPassword.getText()).toString());
        user.setConfirmPassword(Objects.requireNonNull(editConfirmPassword.getText()).toString());

        // Valida a Primeira Parte (Nome, Nickname e Tipo de Usuario)
        if (!user.validationName(user.getName())) {
            inputErrors.errorInputWithoutIcon(editName, user.getError_validation());
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationNickname(user.getNickname())) {
            inputErrors.errorInputWithoutIcon(editNickname, user.getError_validation());
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!opClient.isChecked() && !opSeller.isChecked()) {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            errorOptionUser.setVisibility(View.VISIBLE);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else card_dataPersonal.setStrokeColor(getResources().getColor(R.color.lime_green));
        errorOptionUser.setVisibility(View.GONE);

        // Valida a Segunda Parte (Email e Senhas)
        if (!user.validationEmail(user.getEmail())) {
            inputErrors.errorInputWithoutIcon(editEmail, user.getError_validation());
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationPassword(user.getPassword())) {
            inputErrors.errorInputWithoutIcon(editPassword, user.getError_validation());
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationConfirmPassword(user)) {
            inputErrors.errorInputWithoutIcon(editConfirmPassword, user.getError_validation());
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else card_dataLogin.setStrokeColor(getResources().getColor(R.color.lime_green));

        // Valida os Temos de Uso
        if (!checkTermsUse.isChecked()) {
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.lime_green));
            errorTermsUse.setVisibility(View.VISIBLE);
            card_terms.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            // Passou por todas as validações: Define na Classe userSingUp (Usada p/ fazer o Cadastro)
            card_terms.setStrokeColor(getResources().getColor(R.color.lime_green));
            errorOptionUser.setVisibility(View.GONE);
            errorTermsUse.setVisibility(View.GONE);

            userSingUp.setEmail(user.getEmail());
            userSingUp.setPassword(user.getPassword());
            userSingUp.setConfirmPassword(user.getConfirmPassword());
            userSingUp.setName(user.getName());
            userSingUp.setNickname(user.getNickname());
            userSingUp.setSeller(opSeller.isChecked());
            userSingUp.setCheckedTermsUse(checkTermsUse.isChecked());
            return true;
        }
    }

    /**
     * Listener do Botão "Cadastrar". Verifica as Validações e Cadastra o Usuario
     */
    public void listenerSingUp() {
        createAcount.setOnClickListener(v -> {
            // Valida o Cadatro e Insere o Cadastro na API
            if (validationsSingUp()) {
                if (!registerInAPI(userSingUp)) {
                    new AlertDialogPersonalized(this).defaultDialog(
                            getString(R.string.title_no_register_api),
                            getString(R.string.error_register_api)).show();
                }

                managerKeyboard.closeKeyboard(this);
                // Define TRUE para login Realizado
                HandlerSharedPreferences preferences = new HandlerSharedPreferences(this,
                        HandlerSharedPreferences.NAME_PREFERENCE);
                preferences.setLogin(true);

                // todo: inserir o registro no banco de dados Local (cada novo registro = limpa o banco)
                // TODO RETIRAR e implementar POST p/ API
                Log.e("SING UP", "Nome: " + userSingUp.getName() + "\nEmail: " +
                        userSingUp.getEmail() + "\nNickname:" + userSingUp.getNickname() +
                        "\nSenha: " + userSingUp.getPassword() + "\nConfirmar Senha: " +
                        userSingUp.getConfirmPassword() + "\nOpção Usuario: " +
                        userSingUp.isSeller() + "\nTermos de Uso: " + userSingUp.isCheckedTermsUse());

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

    /**
     * Insere um Usuario no Banco de Dados via API
     *
     * @param userSingUp Usuario inserido no Banco de Dados
     * @return boolean
     */
    private boolean registerInAPI(User userSingUp) {
        //todo: implementação futura
        return true;
    }

}