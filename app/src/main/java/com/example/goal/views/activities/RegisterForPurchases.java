package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.goal.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterForPurchases extends AppCompatActivity {

    private TextInputEditText edit_cpf;
    private TextInputEditText edit_cnpj;

    private TextInputEditText edit_phone;
    private TextInputEditText edit_cep;
    private TextInputEditText edit_address;
    private TextInputEditText edit_district;
    private TextInputEditText edit_number;
    private TextInputEditText edit_complement;

    private TextInputLayout layout_cpf;
    private TextInputLayout layout_cnpj;
    private Spinner spinnerCountry;
    private Spinner spinnerState;
    private Button next_stage;
    private Button createCompleteAcount;
    private RadioButton rdbtn_cpf, rdbtn_cnpj;
    private LinearLayout layout_typeData;

    private boolean isCpf = false;
    private String[] countriesOptions;
    private String country;
    private String[] stateOptions;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_purchases);

        recoveryIds();

        Resources resources = getResources();
        countriesOptions = resources.getStringArray(R.array.pays);
        String[] optionsDocs = resources.getStringArray(R.array.cpf_cnpj);
        stateOptions = resources.getStringArray(R.array.state);


        // Btn que Pula essa Etapa do Cadastro e Abre a Tela Incial
        next_stage.setOnClickListener(v ->{
            startActivity(new Intent(this, IndexActivity.class));
            finish();
        });

        // Configurações das Opções e listeners
        rdbtn_cpf.setText(optionsDocs[0]);
        rdbtn_cnpj.setText(optionsDocs[1]);

        // Configurações dos Spinners
        setupSpinnerCountry();
        setupSpinnerState();

        listenerRadioButtons();

        createCompleteAcount.setOnClickListener(v -> registerForPurchases());
    }

    // Recupera os Ids dos Itens
    private void recoveryIds() {

        layout_typeData = findViewById(R.id.layout_typeData);
        layout_cpf= findViewById(R.id.layoutedit_cpf);
        layout_cnpj = findViewById(R.id.layoutedit_cnpj);

        edit_cpf = findViewById(R.id.edit_cpf);
        edit_cnpj = findViewById(R.id.edit_cnpj);
        edit_phone = findViewById(R.id.edit_phone);
        edit_cep = findViewById(R.id.edit_cep);
        edit_address = findViewById(R.id.edit_address);
        edit_district = findViewById(R.id.edit_district);
        edit_number = findViewById(R.id.edit_number);
        edit_complement = findViewById(R.id.edit_complement);

        spinnerCountry = findViewById(R.id.spinner_countries);
        spinnerState = findViewById(R.id.spinner_state);

        next_stage = findViewById(R.id.btn_nextStage);
        createCompleteAcount = findViewById(R.id.btn_completeAcount);
        rdbtn_cpf = findViewById(R.id.rbtn_cpf);
        rdbtn_cnpj = findViewById(R.id.rbtn_cnpj);
    }

    // Realiza o Cadastro, caso passe pelas validações
    private void registerForPurchases() {
        // TODO IMPLEMENTAR MEDOTOS DE VALIDAÇÕES

        // Abre a pagina Index (Produtos) e Finaliza a Actvity
        startActivity(new Intent(this, IndexActivity.class));
        finish();
    }

    private void setupSpinnerState() {
        ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(this,
                R.array.state, android.R.layout.simple_spinner_item);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapterState);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = stateOptions[position];
                System.out.println("Estado Selecionado: " + state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupSpinnerCountry() {
        ArrayAdapter<CharSequence> adapterCountry = ArrayAdapter.createFromResource(this,
                R.array.pays, android.R.layout.simple_spinner_item);

        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapterCountry);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = countriesOptions[position];
                System.out.println("País Selecionado: " + country);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        });

        // Caso clique no CNPJ, esconde os campos do CPF e mostra os do CNPJ
        rdbtn_cnpj.setOnClickListener(v -> {
            layout_typeData.setVisibility(View.VISIBLE);
            isCpf = false;
            layout_cnpj.setVisibility(View.VISIBLE);
            layout_cpf.setVisibility(View.GONE);
        });
    }

}