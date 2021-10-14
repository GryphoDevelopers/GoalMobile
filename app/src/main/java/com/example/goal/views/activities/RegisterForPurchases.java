package com.example.goal.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.managers.ManagerInputErrors;
import com.example.goal.managers.ManagerKeyboard;
import com.example.goal.managers.ManagerServices;
import com.example.goal.managers.SearchInternet;
import com.example.goal.models.Address;
import com.example.goal.models.SerializationInfos;
import com.example.goal.models.User;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe RegisterForPurchases: Activity para realizar o Registro Completo do Usuario
 * (Documentos e Endereço)
 */
public class RegisterForPurchases extends AppCompatActivity {

    private String[] array_countries, array_state, array_city, array_uf;
    private Context context;
    private LinearLayout layout_typeData, layout_stateCity;
    private ScrollView scrollView;

    private TextView error_document;
    private MaterialCardView card_dataPersonal, card_dataAddress, card_dataTerritory;

    private TextInputLayout layoutEdit_cpf, layoutEdit_cnpj, layoutEdit_country, layoutEdit_state,
            layoutEdit_city, layoutEdit_cep, layoutEdit_exState, layoutEdit_exCity;
    private TextInputEditText edit_cpf, edit_cnpj, edit_phone, edit_cep, edit_address, edit_district,
            edit_number, edit_complement, edit_exState, edit_exCity;
    private AutoCompleteTextView autoComplete_country, autoComplete_state, autoComplete_city;

    private Button registerCompleteUser;
    private RadioButton rdBtn_cpf, rdBtn_cnpj;

