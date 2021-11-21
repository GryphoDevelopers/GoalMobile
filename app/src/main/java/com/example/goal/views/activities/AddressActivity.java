package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerResources.EXCEPTION;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerServices;
import com.example.goal.models.Address;
import com.example.goal.models.api.AddressAPI;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.MaskInputPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe RegisterForPurchases: Activity para realizar o Registro Completo do Usuario
 * (Documentos e Endereço)
 */
public class AddressActivity extends AppCompatActivity {

    private boolean isForeign = false;
    private Context context;
    private MaterialCardView card_dataAddress, card_dataTerritory;
    private TextInputLayout layoutEdit_state;
    private TextInputLayout layoutEdit_city;
    private TextInputLayout layoutEdit_cep;
    private TextInputLayout layoutEdit_exState;
    private TextInputLayout layoutEdit_exCity;
    private TextInputEditText edit_cep;
    private TextInputEditText edit_address;
    private TextInputEditText edit_district;
    private TextInputEditText edit_number;
    private TextInputEditText edit_complement;
    private TextInputEditText edit_exState;
    private TextInputEditText edit_exCity;
    private ImageButton btn_help;
    private String[] array_state, array_cities;
    private ManagerServices managerServices;
    private AlertDialogPersonalized dialog_personalized;
    private Address addressRegister;
    private Handler handlerMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        // Instancia os Itens
        instanceItems();

        // Botão que Pula essa Etapa do Cadastro e Abre a Tela Incial
        Button skip_stage = findViewById(R.id.btn_skipStage);
        skip_stage.setOnClickListener(v -> {
            managerServices.closeKeyboard(this);
            startActivity(new Intent(context, IndexActivity.class));
            finishAffinity();
        });

        // Configura o Switch do País Brasil X Estrangeiro
        setUpCountries();

        // Configura o Dropdown dos Estados (Ao selecionar uma Opção, Configura o Dropdown dos Municipios)
        setUpDropdownState();

        // Clique no Botão "?" das Cidades
        btn_help.setOnClickListener(v -> dialog_personalized.defaultDialog(getString(R.string.title_city),
                Html.fromHtml(getString(R.string.notice_city)).toString()).show());

