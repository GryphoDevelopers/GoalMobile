package com.example.goal.views.activities;

import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_CNPJ;
import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_CPF;
import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_DATE;
import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_PHONE_BR;
import static com.example.goal.views.widgets.MaskInputPersonalized.managerMask;

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
import com.example.goal.models.api.UserAPI;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Activity SingUpActivity: Activity Responsavel por Controlar e Realizar o Cadastro do Usuario
 */
public class SingUpActivity extends AppCompatActivity {

    private TextInputEditText editDateBirth, editCpf, editCnpj, editPhoneBrazilian;
    private TextView errorOptionUser;
    private TextView errorTermsUse;

    private SwitchMaterial switch_documents, switch_phone;
    private RadioButton opClient, opSeller;
    private Button btn_createAcount;
    private Button see_termsUse;

    private MaterialCardView card_dataPersonal, card_documents, card_dataLogin, card_terms;
    private ScrollView scrollView;

    private Context context;
    private ManagerServices managerServices;
    private User userSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        // Instancia os Itens que serão usadis
        instanceItems();

        // Configura a Mudança Entre CPF-CNPJ e Telefones Brasileiros-Estrangeiros
        setUpDocument();
        setUpPhone();

        // Listeners dos Botões
        see_termsUse.setOnClickListener(v -> {
            // TODO IMPLEMENTAR LEITURA DO TERMO DE USO ---> NOVA ACTIVITY});
        });
        listenerSingUp();
    }

    /**
     * Instancia Itens que serão usados (Classes, Widgets)
     */
    private void instanceItems() {
        context = SingUpActivity.this;
        userSingUp = new User(context);
        managerServices = new ManagerServices(context);
        scrollView = findViewById(R.id.scrollView_singUp);

        // Insere as Mascaras no EditText
        editDateBirth = findViewById(R.id.edittext_dateBirth);
        editCpf = findViewById(R.id.edit_cpf);
        editCnpj = findViewById(R.id.edit_cnpj);
        editPhoneBrazilian = findViewById(R.id.edit_brPhone);
        editDateBirth.addTextChangedListener(managerMask(editDateBirth, MASK_DATE));
        editCpf.addTextChangedListener(managerMask(editCpf, MASK_CPF));
        editCnpj.addTextChangedListener(managerMask(editCnpj, MASK_CNPJ));
        editPhoneBrazilian.addTextChangedListener(managerMask(editPhoneBrazilian, MASK_PHONE_BR));

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);

        card_dataPersonal = findViewById(R.id.card_personalData);
        card_documents = findViewById(R.id.card_documments);
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

        // Configura os Switch Document e Phone
        switch_documents = findViewById(R.id.switch_document);
        switch_phone = findViewById(R.id.switch_phone);
    }

    /**
     * Configura os LayoutsEdits de Acordo com a Seleção no Switch
     */
    private void setUpDocument() {
        TextInputLayout layoutEdit_cpf = findViewById(R.id.layoutEdit_cpf);
        TextInputLayout layoutEdit_cnpj = findViewById(R.id.layoutEdit_cnpj);

        switch_documents.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Selecionou a Opção CNPJ
                layoutEdit_cpf.setVisibility(View.GONE);
                layoutEdit_cnpj.setVisibility(View.VISIBLE);
            } else {
                // Desselecionou a Opção CPF
                layoutEdit_cpf.setVisibility(View.VISIBLE);
                layoutEdit_cnpj.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Configura os LayoutsEdits de Acordo com a Seleção no Switch
     */
    private void setUpPhone() {
        TextInputLayout layoutEdit_brPhone = findViewById(R.id.layoutEdit_brPhone);
        TextInputLayout layoutEdit_foreignPhone = findViewById(R.id.layoutEdit_foreignPhone);

        switch_phone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Selecionou a Opção Estrangeiro
                layoutEdit_brPhone.setVisibility(View.GONE);
                layoutEdit_foreignPhone.setVisibility(View.VISIBLE);
            } else {
                // Desselecionou a Opção Estrangeiro
                layoutEdit_brPhone.setVisibility(View.VISIBLE);
                layoutEdit_foreignPhone.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Validação do Usuario (Nome, Nickname, Tipo do Usuario, Email, Senhas e Termo de Uso)
     */
    private boolean validationsSingUp() {
        User user = new User(context);
        ManagerInputErrors managerInputErrors = new ManagerInputErrors(context);

        // Obtem os Dados Inseridos nos Inputs
        TextInputEditText editName = findViewById(R.id.edittext_name);
        TextInputEditText editLastName = findViewById(R.id.edittext_lastName);
        TextInputEditText editNickname = findViewById(R.id.edittext_nickname);
        user.setName(Objects.requireNonNull(editName.getText()).toString());
        user.setLast_name(Objects.requireNonNull(editLastName.getText()).toString());
        user.setNickname(Objects.requireNonNull(editNickname.getText()).toString());
        String date_birth = Objects.requireNonNull(editDateBirth.getText()).toString();

        // Valida a Primeira Parte (Nome, Nickname e Tipo de Usuario)
        if (!user.validationName(user.getName())) {
            managerInputErrors.errorInputEditText(editName, user.getError_validation(), false);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!user.validationName(user.getLast_name())) {
            managerInputErrors.errorInputEditText(editLastName, user.getError_validation(), false);
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


        // Valida a Segunda Parte (CPF/CNPJ)
        if (switch_documents.isChecked()) {
            // CNPJ foi selecionado
            user.setCnpj(Objects.requireNonNull(editCnpj.getText()).toString());
            if (!user.validationCnpj(user.getMaskedCnpj())) {
                managerInputErrors.errorInputEditText(editCnpj, user.getError_validation(), false);
                card_documents.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else if (!user.validationNumberCnpj(user.getUnmaskCnpj())) {
                // Mostra os Possiveris erros de Validação CNPJ (API Externa)
                new AlertDialogPersonalized(context).defaultDialog(
                        getString(R.string.title_input_invalid, "CNPJ"),
                        user.getError_validation()).show();
                managerInputErrors.errorInputEditText(editCnpj, user.getError_validation(), false);
                card_documents.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        } else {
            // Usuario não selecionou = Opção CPF
            user.setCpf(Objects.requireNonNull(editCpf.getText()).toString());
            if (!user.validationCpf(user.getMaskedCpf())) {
                managerInputErrors.errorInputEditText(editCpf, user.getError_validation(), false);
                card_documents.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } //todo implementar validação do numero do cpf em api
        }

        // Valida a Segunda Parte (Telefone)
        if (switch_phone.isChecked()) {
            // Selecionou a Opção "Estrangeiro"
            TextInputEditText editForeignPhone = findViewById(R.id.edit_foreignPhone);
            user.setPhone(Objects.requireNonNull(editForeignPhone.getText()).toString());
            if (!user.validationInternationPhone(user.getMaskedPhone())) {
                managerInputErrors.errorInputEditText(editForeignPhone,
                        user.getError_validation(), false);
                card_documents.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        } else {
            // Manteve a Opção do Telefone Brasileiro
            user.setPhone(Objects.requireNonNull(editPhoneBrazilian.getText()).toString());
            if (!user.validationBrazilianPhone(user.getMaskedPhone())) {
                managerInputErrors.errorInputEditText(editPhoneBrazilian,
                        user.getError_validation(), false);
                card_documents.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        }
        // Passou por todas as Validações da Segunda Parte
        card_documents.setStrokeColor(getResources().getColor(R.color.lime_green));

        // Valida a Terceira Parte (Email, Internet e Consulta na API) e Senhas)
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
            errorTermsUse.setVisibility(View.VISIBLE);
            card_terms.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            // Passou por todas as validações: Define na Classe userSingUp (Usada p/ fazer o Cadastro)
            card_terms.setStrokeColor(getResources().getColor(R.color.lime_green));
            errorOptionUser.setVisibility(View.GONE);
            errorTermsUse.setVisibility(View.GONE);

            userSingUp.setName(user.getName());
            userSingUp.setLast_name(user.getLast_name());
            userSingUp.setDate_birth(date_birth);
            userSingUp.setNickname(user.getNickname());
            userSingUp.setSeller(opSeller.isChecked());

            if (switch_documents.isChecked()) userSingUp.setCnpj(user.getUnmaskCnpj());
            else userSingUp.setCpf(user.getUnmaskCpf());
            userSingUp.setPhone(user.getUnmaskPhone());

            userSingUp.setEmail(user.getEmail());
            userSingUp.setPassword(user.getPassword());
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
                return;
            } else if (!validationsSingUp()) {
                // Erro na Validação do Cadastro
                new SnackBarPersonalized(findViewById(R.id.layout_singup))
                        .defaultSnackBar(getString(R.string.error_singup)).show();
                return;
            }

            managerServices.closeKeyboard(SingUpActivity.this);

            // Registra o User na API e obtem a Instancia do User com ID da API
            UserAPI userAPI = new UserAPI(SingUpActivity.this);
            User userReturnedAPI = userAPI.registerInAPI(userSingUp);

            // Erro na API Goal
            if (userReturnedAPI == null) {
                new AlertDialogPersonalized(context).defaultDialog(
                        getString(R.string.title_no_register_api), userAPI.getError_operation()).show();
                return;
            }

            // Armazenará o Token e a Realização do Login
            ManagerSharedPreferences preferences = new ManagerSharedPreferences(
                    context, ManagerSharedPreferences.NAME_PREFERENCE);

            // Obtem o Token da API
            String token_user = userAPI.getTokenUser(userReturnedAPI.getEmail(), userReturnedAPI.getPassword());
            if (token_user.equals("")) {
                new AlertDialogPersonalized(SingUpActivity.this).defaultDialog(
                        getString(R.string.title_input_invalid, "Usuario"),
                        userAPI.getError_operation()).show();
                return;
            }

            // Define o "Lembrar Login" nas Preferences e o Token do Novo Usuario
            preferences.setJsonWebTokenUser(token_user);
            preferences.setRememberLogin(true);

            // Salva o Usuario no Banco de Dados
            ManagerDataBase managerDataBase = new ManagerDataBase(SingUpActivity.this);
            if (!managerDataBase.insertUser(userReturnedAPI)) {
                new AlertDialogPersonalized(SingUpActivity.this).defaultDialog(
                        getString(R.string.title_no_register_api),
                        managerDataBase.getError_operation()).show();
            } else {
                // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
                startActivity(new Intent(context, AddressActivity.class));
                finish();
            }


        });
    }

}