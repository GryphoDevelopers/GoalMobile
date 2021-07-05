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

public class RegisterForPurchases extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    TextView txtCpf;
    TextView txtCnpj;
    EditText editCpf;
    EditText editCnpj;
    private Button next_stage;
    private RadioButton cpf, cnpj;
    private Spinner spinner;


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
        spinner = findViewById(R.id.spinner_countries);

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pays, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




    }

    //Metodo para pular o Cadastro Completo
    private void skipStep(View view){
        Intent intentIndex = new Intent(this, IndexActivity.class);
        startActivity(intentIndex);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_countries:
                country = countriesOptions[position];
                break;
            case R.id.spinner_state:
                state = stateOptions[position];
                break;
            default:
                country = "";
                state = "";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}