    private ManagerKeyboard managerKeyboard;
    private ManagerInputErrors managerInputErrors;
    private User userRegister;
    private Address addressRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_purchases);

        // Instancia os Itens
        instanceItems();

        // Configura as Opções e Listener CPF e CNPJ
        setUpDocument();

        // Botão que Pula essa Etapa do Cadastro e Abre a Tela Incial
        Button skip_stage = findViewById(R.id.btn_skipStage);
        skip_stage.setOnClickListener(v -> {
            managerKeyboard.closeKeyboard(this);
            startActivity(new Intent(this, IndexActivity.class));
            finishAffinity();
        });

        // Configura o Dropdown dos Países (Ao selecionar uma Opção, Configura o Dropdown do Estado)
        setUpDropdownCountry();

        // Listener do Botão "Finalizar Cadastro"
        completeRegister();
    }

    /**
     * Instancia os Itens (ID, Classes...)
     */
    private void instanceItems() {
        context = RegisterForPurchases.this;
        managerKeyboard = new ManagerKeyboard(context);
        managerInputErrors = new ManagerInputErrors(this);
        addressRegister = new Address(this);
        userRegister = new User(this);

        error_document = findViewById(R.id.error_document);
        layout_typeData = findViewById(R.id.layout_typeData);
        layout_stateCity = findViewById(R.id.layout_stateCity);
        card_dataPersonal = findViewById(R.id.card_doccument);
        card_dataAddress = findViewById(R.id.card_address);
        card_dataTerritory = findViewById(R.id.card_territory);

        edit_cpf = findViewById(R.id.edit_cpf);
        edit_cnpj = findViewById(R.id.edit_cnpj);
        edit_phone = findViewById(R.id.edit_phone);
        edit_cep = findViewById(R.id.edit_cep);
        edit_address = findViewById(R.id.edit_address);
        edit_district = findViewById(R.id.edit_district);
        edit_number = findViewById(R.id.edit_number);
        edit_complement = findViewById(R.id.edit_complement);

        scrollView = findViewById(R.id.scrollView_purchases);
        layoutEdit_cpf = findViewById(R.id.layoutEdit_cpf);
        layoutEdit_cnpj = findViewById(R.id.layoutEdit_cnpj);
        layoutEdit_country = findViewById(R.id.layoutEdit_countries);
        layoutEdit_state = findViewById(R.id.layoutEdit_states);
        layoutEdit_city = findViewById(R.id.layoutEdit_city);
        layoutEdit_cep = findViewById(R.id.layoutEdit_cep);

        edit_exState = findViewById(R.id.edit_exState);
        edit_exCity = findViewById(R.id.edit_exCity);
        layoutEdit_exState = findViewById(R.id.layoutEdit_exState);
        layoutEdit_exCity = findViewById(R.id.layoutEdit_exCity);
        autoComplete_state = findViewById(R.id.autoCompleteState);
        autoComplete_country = findViewById(R.id.autoCompleteCountry);
        autoComplete_city = findViewById(R.id.autoCompleteCity);

        registerCompleteUser = findViewById(R.id.btn_registerCompleteUser);
        rdBtn_cpf = findViewById(R.id.rbtn_cpf);
        rdBtn_cnpj = findViewById(R.id.rbtn_cnpj);
    }

    /**
     * Configura os Textos nos RadioButtons do CPF/CNPJ e os Listeners (Cliques) deles
     */
    private void setUpDocument() {
        String[] optionsDocs = getResources().getStringArray(R.array.cpf_cnpj);
        rdBtn_cpf.setText(optionsDocs[0]);
        rdBtn_cnpj.setText(optionsDocs[1]);

        // Caso clique no CPF, esconde os campos do CNPJ e mostra os do CPF
        rdBtn_cpf.setOnClickListener(v -> {
            layout_typeData.setVisibility(View.VISIBLE);
            layoutEdit_cpf.setVisibility(View.VISIBLE);
            layoutEdit_cnpj.setVisibility(View.GONE);
            error_document.setVisibility(View.GONE);
        });

        // Caso clique no CNPJ, esconde os campos do CPF e mostra os do CNPJ
        rdBtn_cnpj.setOnClickListener(v -> {
            layout_typeData.setVisibility(View.VISIBLE);
            layoutEdit_cnpj.setVisibility(View.VISIBLE);
            layoutEdit_cpf.setVisibility(View.GONE);
            error_document.setVisibility(View.GONE);
        });
    }

    /**
     * Configura o Dropdown dos Países
     */
    private void setUpDropdownCountry() {
        array_countries = getResources().getStringArray(R.array.pays);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, array_countries);

        autoComplete_country.setAdapter(adapter);

        // Listener do AutoComplete dos Países
        autoComplete_country.setOnItemClickListener((parent, view, position, id) -> {
            // Obtem o Valor selecionado
            addressRegister.setCountry(array_countries[position]);
            layoutEdit_country.setErrorEnabled(false);
            layout_stateCity.setVisibility(View.VISIBLE);

            // Configura os Inputs diferentes para Extrangeiros e Brasileiros
            if (addressRegister.getCountry().equals("Estrangeiro")) {
                visibilityInputs(true);
            } else {
                // Verifica se a Internet está disponivel
                if (!internetNotAvailable()) {
                    setUpDropdownState();
                    layoutEdit_state.setEnabled(true);
                    visibilityInputs(false);
                }
            }
        });
    }

    /**
     * Configura o Dropdown dos Estados
     */
    private void setUpDropdownState() {
        array_uf = getResources().getStringArray(R.array.uf);
        array_state = getResources().getStringArray(R.array.state);

        ArrayAdapter<String> adapterState = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, array_state);
        autoComplete_state.setAdapter(adapterState);

        // Listener do AutoCompleteText dos Estados
        autoComplete_state.setOnItemClickListener((parent, view, position, id) -> {
            // Obtem o Valor do Estado e UF respectivamente para configurar o AutoCompleCity
            addressRegister.setState(array_state[position]);
            setUpDropdownCity(array_uf[position]);
            layoutEdit_city.setEnabled(true);
        });
    }

    /**
     * Configura o Dropdown das Cidades
     */
    private void setUpDropdownCity(String country_uf) {
        // Instancia a Classe de AlertDialog que seão usados
        AlertDialogPersonalized dialog_personalized = new AlertDialogPersonalized(context);

        // Inicia um AlertDialog
        AlertDialog progressDialog = dialog_personalized.loadingDialog();
        progressDialog.show();

        // Atividade Background: Busca na API WEB
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            // Formação da URI
            Uri build_uriAPI = Uri.parse(SearchInternet.API_BRAZIL_CITY).buildUpon().
                    appendPath(country_uf).build();

            // Obtenção do JSON
            SearchInternet searchInternet = new SearchInternet(this);
            String json_cities = searchInternet.SearchInAPI(build_uriAPI.toString(), "GET");
            if (json_cities == null) {
                // Exibe o Erro da API na Tela
                runOnUiThread(() -> dialog_personalized.defaultDialog(
                        getString(R.string.title_error_api), searchInternet.getError_search())
                        .show());
            } else {
                runOnUiThread(() -> {
                    // Serialização do JSON
                    SerializationInfos serializationInfos = new SerializationInfos(this);
                    array_city = serializationInfos.serializationCities(json_cities);

                    if (array_city == null) {
                        // Exibe o Erro da Serialização na Tela
                        runOnUiThread(() -> dialog_personalized.
                                defaultDialog(getString(R.string.title_error_api),
                                        serializationInfos.getError_operation()).show());
                    } else {
                        runOnUiThread(() -> {
                            // Configura o AutoCompleteCity
                            ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this,
                                    android.R.layout.simple_dropdown_item_1line, array_city);
                            autoComplete_city.setAdapter(adapterCity);
                        });
                    }
                });
            }
            // Encerra o Circular Progress Indicator
            runOnUiThread(progressDialog::dismiss);
        });

        // Listener do AutoComplete das Cidades
        autoComplete_city.setOnItemClickListener((parent, view, position, id) -> {
            // Obtem o Valor selecionado e Configura o EditText CEP
            addressRegister.setCity(array_city[position]);
            layoutEdit_cep.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Configura quais Inputs vai exibir de acordo com a Nacionalidade (Brasileiro X Estrangeiro
     */
    private void visibilityInputs(boolean showForeign) {
        // Configura a Visibilidade dos Inputs
        int visibilityBrazilian = showForeign ? View.GONE : View.VISIBLE;
        int visibilityForeign = showForeign ? View.VISIBLE : View.GONE;

        layoutEdit_city.setVisibility(visibilityBrazilian);
        layoutEdit_state.setVisibility(visibilityBrazilian);
        layoutEdit_cep.setVisibility(visibilityBrazilian);
        layoutEdit_exState.setVisibility(visibilityForeign);
        layoutEdit_exCity.setVisibility(visibilityForeign);
    }

    /**
     * Validação dos Dados Pessoais do Usuario (Documentos (CPF ou CNPJ) e Telefone)
     */
    private boolean validationPersonalInfos() {
        User user = new User(this);

        if (rdBtn_cpf.isChecked()) {
            // Obtem os Dados do CPF para Validação
            user.setCpf(Objects.requireNonNull(edit_cpf.getText()).toString());
            if (!user.validationCpf(user.getCpf())) {
                managerInputErrors.errorInputEditText(edit_cpf, user.getError_validation(), false);
                card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        } else if (rdBtn_cnpj.isChecked()) {
            // Obtem os dados do CNPJ para Validar
            user.setCnpj(Objects.requireNonNull(edit_cnpj.getText()).toString());

            if (!user.validationCnpj(user.getCnpj())) {
                managerInputErrors.errorInputEditText(edit_cnpj, user.getError_validation(), false);
                card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else if (!user.validationNumberCnpj(user.getCnpj())) {
                // Mostra os Possiveris erros do CNPJ
                new AlertDialogPersonalized(context).defaultDialog(
                        getString(R.string.title_input_invalid, "CNPJ"),
                        Html.fromHtml(getString(R.string.error_cnpj)).toString()).show();
                managerInputErrors.errorInputEditText(edit_cnpj, user.getError_validation(), false);
                card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        } else {
            // Nenhuma opção foi Selecionada
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            error_document.setVisibility(View.VISIBLE);
            return false;
        }

        user.setPhone(Objects.requireNonNull(edit_phone.getText()).toString());
        if (!user.validationPhone(user.getPhone())) {
            managerInputErrors.errorInputEditText(edit_phone, user.getError_validation(), false);
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        }

        // Passou por todas as Vlidações
        if (rdBtn_cnpj.isChecked()) userRegister.setCnpj(user.getCnpj());
        else userRegister.setCpf(user.getCpf());
        userRegister.setPhone(user.getPhone());
        card_dataPersonal.setStrokeColor(getResources().getColor(R.color.lime_green));
        return true;
    }

    /**
     * Valida as Informações da Localização (País, Estado, Cidade)
     *
     * @return boolean
     */
    private boolean validationTerritory() {
        Address address = new Address(this);
        address.setCountry(addressRegister.getCountry());

        // Valida as Opções de país (Brasil X Estrangeiro)
        if (!address.validationCountry(address.getCountry())) {
            managerInputErrors.errorInputLayout(layoutEdit_country, address.getError_validation());
            card_dataTerritory.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else layoutEdit_country.setErrorEnabled(false);

        // Verifica o país para obter os Dados
        boolean isForeign = address.getCountry().equals("Estrangeiro");
        if (isForeign) {
            // Obtem os Valores dos InputText dos Estrangeiros
            address.setState(Objects.requireNonNull(edit_exState.getText()).toString());
            address.setCity(Objects.requireNonNull(edit_exCity.getText()).toString());
        } else {
            // Obtem os Valores dos AutoCompleteText dos Brasileiros
            address.setState(addressRegister.getState());
            address.setCity(addressRegister.getCity());
        }

        // Valida o Estado e Cidade
        if (!address.validationState(address)) {
            if (isForeign)
                managerInputErrors.errorInputEditText(edit_exState, address.getError_validation(), false);
            else
                managerInputErrors.errorInputLayout(layoutEdit_state, address.getError_validation());
            card_dataTerritory.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            // Remove o Erro do InputLayout dos Estados Brasileiros
            if (!isForeign) layoutEdit_state.setErrorEnabled(false);
        }

        if (!address.validationCity(address)) {
            if (isForeign)
                managerInputErrors.errorInputEditText(edit_exCity, address.getError_validation(), false);
            else
                managerInputErrors.errorInputLayout(layoutEdit_city, address.getError_validation());

            card_dataTerritory.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            // Remove o Erro do InputLayout das Cidades Brasileiras
            if (!isForeign) layoutEdit_city.setErrorEnabled(false);
        }

        // Passou por todas as dados da Validação
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
        Address address = new Address(this);
        address.setAddress(Objects.requireNonNull(edit_address.getText()).toString());
        address.setDistrict(Objects.requireNonNull(edit_district.getText()).toString());
        address.setCep(Objects.requireNonNull(edit_cep.getText()).toString());
        address.setComplement(Objects.requireNonNull(edit_complement.getText()).toString());

        // Evita erros ao Convertes EmptyString em Int
        try {
            String number = Objects.requireNonNull(edit_number.getText()).toString();
            address.setNumber(number.equals("") ? 0 : Integer.parseInt(number));
        } catch (NumberFormatException ex) {
            String EXCEPTION_CONVERT = "EX_CONVERT";
            String NAME_CLASS = "RegisterForPurchases";
            Log.e(EXCEPTION_CONVERT, NAME_CLASS + " - Erro ao Converter o Numero do Endereço");
            ex.printStackTrace();
        }

        if (!address.validationAddress(address.getAddress())) {
            managerInputErrors.errorInputEditText(edit_address, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!address.validationDistrict(address.getDistrict())) {
            managerInputErrors.errorInputEditText(edit_district, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!address.validationNumber(address.getNumber())) {
            managerInputErrors.errorInputEditText(edit_number, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        }

        // Caso o CEP Esteja disponivel (País = Brasil)
        if (layoutEdit_cep.getVisibility() == View.VISIBLE) {
            // Validação do CEP (somente para Brasileiros)
            address.setCep(Objects.requireNonNull(edit_cep.getText()).toString());
            if (!address.validationCEP(address.getCep())) {
                managerInputErrors.errorInputEditText(edit_cep, address.getError_validation(), false);
                card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else addressRegister.setCep(edit_cep.getText().toString());
        }

        // Validação do Complemento
        if (!address.validationComplement(address.getComplement())) {
            managerInputErrors.errorInputEditText(edit_complement, address.getError_validation(), false);
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            // Todos os Dados Validados
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.lime_green));
            addressRegister.setAddress(address.getAddress());
            addressRegister.setDistrict(address.getDistrict());
            addressRegister.setComplement(address.getComplement());
            addressRegister.setNumber(address.getNumber());
            return true;
        }
    }

    /**
     * Botão "Finalizar Cadastro". Caso passe por todas as Validações, Realiza o Cadastro
     */
    private void completeRegister() {
        registerCompleteUser.setOnClickListener(v -> {
            if (internetNotAvailable()) return;
            else if (validationPersonalInfos() && validationTerritory() && validationAddress()) {
                managerKeyboard.closeKeyboard(this);

                // TODO RETIRAR e Implementar Cadastro POST API
                Log.e("PURCHASES", addressRegister.getCountry() + "\n" +
                        addressRegister.getState() + "\n" + addressRegister.getCity() + "\n" +
                        addressRegister.getAddress() + "\n" + addressRegister.getDistrict() + "\n" +
                        addressRegister.getNumber() + "\n" + addressRegister.getCep() + "\n" +
                        addressRegister.getComplement() + "\n" + userRegister.getCpf() + "\n" +
                        userRegister.getCnpj() + "\n" + userRegister.getPhone());

                // Abre a pagina Index (Produtos) e Finaliza a Actvity
                startActivity(new Intent(this, IndexActivity.class));
                finishAffinity();
            } else {
                // Validações Não Validas ou Erro no Cadastro
                new SnackBarPersonalized(findViewById(R.id.layout_purchases))
                        .defaultSnackBar(getString(R.string.error_singup))
                        .show();
            }
        });
    }

    /**
     * Verifica se não possui conexão de internet. Se não existir mostra o Erro na Tela.
     *
     * @return true/false
     */
    private boolean internetNotAvailable() {
        if (new ManagerServices(RegisterForPurchases.this).validationInternet()) return false;

        new AlertDialogPersonalized(RegisterForPurchases.this).defaultDialog(
                getString(R.string.title_no_internet),
                Html.fromHtml(getString(R.string.error_network)).toString()).show();
        return true;
    }

}