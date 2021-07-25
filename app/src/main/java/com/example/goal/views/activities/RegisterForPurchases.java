package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.goal.R;
import com.example.goal.controller.ManagerKeyboard;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterForPurchases extends AppCompatActivity {

    private LinearLayout layout_typeData;
    private ConstraintLayout layout_purchases;
    private ConstraintLayout personal_info;
    private ConstraintLayout address_info;

    private TextInputLayout layout_cpf;
    private TextInputLayout layout_cnpj;
    private TextInputLayout edit_country;
    private TextInputLayout edit_state;

    private TextView error_documment;
    private TextView progress_stages;

    private TextInputEditText edit_cpf;
    private TextInputEditText edit_cnpj;
    private TextInputEditText edit_phone;
    private TextInputEditText edit_cep;
    private TextInputEditText edit_address;
    private TextInputEditText edit_district;
    private TextInputEditText edit_number;
    private TextInputEditText edit_complement;

    private AutoCompleteTextView autoCompleteCountry;
    private AutoCompleteTextView autoCompleteState;

    private Button skip_stage;
    private Button createCompleteAcount;
    private Button next_stageRegister;
    private Button back_stageRegister;
    private RadioButton rdbtn_cpf, rdbtn_cnpj;

    private ManagerKeyboard managerKeyboard;
    private boolean isCpf = false;
    private String[] array_countries, array_state;
    private String country, state, address, district, complement, cpf, cnpj, phone;
    int number = 0, cep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_purchases);

        managerKeyboard = new ManagerKeyboard(getApplicationContext());

        // Recupera os Id dos Itens da Tela
        recoveryIds();

        Resources resources = getResources();
        array_countries = resources.getStringArray(R.array.pays);
        String[] optionsDocs = resources.getStringArray(R.array.cpf_cnpj);
        
        array_state = resources.getStringArray(R.array.state);
        progress_stages.setText(getString(R.string.status_page,1,2));

        // Botão que Pula essa Etapa do Cadastro e Abre a Tela Incial
        skip_stage.setOnClickListener(v ->{
            managerKeyboard.closeKeyboard(this);
            startActivity(new Intent(this, IndexActivity.class));
            finish();
        });

        // Configurações das Opções e listeners
        rdbtn_cpf.setText(optionsDocs[0]);
        rdbtn_cnpj.setText(optionsDocs[1]);

        setupInputsLists();
        
        listenersInputsLists();
        listenerRadioButtons();
        listenersStages();
        createCompleteAcount.setOnClickListener(v -> listenerCompleteAcount());
    }

    // Recupera os Ids dos Itens
    private void recoveryIds() {

        error_documment = findViewById(R.id.error_document);
        progress_stages = findViewById(R.id.txt_statusProgressPurchases);

        layout_purchases = findViewById(R.id.layout_purchases);
        layout_typeData = findViewById(R.id.layout_typeData);
        layout_cpf= findViewById(R.id.layoutedit_cpf);
        layout_cnpj = findViewById(R.id.layoutedit_cnpj);
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
        edit_country = findViewById(R.id.edit_countries);
        edit_state = findViewById(R.id.edit_states);

        autoCompleteState = findViewById(R.id.autoCompleteState);
        autoCompleteCountry = findViewById(R.id.autoCompleteCountry);

        skip_stage = findViewById(R.id.btn_skipStage);
        createCompleteAcount = findViewById(R.id.btn_completeAcount);
        next_stageRegister = findViewById(R.id.button_nextStagePurchases);
        back_stageRegister = findViewById(R.id.button_backStagePurchases);
        rdbtn_cpf = findViewById(R.id.rbtn_cpf);
        rdbtn_cnpj = findViewById(R.id.rbtn_cnpj);
    }

    // Botões Avançar e Recuar do Cadstro
    private void listenersStages() {
        next_stageRegister.setOnClickListener( v-> {
            if (validationPersonalInfo()){
                back_stageRegister.setVisibility(View.VISIBLE);
                next_stageRegister.setVisibility(View.INVISIBLE);
                managerKeyboard.closeKeyboard(this);
                personal_info.setVisibility(View.GONE);
                address_info.setVisibility(View.VISIBLE);
                progress_stages.setText(getString(R.string.status_page,2,2));
            }
        });

        back_stageRegister.setOnClickListener( v-> {
            next_stageRegister.setVisibility(View.VISIBLE);
            back_stageRegister.setVisibility(View.INVISIBLE);
            managerKeyboard.closeKeyboard(this);
            personal_info.setVisibility(View.VISIBLE);
            address_info.setVisibility(View.GONE);
            progress_stages.setText(getString(R.string.status_page,1,2));
        });
    }

    // Botão "Finalizar Cadastro" ---> Realiza o Cadastro, caso passe pelas validações
    private void listenerCompleteAcount() {

        if (validationPersonalInfo()){
            if (filledInputsAddress()){
                managerKeyboard.closeKeyboard(this);

                // TODO RETIRAR
                Log.e("PURCHASES", country + "\n" + state + "\n" +  address + "\n" + 
                        district + "\n" +  complement + "\n" +  cpf + "\n" +  cnpj + "\n" + phone
                        + "\n" + number + "\n" + cep);

                // Abre a pagina Index (Produtos) e Finaliza a Actvity
                startActivity(new Intent(this, IndexActivity.class));
                finish();
            }
            
        } else {
            // Erro no Cadastro
            SnackBarPersonalized snackBar = new SnackBarPersonalized(
                    findViewById(R.id.layout_purchases));
            snackBar.makeDefaultSnackBar(R.string.error_singup).show();
        }
    }

    // Configura o Input dos Países e Estados
    private void setupInputsLists(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, array_countries);
        autoCompleteCountry.setAdapter(adapter);

        ArrayAdapter<String> adapterState = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, array_state);
        autoCompleteState.setAdapter(adapterState);
    }

    // Listener dos Inputs dos Países e Estados
    private void listenersInputsLists(){
        autoCompleteCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = array_countries[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                country = "";
            }
        });

        autoCompleteState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = array_state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                state = "";
            }
        });
    }

    // Clique nas Opções dos RadioButton
    private void listenerRadioButtons() {
        // Caso clique no CPF, esconde os campos do CNPJ e mostra os do CPF
        rdbtn_cpf.setOnClickListener(v -> {
            isCpf = true;
            layout_typeData.setVisibility(View.VISIBLE);
            layout_cnpj.setVisibility(View.GONE);
            layout_cpf.setVisibility(View.VISIBLE);
            error_documment.setVisibility(View.GONE);
        });

        // Caso clique no CNPJ, esconde os campos do CPF e mostra os do CNPJ
        rdbtn_cnpj.setOnClickListener(v -> {
            layout_typeData.setVisibility(View.VISIBLE);
            isCpf = false;
            layout_cnpj.setVisibility(View.VISIBLE);
            layout_cpf.setVisibility(View.GONE);
            error_documment.setVisibility(View.GONE);
        });
    }

    // Valida a 1° parte do Cadastro
    private boolean validationPersonalInfo(){
        phone = edit_phone.getText().toString();

        if (!filledInputDocument()){
            error_documment.setVisibility(View.VISIBLE);
            return false;
        } else if (phone.equals("")){
            errorInput(edit_phone);
            return false;
        } else {
            error_documment.setVisibility(View.GONE);
            return true;
        }
    }

    // Valida se algum dos 2 documentos foram preenchidos
    private boolean filledInputDocument() {

        if (rdbtn_cpf.isChecked()) {
            cpf = edit_cpf.getText().toString();

            if (cpf.equals("")){
                errorInput(edit_cpf);
                return false;
            } else {
                return true;
            }
        } else if (rdbtn_cnpj.isChecked()) {
            cnpj = edit_cnpj.getText().toString();

            if (cnpj.equals("")){
                errorInput(edit_cnpj);
                return false;
            } else {
                return true;
            }
        } else{
            // Nenhum Botão Selecionado
            return false;
        }
    }
    
    // Valida se os Campos do Endereço foi Preenchidos
    private boolean filledInputsAddress() {

        country = autoCompleteCountry.getText().toString();
        state = autoCompleteState.getText().toString();
        address = edit_address.getText().toString();
        district = edit_district.getText().toString();
        complement = edit_complement.getText().toString();

        if (country.equals("")){
            edit_country.setError(getString(R.string.errorInputs));
            edit_country.requestFocus();
            managerKeyboard.openKeyboard(edit_country);
            return false;
        } else if (state.equals("")){
            edit_state.setError(getString(R.string.errorInputs));
            edit_state.requestFocus();
            managerKeyboard.openKeyboard(edit_state);
            return false;
        } else if (edit_cep.getText().toString().equals("")){
            errorInput(edit_cep);
            return false;
        } else if (address.equals("")){
            errorInput(edit_address);
            return false;
        } else if (district.equals("")){
            errorInput(edit_district);
            return false;
        } else if (edit_number.getText().toString().equals("")){
            errorInput(edit_number);
            return false;
        } else if (complement.equals("")){
            errorInput(edit_complement);
            return false;
        }  else {
            cep = Integer.parseInt(edit_cep.getText().toString());
            number = Integer.parseInt(edit_number.getText().toString());
            return true;
        }
    }

    private void errorInput(TextInputEditText inputEditText){
        inputEditText.setError(getString(R.string.errorInputs));
        inputEditText.requestFocus();
        managerKeyboard.openKeyboard(inputEditText);
    }

}