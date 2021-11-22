package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_CNPJ;
import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_CPF;
import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_DATE;
import static com.example.goal.views.widgets.MaskInputPersonalized.MASK_PHONE_BR;
import static com.example.goal.views.widgets.MaskInputPersonalized.managerMask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity SingUpActivity: Activity Responsavel por Controlar e Realizar o Cadastro do Usuario
 */
public class SingUpActivity extends AppCompatActivity {

    private TextInputEditText editDateBirth;
    private TextInputEditText editCpf;
    private TextInputEditText editCnpj;
    private TextInputEditText editPhoneBrazilian;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirmPassword;
    private TextInputEditText editName;
    private TextInputEditText editLastName;
    private TextInputEditText editNickname;

    private TextView errorOptionUser;
    private TextView errorTermsUse;

    private SwitchMaterial switch_documents, switch_phone;
    private RadioButton opClient, opSeller;
    private Button btn_createAcount;
    private Button see_termsUse;

    /**
     * Armazena os Cards que os dados já estão validados
     */
    private MaterialCardView[] card_corrects;
    private MaterialCardView card_dataPersonal, card_documents, card_dataLogin, card_terms;
    private ScrollView scrollView;

    private Context context;
    private AlertDialogPersonalized dialogPersonalized;
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
        dialogPersonalized = new AlertDialogPersonalized(context);

        // Insere as Mascaras no EditText
        editDateBirth = findViewById(R.id.edittext_dateBirth);
        editCpf = findViewById(R.id.edit_cpf);
        editCnpj = findViewById(R.id.edit_cnpj);
        editPhoneBrazilian = findViewById(R.id.edit_brPhone);
        editDateBirth.addTextChangedListener(managerMask(editDateBirth, MASK_DATE));
        editCpf.addTextChangedListener(managerMask(editCpf, MASK_CPF));
        editCnpj.addTextChangedListener(managerMask(editCnpj, MASK_CNPJ));
        editPhoneBrazilian.addTextChangedListener(managerMask(editPhoneBrazilian, MASK_PHONE_BR));

