package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goal.R;
import com.example.goal.controllers.InputErrors;
import com.example.goal.controllers.ManagerKeyboard;
import com.example.goal.models.Address;
import com.example.goal.models.User;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterForPurchases extends AppCompatActivity {

    private LinearLayout layout_typeData;
    private LinearLayout layout_stateCity;
    private MaterialCardView card_dataPersonal, card_dataAddress;

    private TextInputLayout layoutEdit_cpf;
    private TextInputLayout layoutEdit_cnpj;
    private TextInputLayout layoutEdit_country;
    private TextInputLayout layoutEdit_state;
    private TextInputLayout layoutEdit_city;
    private TextInputLayout layoutEdit_cep;
    private TextInputLayout layoutEdit_exState;
    private TextInputLayout layoutEdit_exCity;

    private TextInputEditText edit_cpf;
    private TextInputEditText edit_cnpj;
    private TextInputEditText edit_phone;
    private TextInputEditText edit_cep;
    private TextInputEditText edit_address;
    private TextInputEditText edit_district;
    private TextInputEditText edit_number;
    private TextInputEditText edit_complement;
    private TextInputEditText edit_exState;
    private TextInputEditText edit_exCity;

    private TextView error_document;

    private AutoCompleteTextView autoComplete_country;
    private AutoCompleteTextView autoComplete_state;
    private AutoCompleteTextView autoComplete_city;

    private Button registerCompleteUser;
    private RadioButton rdBtn_cpf, rdBtn_cnpj;

    private String[] array_countries, array_state, array_city;
    private ManagerKeyboard managerKeyboard;
    private InputErrors inputErrors;
    private User userRegister;
    private Address addressRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_purchases);

        // Instancia os Itens
        instanceItems();

        // Configura as Opções CPF e CNPJ
        setUpDocument();
        listenerDocument();

        // Botão que Pula essa Etapa do Cadastro e Abre a Tela Incial
        Button skip_stage = findViewById(R.id.btn_skipStage);
        skip_stage.setOnClickListener(v -> {
            managerKeyboard.closeKeyboard(this);
            startActivity(new Intent(this, IndexActivity.class));
            // todo: testar se irá fechar as activities da pilha
            finishAffinity();
        });

        // Configura o Dropdown dos Países (Ao selecionar uma Opção, Configura o Dropdown do Estado)
        setUpDropdownCountry();
        listenerCountries();

        // Listener do Botão "Finalizar Cadastro"
        completeRegister();
    }

    // Instancia os Itens (ID, Classes...)
    private void instanceItems() {
        managerKeyboard = new ManagerKeyboard(getApplicationContext());
        inputErrors = new InputErrors(this);
        addressRegister = new Address(this);
        userRegister = new User(this);

        error_document = findViewById(R.id.error_document);
        layout_typeData = findViewById(R.id.layout_typeData);
        layout_stateCity = findViewById(R.id.layout_stateCity);
        card_dataPersonal = findViewById(R.id.card_doccument);
        card_dataAddress = findViewById(R.id.card_address);

        edit_cpf = findViewById(R.id.edit_cpf);
        edit_cnpj = findViewById(R.id.edit_cnpj);
        edit_phone = findViewById(R.id.edit_phone);
        edit_cep = findViewById(R.id.edit_cep);
        edit_address = findViewById(R.id.edit_address);
        edit_district = findViewById(R.id.edit_district);
        edit_number = findViewById(R.id.edit_number);
        edit_complement = findViewById(R.id.edit_complement);

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

    // Configura os Textos nos Checkbox do CPF/CNPj
    private void setUpDocument() {
        String[] optionsDocs = getResources().getStringArray(R.array.cpf_cnpj);
        rdBtn_cpf.setText(optionsDocs[0]);
        rdBtn_cnpj.setText(optionsDocs[1]);
    }

    // Clique nas Opções dos RadioButton (CPF, CNPJ)
    private void listenerDocument() {
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

    // Configura o Dropdown dos Países
    private void setUpDropdownCountry() {
        array_countries = getResources().getStringArray(R.array.pays);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, array_countries);

        autoComplete_country.setAdapter(adapter);
    }

    // Configura o Dropdown dos Estados
    private void setUpDropdownState() {
        array_state = getResources().getStringArray(R.array.state);

        ArrayAdapter<String> adapterState = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, array_state);
        autoComplete_state.setAdapter(adapterState);

        listenerStates();
    }

    // Configura o Dropdown das Cidades
    private void setUpDropdownCity() {
        // Todo: substituir pelas cidades recebidas da API
        array_city = getResources().getStringArray(R.array.state);

        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, array_city);
        autoComplete_city.setAdapter(adapterCity);

        listenerCity();
    }

    // Listener do Dropdown dos Paises
    private void listenerCountries() {
        autoComplete_country.setOnItemClickListener((parent, view, position, id) -> {
            // Obtem o Valor selecionado
            addressRegister.setCountry(array_countries[position]);
            layout_stateCity.setVisibility(View.VISIBLE);

            // Configura os Inputs diferentes para Extrangeiros e Brasileiros
            if (addressRegister.getCountry().equals("Estrangeiro")) {
                visibilityInputs(true);
            } else {
                setUpDropdownState();
                layoutEdit_state.setEnabled(true);
                visibilityInputs(false);
            }
        });
    }

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

    // Listener do Dropdown dos Estados
    private void listenerStates() {
        autoComplete_state.setOnItemClickListener((parent, view, position, id) -> {
            // Obtem o Valor selecionado e Configura o DropDown City
            addressRegister.setState(array_state[position]);
            setUpDropdownCity();
            layoutEdit_city.setEnabled(true);
        });
    }

    // Listener dos Inputs das Cidades
    private void listenerCity() {
        autoComplete_city.setOnItemClickListener((parent, view, position, id) -> {
            // Obtem o Valor selecionado e Configura o EditText CEP
            addressRegister.setCity(array_city[position]);
            layoutEdit_cep.setEnabled(true);
        });
    }

    // Validação dos Documentos (CPF ou CNPJ)
    private boolean validationPersonalInfos() {
        User user = new User(this);

        if (rdBtn_cpf.isChecked()) {
            // Obtem os Dados do CPF para Validação
            user.setCpf(Objects.requireNonNull(edit_cpf.getText()).toString());
            if (!user.validationCpf(user.getCpf())) {
                inputErrors.errorInputWithoutIcon(edit_cpf, user.getError_validation());
                card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        } else if (rdBtn_cnpj.isChecked()) {
            // Obtem os dados do CNPJ para Validar
            user.setCnpj(Objects.requireNonNull(edit_cnpj.getText()).toString());
            if (!user.validationCnpj(user.getCnpj())) {
                inputErrors.errorInputWithoutIcon(edit_cnpj, user.getError_validation());
                card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            }
        } else {
            // Nenhuma opção foi Selecionada
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            error_document.setVisibility(View.VISIBLE);
            return false;
        }

        user.setPhone(Objects.requireNonNull(edit_phone.getText()).toString());
        if (!user.validationPhone(user.getPhone())) {
            inputErrors.errorInputWithoutIcon(edit_phone, user.getError_validation());
            card_dataPersonal.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        }

        // Remove o Erro e Define o valor no Usuario
        if (rdBtn_cnpj.isChecked()) {
            error_document.setVisibility(View.GONE);
            user.setCnpj(edit_cnpj.getText().toString());
        } else {
            error_document.setVisibility(View.GONE);
            user.setCpf(edit_cpf.getText().toString());
        }
        userRegister.setPhone(edit_phone.getText().toString());
        card_dataPersonal.setStrokeColor(getResources().getColor(R.color.lime_green));
        return true;
    }

    // Valida as Informações da Localização (País, Estado, Cidade)
    private boolean validationLocale() {
        Address address = new Address(this);
        address.setCountry(addressRegister.getCountry());

        // Valida as Opções de país (Brasil X Estrangeiro)
        if (!address.validationCountry(address.getCountry())) {
            inputErrors.errorInputLayout(layoutEdit_country, address.getError_validation());
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
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
                inputErrors.errorInputWithoutIcon(edit_exState, address.getError_validation());
            else inputErrors.errorInputLayout(layoutEdit_state, address.getError_validation());
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            // Remove o Erro do InputLayout dos Estados Brasileiros
            if (!isForeign) layoutEdit_state.setErrorEnabled(false);
        }

        if (!address.validationCity(address)) {
            if (isForeign)
                inputErrors.errorInputWithoutIcon(edit_exCity, address.getError_validation());
            else inputErrors.errorInputLayout(layoutEdit_city, address.getError_validation());

            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            // Remove o Erro do InputLayout das Cidades Brasileiras
            if (!isForeign) layoutEdit_city.setErrorEnabled(false);
        }

        // Finalização das Validações e Atribuição dos Valores a classe Global addressRegister
        if (!isForeign) {
            // Validação do CEP (somente para Brasileiros)
            address.setCep(Objects.requireNonNull(edit_cep.getText()).toString());
            if (!address.validationCEP(address)) {
                inputErrors.errorInputWithoutIcon(edit_cep, address.getError_validation());
                card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
                return false;
            } else addressRegister.setCep(edit_cep.getText().toString());

        } else {
            // Define o Estado e Cidade do Estrangeiro
            addressRegister.setState(address.getState());
            addressRegister.setCity(address.getCity());
        }

        // Passou por todas as dados da Validação
        card_dataAddress.setStrokeColor(getResources().getColor(R.color.lime_green));
        return true;
    }

    // Valida as Informações do Endereço (Endereço, Bairro, Numero, Complemento)
    private boolean validationAddress() {
        // Obtem os Valores
        Address address = new Address(this);
        address.setAddress(Objects.requireNonNull(edit_address.getText()).toString());
        address.setDistrict(Objects.requireNonNull(edit_district.getText()).toString());
        address.setComplement(Objects.requireNonNull(edit_complement.getText()).toString());

        // Evita erros ao Convertes EmptyString em Int
        String number = Objects.requireNonNull(edit_number.getText()).toString();
        address.setNumber(number.equals("") ? 0 : Integer.parseInt(number));

        if (!address.validationAddress(address.getAddress())) {
            inputErrors.errorInputWithoutIcon(edit_address, address.getError_validation());
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!address.validationDistrict(address.getDistrict())) {
            inputErrors.errorInputWithoutIcon(edit_district, address.getError_validation());
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!address.validationNumber(address.getNumber())) {
            inputErrors.errorInputWithoutIcon(edit_number, address.getError_validation());
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else if (!address.validationComplement(address.getComplement())) {
            inputErrors.errorInputWithoutIcon(edit_complement, address.getError_validation());
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.ruby_red));
            return false;
        } else {
            card_dataAddress.setStrokeColor(getResources().getColor(R.color.lime_green));
            addressRegister.setAddress(edit_address.getText().toString());
            addressRegister.setDistrict(edit_district.getText().toString());
            addressRegister.setComplement(edit_complement.getText().toString());
            addressRegister.setNumber(Integer.parseInt(edit_number.getText().toString()));
            return true;
        }
    }

    // Botão "Finalizar Cadastro" ---> Realiza o Cadastro, caso passe pelas validações
    private void completeRegister() {
        registerCompleteUser.setOnClickListener(v -> {
            if (validationPersonalInfos() && validationLocale() && validationAddress()) {
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
                finish();
            } else {
                // Validações Não Validas ou Erro no Cadastro
                SnackBarPersonalized snackBar = new SnackBarPersonalized(findViewById(R.id.layout_purchases));
                snackBar.makeDefaultSnackBar(R.string.error_singup).show();
            }
        });
    }

}