        // Listener do Botão "Finalizar Cadastro"
        registerAddress();
    }

    /**
     * Instancia os Itens (ID, Classes...) que serão usados em diferentes metodos
     */
    private void instanceItems() {
        context = AddressActivity.this;
        managerServices = new ManagerServices(context);
        dialog_personalized = new AlertDialogPersonalized(context);
        addressRegister = new Address(context);

        // Exibirá os Resultados Assincronos sempre na Thread Principal
        handlerMain = new Handler(Looper.getMainLooper());

        card_dataAddress = findViewById(R.id.card_address);
        card_dataTerritory = findViewById(R.id.card_territory);

        edit_exState = findViewById(R.id.edit_exState);
        edit_exCity = findViewById(R.id.edit_exCity);

        // Insere a Mascara nos Inputs
        edit_cep = findViewById(R.id.edit_cep);
        edit_cep.addTextChangedListener(MaskInputPersonalized.managerMask(edit_cep, MaskInputPersonalized.MASK_CEP));

        edit_address = findViewById(R.id.edit_address);
        edit_district = findViewById(R.id.edit_district);
        edit_number = findViewById(R.id.edit_number);
        edit_complement = findViewById(R.id.edit_complement);

        layoutEdit_state = findViewById(R.id.layoutEdit_states);
        layoutEdit_city = findViewById(R.id.layoutEdit_city);
        layoutEdit_cep = findViewById(R.id.layoutEdit_cep);
        layoutEdit_exState = findViewById(R.id.layoutEdit_exState);
        layoutEdit_exCity = findViewById(R.id.layoutEdit_exCity);

        btn_help = findViewById(R.id.btn_help);
    }

    /**
     * Configura quais Inputs vai exibir de acordo com a Nacionalidade (Brasileiro X Estrangeiro)
     */
    private void setUpCountries() {
        SwitchMaterial switch_country = findViewById(R.id.switch_country);
        switch_country.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Define o novo Valor da Variavel que controla Brasileiros X Estrangeiros
            isForeign = isChecked;

            // Configura a Visibilidade dos Inputs
            int visibilityBrazilian = isForeign ? View.GONE : View.VISIBLE;
            int visibilityForeign = isForeign ? View.VISIBLE : View.GONE;

            btn_help.setVisibility(visibilityBrazilian);
            layoutEdit_city.setVisibility(visibilityBrazilian);
            layoutEdit_state.setVisibility(visibilityBrazilian);
            layoutEdit_cep.setVisibility(visibilityBrazilian);

            layoutEdit_exState.setVisibility(visibilityForeign);
            layoutEdit_exCity.setVisibility(visibilityForeign);
        });
    }

    /**
     * Configura o Dropdown dos Estados
     */
    private void setUpDropdownState() {
        AutoCompleteTextView autoComplete_state = findViewById(R.id.autoCompleteState);
        array_state = getResources().getStringArray(R.array.state);

        ArrayAdapter<String> adapterState = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, array_state);
        autoComplete_state.setAdapter(adapterState);

        // Listener do AutoCompleteText dos Estados
        autoComplete_state.setOnItemClickListener((parent, view, position, id) -> {
            // Obtem o Valor do Estado e UF respectivamente para configurar o AutoCompleCity
            String state = array_state[position];
            addressRegister.setState(state);
            setUpDropdownCities(addressRegister.getUF(state));
        });
        autoComplete_state.setOnClickListener(v -> managerServices.closeKeyboard(this));
    }

    /**
     * Configura o Dropdown das Cidades
     *
     * @param uf Unidade Federeativa do Estado para obter a lista de Cidades
     */
    private void setUpDropdownCities(String uf) {
        AutoCompleteTextView autoComplete_city = findViewById(R.id.autoCompleteCity);

        // Verifica se a Internet está disponivel
        if (!managerServices.availableInternet()) {
            dialog_personalized.defaultDialog(getString(R.string.title_no_internet),
                    Html.fromHtml(getString(R.string.error_network)).toString()).show();
        }

        // Instancia a Classe de AlertDialog que será usado
        AlertDialog progressDialog = dialog_personalized.loadingDialog(
                getString(R.string.message_loadingDownload, "das Cidades"), true);

        ExecutorService executorService = Executors.newCachedThreadPool();
        // Executa uma tarefa assincrona
        executorService.execute(() -> {
            handlerMain.post(progressDialog::show);
            String[] array_cities_async = addressRegister.getCities(executorService, uf);
            handlerMain.post(() -> {
                array_cities = array_cities_async;
                progressDialog.dismiss();

                if (array_cities != null) {
                    // Configura o AutoCompleteCity
                    ArrayAdapter<String> adapterCity = new ArrayAdapter<>(context,
                            android.R.layout.simple_dropdown_item_1line, array_cities);
                    autoComplete_city.setAdapter(adapterCity);
                    layoutEdit_city.setEnabled(true);

                    // Listener do AutoComplete das Cidades
                    autoComplete_city.setOnItemClickListener((parent, view, position, id) -> {
                        // Obtem o Valor selecionado e Configura o EditText CEP
                        addressRegister.setCity(array_cities[position]);
                        layoutEdit_cep.setVisibility(View.VISIBLE);
                    });
                } else {
                    layoutEdit_city.setEnabled(false);
                    dialog_personalized.defaultDialog(getString(R.string.title_input_invalid, "Estado"),
                            addressRegister.getError_validation()).show();
                }
            });
        });
        autoComplete_city.setOnClickListener(v -> managerServices.closeKeyboard(this));
    }

    /**
     * Valida as Informações da Localização de Endereços Estrangeiros (País, Estado, Cidade)
     *
     * @return {@link TextInputEditText}|null
     */
    private TextInputEditText validationTerritoryForeign() {
        // Configura o País e Realiza as Validações
        addressRegister.setCountry(addressRegister.getCountryForBoolean(isForeign));

        // Obtem os Valores dos InputText dos Estrangeiros
        addressRegister.setState(Objects.requireNonNull(edit_exState.getText()).toString());
        addressRegister.setCity(Objects.requireNonNull(edit_exCity.getText()).toString());
        if (!addressRegister.validationState(addressRegister)) return edit_exState;
        else if (!addressRegister.validationCity(addressRegister)) return edit_exCity;
        else return null;
    }

    /**
     * Valida as Informações da Localização de Brasileiros (País, Estado, Cidade)
     *
     * @return {@link TextInputEditText}|null
     */
    private TextInputLayout validationTerritoryBrazilian() {
        // Configura o País e Realiza as Validações
        addressRegister.setCountry(addressRegister.getCountryForBoolean(isForeign));
        // Valida o Endereço dos Brasileiros
        if (!addressRegister.validationState(addressRegister)) return layoutEdit_state;
        else if (!addressRegister.validationCity(addressRegister)) return layoutEdit_city;
        else return null;
    }

    /**
     * Valida as Informações da Verificação da do CEP
     * <p>
     * * Caso haja erro, na Posição 0 estará o Titulo, e na Posição 1 na Mensagem
     *
     * @return String[]|null
     */
    private String[] validationAPI(ExecutorService executorService) {
        if (!addressRegister.checkCEP(executorService, addressRegister)) {
            return new String[]{getString(R.string.title_input_invalid, "CEP"),
                    addressRegister.getError_validation()};
        }
        return null;
    }

    /**
     * Valida as Informações do Endereço (Endereço, Bairro, Numero, Complemento)
     *
     * @return {@link TextInputEditText}|null
     */
    private TextInputEditText validationAddress() {
        // Obtem os Valores dos Inputs
        addressRegister.setAddress(Objects.requireNonNull(edit_address.getText()).toString());
        addressRegister.setDistrict(Objects.requireNonNull(edit_district.getText()).toString());

        if (!addressRegister.validationAddress(addressRegister.getAddress()))
            return edit_address;
        else if (!addressRegister.validationDistrict(addressRegister.getDistrict()))
            return edit_district;

        // Valida o CEP caso esteja disponivel (País = Brasil)
        if (layoutEdit_cep.getVisibility() == View.VISIBLE) {
            // Validação do CEP (somente para Brasileiros). É necessario Endereço, UF, e Bairro
            addressRegister.setCep(Objects.requireNonNull(edit_cep.getText()).toString());
            addressRegister.setState(addressRegister.getUF(addressRegister.getState()));
            if (!addressRegister.validationCEP(addressRegister.getMaskedCep())) return edit_cep;
        }

        // Evita erros ao Convertes Empty String em Int
        try {
            String number = Objects.requireNonNull(edit_number.getText()).toString();
            addressRegister.setNumber(number.equals("") ? 0 : Integer.parseInt(number));
        } catch (Exception ex) {
            Log.e(EXCEPTION, "RegisterForPurchases - Erro ao Converter o Numero do Endereço");
            ex.printStackTrace();
        }
        addressRegister.setComplement(Objects.requireNonNull(edit_complement.getText()).toString());

        // Validação do Numero e Complemento
        if (!addressRegister.validationNumber(addressRegister.getNumber())) return edit_number;
        else if (!addressRegister.validationComplement(addressRegister.getComplement()))
            return edit_complement;
        else return null;
    }

    /**
     * Botão "Finalizar Cadastro". Caso passe por todas as Validações, Realiza o Cadastro
     */
    private void registerAddress() {
        // Instancia e Obtem o Listener do Botão
        Button registerCompleteUser = findViewById(R.id.btn_registerCompleteUser);
        registerCompleteUser.setOnClickListener(v -> {
            // Fecha o Teclado (Caso esteja aberto)
            managerServices.closeKeyboard(this);

            // Valida a Conexão com a Internet
            if (!managerServices.availableInternet()) {
                dialog_personalized.defaultDialog(
                        getString(R.string.title_no_internet),
                        Html.fromHtml(getString(R.string.error_network)).toString()).show();
                return;
            }

            // Cria um AlertDialog do Estilo "Carregando..."
            AlertDialog dialogLoading = dialog_personalized.loadingDialog(
                    getString(R.string.message_loadingSingUp, "Endereço"), false);

            // Executa as Validações e Cadatros em uma Tarefa Assincrona
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(() -> {

                handlerMain.post(dialogLoading::show);

                // Realiza a Validação dos Dados Inseridos
                TextInputEditText wrongForeign = isForeign ? validationTerritoryForeign() : null;
                TextInputLayout wrongBrazilian = isForeign ? null : validationTerritoryBrazilian();
                String[] wrongAPI = validationAPI(executorService);
                TextInputEditText wrongAddress = validationAddress();
                ManagerInputErrors inputErrors = new ManagerInputErrors(context);

                if (wrongForeign != null) {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        inputErrors.errorInputEditText(wrongForeign,
                                addressRegister.getError_validation(), false);
                    });
                    return;
                } else if (wrongBrazilian != null) {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        inputErrors.errorInputLayout(wrongBrazilian, addressRegister.getError_validation());
                    });
                    return;
                } else {
                    handlerMain.post(() -> {
                        card_dataTerritory.setStrokeColor(getResources().getColor(R.color.lime_green));
                        layoutEdit_state.setErrorEnabled(false);
                        layoutEdit_city.setErrorEnabled(false);
                    });
                }

                if (wrongAddress != null) {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        inputErrors.errorInputEditText(wrongAddress,
                                addressRegister.getError_validation(), false);
                    });
                    return;
                } else if (wrongAPI != null) {
                    handlerMain.post(() -> {
                        dialogLoading.dismiss();
                        dialog_personalized.defaultDialog(wrongAPI[0], wrongAPI[1]).show();
                    });
                    return;
                } else handlerMain.post(() -> card_dataAddress.setStrokeColor(getResources()
                        .getColor(R.color.lime_green)));


                // Tenta Inserir o Endereço na API (de forma Assincrona)
                AddressAPI addressAPI = new AddressAPI(context);
                if (!addressAPI.insertAddress(executorService, addressRegister)) {
                    handlerMain.post(() -> {
                        // Remove o Alert Dialog "Carregando" e exibe o Erro do Registro na API
                        dialogLoading.dismiss();
                        dialog_personalized.defaultDialog(getString(R.string.title_no_register_api),
                                addressAPI.getError_operation()).show();
                    });
                    return;
                }

                // Insere o Endereço no Banco de Dados Local (SQLite)
                ManagerDataBase managerDataBase = new ManagerDataBase(context);
                if (!managerDataBase.insertAddress(addressRegister)) {
                    handlerMain.post(() -> {
                        // Remove o Alert Dialog "Carregando" e exibe o Erro do Banco Local
                        dialogLoading.dismiss();
                        dialog_personalized.defaultDialog(
                                getString(R.string.title_no_register_api),
                                managerDataBase.getError_operation()).show();
                    });
                } else {
                    handlerMain.post(() -> {
                        // Abre a pagina Index (Produtos) e Finaliza a Actvity na Thread Principal
                        dialogLoading.dismiss();
                        startActivity(new Intent(context, IndexActivity.class));
                        finishAffinity();
                    });
                }
            });
        });
    }

}
