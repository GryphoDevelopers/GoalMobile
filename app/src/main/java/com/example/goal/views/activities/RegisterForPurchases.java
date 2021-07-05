package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.goal.R;

public class RegisterForPurchases extends AppCompatActivity {

    TextView txtCpf;
    TextView txtCnpj;
    EditText editCpf;
    EditText editCnpj;
    private Spinner spinnerCountry;
    private Spinner spinnerState;
    private Button next_stage;
    private Button createCompleteAcount;
    private RadioButton cpf, cnpj;


    private boolean isCpf = false;
    private String[] countriesOptions;
    private String country;
    private String[] stateOptions;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_purchases);

        Resources resources = getResources();
        countriesOptions = resources.getStringArray(R.array.pays);
        String[] optionsDocs = resources.getStringArray(R.array.cpf_cnpj);
        stateOptions = resources.getStringArray(R.array.state);

        next_stage = findViewById(R.id.btn_nextStage);
        cpf = findViewById(R.id.rbtn_cpf);
        cnpj = findViewById(R.id.rbtn_cnpj);
        txtCpf = findViewById(R.id.txt_cpf) ;
        txtCnpj = findViewById(R.id.txt_cnpj);
        editCpf = findViewById(R.id.edit_cpf);
        editCnpj = findViewById(R.id.edit_cnpj);
        spinnerCountry = findViewById(R.id.spinner_countries);
        spinnerState = findViewById(R.id.spinner_state);
        createCompleteAcount = findViewById(R.id.btn_completeAcount);

        next_stage.setOnClickListener(v -> skipStep(v));

        //Coloca o array CPF e CNPJ na Tela
        cpf.setText(optionsDocs[0]);
        cnpj.setText(optionsDocs[1]);


        // Caso clique no CPF, esconde os campos do CNPJ e mostra os do CPF
        cpf.setOnClickListener(v -> {
            isCpf = true;
            txtCnpj.setVisibility(View.GONE);
            editCnpj.setVisibility(View.GONE);
            txtCpf.setVisibility(View.VISIBLE);
            editCpf.setVisibility(View.VISIBLE);
        });

        // Caso clique no CNPJ, esconde os campos do CPF e mostra os do CNPJ
        cnpj.setOnClickListener(v -> {
            isCpf = false;
            txtCnpj.setVisibility(View.VISIBLE);
            editCnpj.setVisibility(View.VISIBLE);
            txtCpf.setVisibility(View.GONE);
            editCpf.setVisibility(View.GONE);
        });

        ArrayAdapter<CharSequence> adapterCountry = ArrayAdapter.createFromResource(this,
                R.array.pays, android.R.layout.simple_spinner_item);

        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapterCountry);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = countriesOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(this,
                R.array.state, android.R.layout.simple_spinner_item);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapterState);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = stateOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createCompleteAcount.setOnClickListener(v -> validationCompleteSingUp());

    }

    //Metodo para pular o Cadastro Completo
    private void skipStep(View view){
        Intent intentIndex = new Intent(this, IndexActivity.class);
        startActivity(intentIndex);
    }

    private void validationCompleteSingUp(){
        // TODO IMPLEMENTAR MEDOTOS DE VALIDAÇÕES

        // Abre a pagina Index (Produtos) e Finaliza a Actvity
        Intent indexProducts = new Intent(this, IndexActivity.class);
        startActivity(indexProducts);
        finish();
    }

}