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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.goal.R;
import com.example.goal.controllers.InputErrors;
import com.example.goal.controllers.ManagerKeyboard;
import com.example.goal.models.Address;
import com.example.goal.models.User;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterForPurchases extends AppCompatActivity {

    private LinearLayout layout_typeData;
    private LinearLayout layout_stateCity;
    private ConstraintLayout personal_info;
    private ConstraintLayout address_info;

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

    private Button skip_stage;
    private Button registerCompleteUser;
    private Button next_stageRegister;
    private Button back_stageRegister;
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
        skip_stage.setOnClickListener(v -> {
            managerKeyboard.closeKeyboard(this);
            startActivity(new Intent(this, IndexActivity.class));
            // todo: testar se irá fechar as activities da pilha
            finishAffinity();
        });

        // Configura os Botões Inferiores "Voltar" e "Proximo"
        listenerNextStage();
        listenerBackStage();

        // Configura o Dropdown dos Países (Ao selecionar uma Opção, Configura o Dropdown do Estado)
        setUpDropdownCountry();
        listenerCountries();

        registerCompleteUser.setOnClickListener(v -> completeRegister());
    }

    // Instancia os Itens (ID, Classes...)
    private void instanceItems() {
        managerKeyboard = new ManagerKeyboard(getApplicationContext());
        inputErrors = new InputErrors(this);
        addressRegister = new Address(this);
        userRegister = new User();

        error_document = findViewById(R.id.error_document);
        layout_typeData = findViewById(R.id.layout_typeData);
        layout_stateCity = findViewById(R.id.layout_stateCity);
        personal_info = findViewById(R.id.layout_dataPersonal);
        address_info = findViewById(R.id.layout_address);

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

        skip_stage = findViewById(R.id.btn_skipStage);
        registerCompleteUser = findViewById(R.id.btn_registerCompleteUser);
        next_stageRegister = findViewById(R.id.button_nextStagePurchases);
        back_stageRegister = findViewById(R.id.button_backStagePurchases);
        rdBtn_cpf = findViewById(R.id.rbtn_cpf);
        rdBtn_cnpj = findViewById(R.id.rbtn_cnpj);
    }

    // Configura o Checkbox do CPF/CNPj
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

    // Botão Inferior que Avança no Cadastro CPF + Endereço
    private void listenerNextStage() {
        next_stageRegister.setOnClickListener(v -> {
            if (validationDocument() && validationPhone()) {
                back_stageRegister.setVisibility(View.VISIBLE);
                next_stageRegister.setVisibility(View.INVISIBLE);
                managerKeyboard.closeKeyboard(this);
                personal_info.setVisibility(View.GONE);
                address_info.setVisibility(View.VISIBLE);
            }
        });
    }

    // Botão Inferior que Volta no Cadastro CPF + Endereço
    private void listenerBackStage() {
        back_stageRegister.setOnClickListener(v -> {
            next_stageRegister.setVisibility(View.VISIBLE);
            back_stageRegister.setVisibility(View.INVISIBLE);
            managerKeyboard.closeKeyboard(this);
            personal_info.setVisibility(View.VISIBLE);
            address_info.setVisibility(View.GONE);
        });
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

        int visibilityBrazilian = showForeign ? View.GONE : View.VISIBLE;
        int visibilityForeign = showForeign ? View.VISIBLE : View.GONE;

        // IFs para evitar a repetição da ação.
        // Caso a visibilidade já está de acordo com o tipo de 'Paìs', não altera
        if (layoutEdit_city.getVisibility() != visibilityBrazilian
                || layoutEdit_state.getVisibility() != visibilityBrazilian) {
            layoutEdit_city.setVisibility(visibilityBrazilian);
            layoutEdit_state.setVisibility(visibilityBrazilian);
            layoutEdit_cep.setVisibility(visibilityBrazilian);
        }

        if (layoutEdit_exState.getVisibility() != visibilityForeign
                || layoutEdit_exCity.getVisibility() != visibilityForeign
                || layoutEdit_cep.getVisibility() != visibilityForeign) {
            layoutEdit_exState.setVisibility(visibilityForeign);
            layoutEdit_exCity.setVisibility(visibilityForeign);
        }
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
    private boolean validationDocument() {
        User user = new User();

        // Valdiação CPF e CNPJ
        if (rdBtn_cpf.isChecked()) {
            error_document.setVisibility(View.GONE);

            user.setCpf(Objects.requireNonNull(edit_cpf.getText()).toString());
            String validationCpf = user.validationCpf(user.getCpf());

            if (!validationCpf.equals(User.OK)) {
                inputErrors.errorInputEditText(edit_cpf, validationCpf);
                return false;
            } else {
                user.setCpf(edit_cpf.getText().toString());
                return true;
            }

        } else if (rdBtn_cnpj.isChecked()) {
            error_document.setVisibility(View.GONE);

            user.setCnpj(Objects.requireNonNull(edit_cnpj.getText()).toString());
            String validationCnpj = user.validationCnpj(user.getCnpj());

            if (!validationCnpj.equals(User.OK)) {
                inputErrors.errorInputEditText(edit_cpf, validationCnpj);
                return false;
            } else {
                user.setCnpj(edit_cnpj.getText().toString());
                return true;
            }

        } else {
            error_document.setVisibility(View.VISIBLE);
            return false;
        }
    }

    // Valida o Numero de Telefone
    private boolean validationPhone() {
        User user = new User();

        user.setPhone(Objects.requireNonNull(edit_phone.getText()).toString());
        String validationPhone = user.validationPhone(user.getPhone());

        if (!validationPhone.equals(User.OK)) {
            inputErrors.errorInputEditText(edit_phone, validationPhone);
            return false;
        } else {
            userRegister.setPhone(edit_phone.getText().toString());
            return true;
        }
    }

    // Valida as Informações da Localização (País, Estado, Cidade)
    private boolean validationLocale() {
        Address address = new Address(this);

        address.setCountry(autoComplete_country.getText().toString());
        address.setState(autoComplete_state.getText().toString());
        address.setCity(autoComplete_city.getText().toString());
        address.setCep(Objects.requireNonNull(edit_cep.getText()).toString());

        String validationCountry = address.validationCountry(address.getCountry());
        String validationState = address.validationState(address);
        String validationCity = address.validationCity(address);
        String validationCep = address.validationCEP(address);

        boolean isForeign;

        if (!validationCountry.equals(User.OK)) {
            inputErrors.errorInputLayout(layoutEdit_country, validationCountry);
            return false;
        }

        // Verifica se o País selecionado é = Brasil
        isForeign = address.getCountry().equals("Estrangeiro");

        // Validação dos dados diferentes entre Brasileiros e Estrangeiros
        if (!validationState.equals(User.OK)) {
            if (isForeign) inputErrors.errorInputEditText(edit_exState, validationState);
            inputErrors.errorInputLayout(layoutEdit_state, validationState);
            return false;
        } else if (!validationCity.equals(User.OK)) {
            if (isForeign) inputErrors.errorInputEditText(edit_exCity, validationCity);
            inputErrors.errorInputLayout(layoutEdit_city, validationCity);
            return false;
        }

        // Validação do CEP somente para Brasileiros
        if (!isForeign) {
            if (!validationCep.equals(User.OK)) {
                inputErrors.errorInputEditText(edit_cep, validationCep);
                return false;
            } else {
                addressRegister.setCep(edit_cep.getText().toString());
            }
        }

        // Passou por todas as dados da Validação
        addressRegister.setCountry(autoComplete_country.getText().toString());
        addressRegister.setState(autoComplete_state.getText().toString());
        addressRegister.setCity(autoComplete_city.getText().toString());
        return true;
    }

    // Valida as Informações do Endereço (Endereço, Bairro, Numero, Complemento)
    private boolean validationAddress() {
        Address address = new Address(this);

        address.setAddress(Objects.requireNonNull(edit_address.getText()).toString());
        address.setDistrict(Objects.requireNonNull(edit_district.getText()).toString());
        address.setComplement(Objects.requireNonNull(edit_complement.getText()).toString());

        // Evita erros ao Convertes EmptyString em Int
        String number = Objects.requireNonNull(edit_number.getText()).toString();
        address.setNumber(number.equals("") ? 0 : Integer.parseInt(number));

        String validationAddress = address.validationAddress(address.getAddress());
        String validationDistrict = address.validationDistrict(address.getDistrict());
        String validationNumber = address.validationNumber(address.getNumber());
        String validationComplement = address.validationComplement(address.getComplement());

        if (!validationAddress.equals(User.OK)) {
            inputErrors.errorInputEditText(edit_address, validationAddress);
            return false;
        } else if (!validationDistrict.equals(User.OK)) {
            inputErrors.errorInputEditText(edit_district, validationDistrict);
            return false;
        } else if (!validationNumber.equals(User.OK)) {
            inputErrors.errorInputEditText(edit_number, validationNumber);
            return false;
        } else if (!validationComplement.equals(User.OK)) {
            inputErrors.errorInputEditText(edit_complement, validationComplement);
            return false;
        } else {
            addressRegister.setAddress(edit_address.getText().toString());
            addressRegister.setDistrict(edit_district.getText().toString());
            addressRegister.setComplement(edit_complement.getText().toString());
            addressRegister.setNumber(Integer.parseInt(edit_number.getText().toString()));
            return true;
        }
    }

    // Botão "Finalizar Cadastro" ---> Realiza o Cadastro, caso passe pelas validações
    private void completeRegister() {

        if (validationDocument() && validationPhone() && validationLocale() && validationAddress()) {
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
            SnackBarPersonalized snackBar =
                    new SnackBarPersonalized(findViewById(R.id.layout_purchases));
            snackBar.makeDefaultSnackBar(R.string.error_singup).show();
        }
    }

}