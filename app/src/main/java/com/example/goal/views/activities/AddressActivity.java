package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerResources.EXCEPTION;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
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
    private ManagerInputErrors managerInputErrors;
    private Address addressRegister;

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
        managerInputErrors = new ManagerInputErrors(context);
        dialog_personalized = new AlertDialogPersonalized(context);
        addressRegister = new Address(context);

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

        // Executa uma tarefa assincrona
        Executors.newSingleThreadExecutor().execute(() -> {
            runOnUiThread(progressDialog::show);
            String[] array_cities_async = addressRegister.getCities(uf);
            runOnUiThread(() -> {
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
     * Valida as Informações da Localização (País, Estado, Cidade)
     *
     * @return boolean
     */
    private boolean validationTerritory() {
        Address address = new Address(context);

        // Configura o País e Realiza as Validações
        address.setCountry(address.getCountryForBoolean(isForeign));
        if (isForeign) {
            // Obtem os Valores dos InputText dos Estrangeiros
            address.setState(Objects.requireNonNull(edit_exState.getText()).toString());
            address.setCity(Objects.requireNonNull(edit_exCity.getText()).toString());
            if (!address.validationState(address)) {
                managerInputErrors.errorInputEditText(edit_exState, address.getError_validation(), false);
                card_dataTerritory.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else if (!address.validationCity(address)) {
                managerInputErrors.errorInputEditText(edit_exCity, address.getError_validation(), false);
                card_dataTerritory.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        } else {
            // Remove o Erro dos Layouts (se Hourver) e Obtem os Valores do AutoCompleteText
            layoutEdit_state.setErrorEnabled(false);
            layoutEdit_city.setErrorEnabled(false);
            address.setState(addressRegister.getState());
            address.setCity(addressRegister.getCity());

            // Valida o Endereço
            if (!address.validationState(address)) {
                managerInputErrors.errorInputLayout(layoutEdit_state, address.getError_validation());
                card_dataTerritory.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else if (!address.validationCity(address)) {
                managerInputErrors.errorInputLayout(layoutEdit_city, address.getError_validation());
                card_dataTerritory.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        }

        // Passou por todas as dados da Validação
        addressRegister.setCountry(address.getCountry());
        addressRegister.setState(address.getState());
        addressRegister.setCity(address.getCity());
        card_dataTerritory.setStrokeColor(getResources().getColor(R.color.lime_green));
        return true;
    }

    /**
     * Valida as Informações do Endereço (Endereço, Bairro, Numero, Complemento)
     *
     * @return boolean
     */
    private boolean validationAddress() {
        // Obtem os Valores
        Address address = new Address(context);
        address.setAddress(Objects.requireNonNull(edit_address.getText()).toString());
        address.setDistrict(Objects.requireNonNull(edit_district.getText()).toString());

        if (!address.validationAddress(address.getAddress())) {
            managerInputErrors.errorInputEditText(edit_address, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!address.validationDistrict(address.getDistrict())) {
            managerInputErrors.errorInputEditText(edit_district, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            addressRegister.setAddress(address.getAddress());
            addressRegister.setDistrict(address.getDistrict());
        }

        // Caso o CEP Esteja disponivel (País = Brasil)
        if (layoutEdit_cep.getVisibility() == View.VISIBLE) {

            // Validação do CEP (somente para Brasileiros). É necessario Endereço, UF, e Bairro
            address.setCep(Objects.requireNonNull(edit_cep.getText()).toString());
            address.setState(address.getUF(addressRegister.getState()));
            if (!address.validationCEP(address.getMaskedCep())) {
                managerInputErrors.errorInputEditText(edit_cep, address.getError_validation(), false);
                card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else if (!address.checkCEP(address)) {
                dialog_personalized.defaultDialog(
                        getString(R.string.title_input_invalid, "CEP"),
                        address.getError_validation()).show();
                managerInputErrors.errorInputEditText(edit_cep, address.getError_validation(), false);
                card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else addressRegister.setCep(address.getUnmaskCep());
        }

        // Evita erros ao Convertes Empty String em Int
        try {
            String number = Objects.requireNonNull(edit_number.getText()).toString();
            address.setNumber(number.equals("") ? 0 : Integer.parseInt(number));
        } catch (Exception ex) {
            String NAME_CLASS = "RegisterForPurchases";
            Log.e(EXCEPTION, NAME_CLASS + " - Erro ao Converter o Numero do Endereço");
            ex.printStackTrace();
        }
        address.setComplement(Objects.requireNonNull(edit_complement.getText()).toString());

        // Validação do Numero e Complemento
        if (!address.validationNumber(address.getNumber())) {
            managerInputErrors.errorInputEditText(edit_number, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!address.validationComplement(address.getComplement())) {
            managerInputErrors.errorInputEditText(edit_complement, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            addressRegister.setNumber(address.getNumber());
            addressRegister.setComplement(address.getComplement());
        }

        // Todos os Dados Validados
        card_dataAddress.setStrokeColor(getResources().getColor(R.color.lime_green));
        return true;
    }

    /**
     * Botão "Finalizar Cadastro". Caso passe por todas as Validações, Realiza o Cadastro
     */
    private void registerAddress() {
        // Instancia e Obtem o Listener do Botão
        Button registerCompleteUser = findViewById(R.id.btn_registerCompleteUser);
        registerCompleteUser.setOnClickListener(v -> {
            if (!managerServices.availableInternet()) {
                dialog_personalized.defaultDialog(getString(R.string.title_no_internet),
                        Html.fromHtml(getString(R.string.error_network)).toString()).show();
            } else if (validationTerritory() && validationAddress()) {
                managerServices.closeKeyboard(this);

                AddressAPI addressAPI = new AddressAPI(AddressActivity.this);
                if (addressAPI.insertAddress(addressRegister)) {

                    // Insere o Endereço no Banco de Dados
                    ManagerDataBase managerDataBase = new ManagerDataBase(AddressActivity.this);
                    if (!managerDataBase.insertAddress(addressRegister)) {
                        dialog_personalized.defaultDialog(getString(R.string.title_no_register_api),
                                managerDataBase.getError_operation()).show();
                    } else {
                        // Abre a pagina Index (Produtos) e Finaliza a Actvity
                        startActivity(new Intent(context, IndexActivity.class));
                        finishAffinity();
                    }

                } else {
                    // Erro na API Goal
                    dialog_personalized.defaultDialog(getString(R.string.title_no_register_api),
                            addressAPI.getError_operation()).show();
                }
            } else {
                // Validações Não Validas ou Erro no Cadastro
                new SnackBarPersonalized(findViewById(R.id.layout_purchases)).defaultSnackBar(
                        getString(R.string.error_singup)).show();
            }
        });
    }

}