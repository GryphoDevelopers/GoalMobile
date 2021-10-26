package com.example.goal.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerServices;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * Activity SingUpActivity: Activity Responsavel por Controlar e Realizar o Cadastro do Usuario
 */
public class SingUpActivity extends AppCompatActivity {

    private TextView errorOptionUser;
    private TextView errorTermsUse;

    private RadioButton opClient, opSeller;
    private Button btn_createAcount;
    private Button see_termsUse;

    private MaterialCardView card_dataPersonal, card_dataLogin, card_terms;
    private ScrollView scrollView;

    private Context context;
    private ManagerServices managerServices;
    private User userSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        // Instancia os Itens que serão usadis
        instanceItens();

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
        context = SingUpActivity.this;
        userSingUp = new User(context);
        managerServices = new ManagerServices(context);
        scrollView = findViewById(R.id.scrollView_singUp);

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);

        card_dataPersonal = findViewById(R.id.card_personalData);
        card_dataLogin = findViewById(R.id.card_loginData);
        card_terms = findViewById(R.id.card_terms_use);

        btn_createAcount = findViewById(R.id.button_createAcount);
        see_termsUse = findViewById(R.id.see_termsUse);

        // Configura o Texto das Opções "Vendedores" e "Clientes"
        String[] optionsUsers = getResources().getStringArray(R.array.options_peopleType);
        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);
        opSeller.setText(optionsUsers[0]);
        opClient.setText(optionsUsers[1]);
    }

    /**
     * Validação do Usuario (Nome, Nickname, Tipo do Usuario, Email, Senhas e Termo de Uso)
     */
    private boolean validationsSingUp() {
        User user = new User(context);
        ManagerInputErrors managerInputErrors = new ManagerInputErrors(context);

        // Obtem os Dados Inseridos nos Inputs
        TextInputEditText editName = findViewById(R.id.edittext_name);
        TextInputEditText editNickname = findViewById(R.id.edittext_nickname);
        TextInputEditText editDateBirth = findViewById(R.id.edittext_dateBirth);
        user.setName(Objects.requireNonNull(editName.getText()).toString());
        user.setNickname(Objects.requireNonNull(editNickname.getText()).toString());
        String date_birth = Objects.requireNonNull(editDateBirth.getText()).toString();

        // Valida a Primeira Parte (Nome, Nickname e Tipo de Usuario)
        if (!user.validationName(user.getName())) {
            managerInputErrors.errorInputEditText(editName, user.getError_validation(), false);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationDateBirth(date_birth)) {
            managerInputErrors.errorInputEditText(editDateBirth, user.getError_validation(), false);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationNickname(user.getNickname())) {
            managerInputErrors.errorInputEditText(editNickname, user.getError_validation(), false);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!opClient.isChecked() && !opSeller.isChecked()) {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            errorOptionUser.setVisibility(View.VISIBLE);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.lime_green));
            errorOptionUser.setVisibility(View.GONE);
        }

        // Valida a Segunda Parte (Email, Internet e Consulta na API) e Senhas)
        TextInputEditText editEmail = findViewById(R.id.edittext_email);
        TextInputEditText editPassword = findViewById(R.id.edittext_password);
        TextInputEditText editConfirmPassword = findViewById(R.id.edittext_confirmPassword);
        user.setEmail(Objects.requireNonNull(editEmail.getText()).toString());
        user.setPassword(Objects.requireNonNull(editPassword.getText()).toString());
        user.setConfirmPassword(Objects.requireNonNull(editConfirmPassword.getText()).toString());

        if (!user.validationEmail(user.getEmail())) {
            managerInputErrors.errorInputEditText(editEmail, user.getError_validation(), false);
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationEmailAPI(user.getEmail())) {
            managerInputErrors.errorInputEditText(editEmail, user.getError_validation(), false);
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            // Cria um AlertDialog na Tela
            new AlertDialogPersonalized(context).defaultDialog(
                    getString(R.string.title_input_invalid, "Email"),
                    Html.fromHtml(user.getError_validation()).toString()).show();
            return false;
        } else if (!user.validationPassword(user.getPassword())) {
            managerInputErrors.errorInputEditText(editPassword, user.getError_validation(), false);
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationConfirmPassword(user)) {
            managerInputErrors.errorInputEditText(editConfirmPassword, user.getError_validation(), false);
            card_dataLogin.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else card_dataLogin.setStrokeColor(getResources().getColor(R.color.lime_green));

        // Valida os Temos de Uso
        CheckBox cbx_termsUse = findViewById(R.id.cbx_termsUse);

        if (!cbx_termsUse.isChecked()) {
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
            userSingUp.setDate_birth(date_birth);
            userSingUp.setNickname(user.getNickname());
            userSingUp.setSeller(opSeller.isChecked());
            userSingUp.setCheckedTermsUse(cbx_termsUse.isChecked());
            return true;
        }
    }

    /**
     * Listener do Botão "Cadastrar". Verifica as Validações e Cadastra o Usuario
     */
    public void listenerSingUp() {
        btn_createAcount.setOnClickListener(v -> {
            if (!new ManagerServices(context).availableInternet()) {
                // Conexão de Internet Indisponivel
                new AlertDialogPersonalized(context).defaultDialog(
                        getString(R.string.title_no_internet),
                        Html.fromHtml(getString(R.string.error_network)).toString()).show();
            } else if (validationsSingUp()) {

                // Tenta Registrar na API
                if (registerInAPI(userSingUp)) {
                    managerServices.closeKeyboard(SingUpActivity.this);

                    // Define TRUE para lembrar o Login que acabou de ser Feiro
                    ManagerSharedPreferences preferences = new ManagerSharedPreferences(
                            context, ManagerSharedPreferences.NAME_PREFERENCE);
                    preferences.rememberLogin(true);

                    // todo: inserir o registro no banco de dados Local (cada novo registro = limpa o banco)
                    // TODO RETIRAR e implementar POST p/ API
                    Log.e("SING UP", "Nome: " + userSingUp.getName() + "\nEmail: " +
                            userSingUp.getEmail() + "\nData de Nascimento: " + userSingUp.getDate_birth().toString()
                            + "\nNickname:" + userSingUp.getNickname() + "\nSenha: " +
                            userSingUp.getPassword() + "\nConfirmar Senha: " +
                            userSingUp.getConfirmPassword() + "\nOpção Usuario: " + userSingUp.isSeller()
                            + "\nTermos de Uso: " + userSingUp.isCheckedTermsUse());

                    // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
                    startActivity(new Intent(context, RegisterForPurchases.class));
                    finish();
                } else {
                    // Erro na API Goal
                    new AlertDialogPersonalized(context).defaultDialog(
                            getString(R.string.title_no_register_api),
                            getString(R.string.error_register_api)).show();
                }
            } else {
                // Erro na Validação do Cadastro
                new SnackBarPersonalized(findViewById(R.id.layout_singup))
                        .defaultSnackBar(getString(R.string.error_singup)).show();
            }
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