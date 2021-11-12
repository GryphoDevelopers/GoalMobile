package com.example.goal.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerServices;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.models.UserAPI;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.MaskInputPersonalized;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * Activity SingUpActivity: Activity Responsavel por Controlar e Realizar o Cadastro do Usuario
 */
public class SingUpActivity extends AppCompatActivity {

    private TextInputEditText editDateBirth;
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

        // Insere a Mascara no EditText
        editDateBirth = findViewById(R.id.edittext_dateBirth);
        editDateBirth.addTextChangedListener(
                MaskInputPersonalized.managerMask(editDateBirth, MaskInputPersonalized.MASK_DATE));

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
            userSingUp.setName(user.getName());
            userSingUp.setDate_birth(date_birth);
            userSingUp.setNickname(user.getNickname());
            userSingUp.setSeller(opSeller.isChecked());
            return true;
        }
    }

    /**
     * Listener do Botão "Cadastrar". Verifica as Validações e Cadastra o Usuario
     */
    public void listenerSingUp() {
        btn_createAcount.setOnClickListener(v -> {

            if (!managerServices.availableInternet()) {
                // Conexão de Internet Indisponivel
                new AlertDialogPersonalized(context).defaultDialog(
                        getString(R.string.title_no_internet),
                        Html.fromHtml(getString(R.string.error_network)).toString()).show();
            } else if (validationsSingUp()) {
                managerServices.closeKeyboard(SingUpActivity.this);

                // Tenta Registrar o Usuario na API
                if (UserAPI.registerInAPI(userSingUp)) {

                    // Obtem o Token da API
                    //todo armazenar token da api nas sharedpreferences
                    String token_user = UserAPI.getTokenUser(userSingUp.getEmail(), userSingUp.getPassword());
                    if (token_user.equals("")) {
                        new AlertDialogPersonalized(SingUpActivity.this).defaultDialog(
                                getString(R.string.title_input_invalid, "Usuario"),
                                Html.fromHtml(getString(R.string.error_login_api)).toString()).show();
                        return;
                    }

                    // Define TRUE para lembrar o Login que acabou de ser Feiro
                    ManagerSharedPreferences preferences = new ManagerSharedPreferences(
                            context, ManagerSharedPreferences.NAME_PREFERENCE);
                    preferences.rememberLogin(true);

                    // Salva o Usuario no Banco de Dados
                    ManagerDataBase managerDataBase = new ManagerDataBase(SingUpActivity.this);
                    if (!managerDataBase.insertUser(userSingUp)) {
                        new AlertDialogPersonalized(SingUpActivity.this).defaultDialog(
                                getString(R.string.title_no_register_api),
                                managerDataBase.getError_operation()).show();
                    } else {
                        // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
                        startActivity(new Intent(context, RegisterForPurchases.class));
                        finish();
                    }
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

}