        editName = findViewById(R.id.edittext_name);
        editLastName = findViewById(R.id.edittext_lastName);
        editNickname = findViewById(R.id.edittext_nickname);
        editEmail = findViewById(R.id.edittext_email);
        editPassword = findViewById(R.id.edittext_password);
        editConfirmPassword = findViewById(R.id.edittext_confirmPassword);

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);

        card_corrects = new MaterialCardView[4];
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
     *
     * @return {@link TextInputEditText}|null
     */
    private TextInputEditText validationsSingUp() {
        // Obtem os Dados Inseridos nos Inputs
        userSingUp.setName(Objects.requireNonNull(editName.getText()).toString());
        userSingUp.setLast_name(Objects.requireNonNull(editLastName.getText()).toString());
        userSingUp.setNickname(Objects.requireNonNull(editNickname.getText()).toString());
        String date_birth = Objects.requireNonNull(editDateBirth.getText()).toString();

        // Valida a Primeira Parte (Nome, Nickname e Tipo de Usuario)
        if (!userSingUp.validationName(userSingUp.getName())) return editName;
        else if (!userSingUp.validationName(userSingUp.getLast_name())) return editLastName;
        else if (!userSingUp.validationDateBirth(date_birth)) return editDateBirth;
        else if (!userSingUp.validationNickname(userSingUp.getNickname())) return editNickname;
        else if (validationTypeUser() == null) card_corrects[0] = card_dataPersonal;
        else userSingUp.setDate_birth(date_birth);

        // Valida a Segunda Parte (CPF/CNPJ)
        if (switch_documents.isChecked()) {
            // CNPJ foi selecionado
            userSingUp.setCnpj(Objects.requireNonNull(editCnpj.getText()).toString());
            if (!userSingUp.validationCnpj(userSingUp.getMaskedCnpj())) {
                card_corrects[1] = null;
                return editCnpj;
            }
        } else {
            // Usuario não selecionou = Opção CPF
            userSingUp.setCpf(Objects.requireNonNull(editCpf.getText()).toString());
            if (!userSingUp.validationCpf(userSingUp.getMaskedCpf())) {
                card_corrects[1] = null;
                return editCpf;
            }
            //todo implementar validação do numero do cpf em api
        }

        // Valida a Segunda Parte (Telefone)
        if (switch_phone.isChecked()) {
            // Selecionou a Opção "Estrangeiro"
            TextInputEditText editForeignPhone = findViewById(R.id.edit_foreignPhone);
            userSingUp.setPhone(Objects.requireNonNull(editForeignPhone.getText()).toString());
            if (!userSingUp.validationInternationPhone(userSingUp.getMaskedPhone())) {
                card_corrects[1] = null;
                return editForeignPhone;
            }
        } else {
            // Manteve a Opção do Telefone Brasileiro
            userSingUp.setPhone(Objects.requireNonNull(editPhoneBrazilian.getText()).toString());
            if (!userSingUp.validationBrazilianPhone(userSingUp.getMaskedPhone())) {
                card_corrects[1] = null;
                return editPhoneBrazilian;
            }
        }

        // Valida a Terceira Parte (Email, Internet e Consulta na API) e Senhas)
        userSingUp.setEmail(Objects.requireNonNull(editEmail.getText()).toString());
        userSingUp.setPassword(Objects.requireNonNull(editPassword.getText()).toString());
        userSingUp.setConfirmPassword(Objects.requireNonNull(editConfirmPassword.getText()).toString());
        if (!userSingUp.validationEmail(userSingUp.getEmail())) {
            card_corrects[2] = null;
            return editEmail;
        } else if (!userSingUp.validationPassword(userSingUp.getPassword())) {
            card_corrects[2] = null;
            return editPassword;
        } else if (!userSingUp.validationConfirmPassword(userSingUp)) {
            card_corrects[2] = null;
            return editConfirmPassword;
        }

        // Atribui os Valores Corretos após ter Executado todas as Validações
        if (switch_documents.isChecked()) userSingUp.setCnpj(userSingUp.getUnmaskCnpj());
        else userSingUp.setCpf(userSingUp.getUnmaskCpf());
        userSingUp.setPhone(userSingUp.getUnmaskPhone());
        return null;
    }

    /**
     * Valida o CNPJ e Telefone em uma API.
     * <p>
     * * Caso haja erro, o Titulo estará na Posição 0 e a Mensagem na Posição 1
     *
     * @param executorService {@link ExecutorService} utilizado nas consultas e validações em APIs
     * @return String[]|null
     */
    private String[] validationAPI(ExecutorService executorService) {
        if (!userSingUp.validationNumberCnpj(executorService, userSingUp.getUnmaskCnpj())) {
            // Obtem os Possiveis erros de Validação CNPJ (API Externa)
            return new String[]{getString(R.string.title_input_invalid, "CNPJ"),
                    userSingUp.getError_validation()};
        } else if (!userSingUp.validationEmailAPI(executorService, userSingUp.getEmail())) {
            // Obtem se o Erro se o email é descartavel
            return new String[]{getString(R.string.title_input_invalid, "Email"),
                    Html.fromHtml(userSingUp.getError_validation()).toString()};
        } else {
            // Passou por todas as Validações da Segunda Parte
            card_corrects[1] = card_documents;
            card_corrects[2] = card_dataLogin;
            return null;
        }
    }

    /**
     * Valida se o Usuario aceitou os Termos de Uso
     * <p>
     * *Retorno é o Texto exibindo o Erro
     *
     * @return {@link TextView}|null
     */
    private TextView validationTermsUse() {
        // Valida os Temos de Uso
        CheckBox cbx_termsUse = findViewById(R.id.cbx_termsUse);
        if (cbx_termsUse.isChecked()) {
            card_corrects[3] = card_terms;
            return null;
        } else return errorTermsUse;
    }

    /**
     * Valida se o Usuario selecionou algum Tipo de Usuario (Vendedor, Cliente)
     * <p>
     * *Retorno é o Texto exibindo o Erro
     *
     * @return {@link TextView}|null
     */
    private TextView validationTypeUser() {
        // Valida o Tipo de Usuario
        if (!opClient.isChecked() && !opSeller.isChecked()) return errorOptionUser;
        else {
            userSingUp.setSeller(opSeller.isChecked());
            return null;
        }
    }

    /**
     * Listener do Botão "Cadastrar". Verifica as Validações e Cadastra o Usuario
     */
    public void listenerSingUp() {
        btn_createAcount.setOnClickListener(v -> {

            // Dialog que exibe um Alerta de "Carregando..." na Tela
            AlertDialog dialogLoading = dialogPersonalized.loadingDialog(
                    getString(R.string.message_loadingSingUp, "Usuario"), false);

            try {
                // Fecha o Teclado caso esteja Aberto
                managerServices.closeKeyboard(SingUpActivity.this);
                dialogLoading.show();

                // Exibirá os Resultados sempre na Thread Principal
                Handler handlerMain = new Handler(Looper.getMainLooper());

                // Realiza as Tarefas Assincronas
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(() -> {

                    // Obtem o Resultado das Validações
                    TextInputEditText wrongSingUp = validationsSingUp();
                    String[] errorAPI = validationAPI(executorService);
                    TextView wrongTypeUser = validationTypeUser();
                    TextView wrongTermsUse = validationTermsUse();

                    // Atribui as Cores nos Cards já Validados
                    for (MaterialCardView item : card_corrects) {
                        if (item != null) handlerMain.post(() -> item.setStrokeColor(
                                getResources().getColor(R.color.lime_green)));
                    }

                    if (wrongSingUp != null) {
                        handlerMain.post(() -> {
                            dialogLoading.dismiss();
                            new ManagerInputErrors(context).errorInputEditText(wrongSingUp,
                                    userSingUp.getError_validation(), false);
                        });
                        return;
                    } else if (wrongTypeUser != null) {
                        handlerMain.post(() -> {
                            dialogLoading.dismiss();
                            wrongTypeUser.setVisibility(View.VISIBLE);
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        });
                        return;
                    } else if (errorAPI != null) {
                        handlerMain.post(() -> {
                            dialogLoading.dismiss();
                            dialogPersonalized.defaultDialog(errorAPI[0], errorAPI[1]).show();
                        });
                        return;
                    } else if (wrongTermsUse != null) {
                        handlerMain.post(() -> {
                            dialogLoading.dismiss();
                            wrongTermsUse.setVisibility(View.VISIBLE);
                        });
                        return;
                    } else {
                        // Tudo foi Validado
                        handlerMain.post(() -> {
                            errorOptionUser.setVisibility(View.GONE);
                            errorTermsUse.setVisibility(View.GONE);
                        });
                    }

                    // Registra o User na API e obtem a Instancia do User com ID da API
                    UserAPI userAPI = new UserAPI(context);
                    User userReturnedAPI = userAPI.registerInAPI(executorService, userSingUp);
                    if (userReturnedAPI == null) {
                        handlerMain.post(() -> {
                            // Erro no Cadastro da API Invalida. Tira o Loading e Exibe o Erro
                            dialogLoading.dismiss();
                            dialogPersonalized.defaultDialog(getString(R.string.title_no_register_api),
                                    userAPI.getError_operation()).show();
                        });
                        return;
                    }

                    // Armazenará o Token e a Realização do Login
                    ManagerSharedPreferences preferences = new ManagerSharedPreferences(context,
                            ManagerSharedPreferences.NAME_PREFERENCE);

                    // Obtem e Valida o Token gerado pela API
                    String token_user = userAPI.getTokenUser(executorService, userReturnedAPI.getEmail(),
                            userReturnedAPI.getPassword());
                    if (token_user.equals("")) {
                        handlerMain.post(() -> {
                            // Erro no Cadastro da API Invalida. Tira o Loading e Exibe o Erro
                            dialogLoading.dismiss();
                            dialogPersonalized.defaultDialog(
                                    getString(R.string.title_input_invalid, "Usuario"),
                                    userAPI.getError_operation()).show();
                        });
                        return;
                    }

                    // Define o "Lembrar Login" nas Preferences e o Token do Novo Usuario
                    preferences.setJsonWebTokenUser(token_user);
                    preferences.setRememberLogin(true);

                    // Salva o Usuario no Banco de Dados
                    ManagerDataBase managerDataBase = new ManagerDataBase(context);
                    if (!managerDataBase.insertUser(userReturnedAPI)) {
                        handlerMain.post(() -> {
                            // Erro no Salavar o Usuario no Banco de DAdos Local (SQLite)
                            dialogLoading.dismiss();
                            dialogPersonalized.defaultDialog(
                                    getString(R.string.title_no_register_api),
                                    managerDataBase.getError_operation()).show();
                        });
                    } else {
                        handlerMain.post(() -> {
                            dialogLoading.dismiss();
                            // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
                            startActivity(new Intent(context, AddressActivity.class));
                            finish();
                        });
                    }
                });

            } catch (Exception ex) {
                Log.e(EXCEPTION, "SingUpActivity - Erro no Cadastro do Usuario: " + ex.getClass().getName());
                ex.printStackTrace();
                dialogLoading.dismiss();
                dialogPersonalized.defaultDialog(
                        getString(R.string.title_input_invalid, "Cadastro"),
                        getString(R.string.error_exception)).show();
            }
        });
    }

}
