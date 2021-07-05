package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.R;

public class SingUpActivity extends AppCompatActivity {


    private TextView errorOptionUser;
    private TextView errorTermsUse;
    private EditText editName;
    private EditText editEmail;
    private EditText editConfirmEmail;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private RadioButton opClient, opSeller;
    private CheckBox checkTermsUse;
    private Button createAcount;

    private String name, email, confirmEmail, password, confirmPassword, optionUser;

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        editName = findViewById(R.id.editText_name);
        editEmail = findViewById(R.id.editText_email);
        editConfirmEmail = findViewById(R.id.editText_confrimEmail);
        editPassword = findViewById(R.id.editText_password);
        editConfirmPassword = findViewById(R.id.editText_confirmPassword);
        checkTermsUse = findViewById(R.id.cbx_termsUse);
        createAcount = findViewById(R.id.btn_createAcount);

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);

        //Recupera os valores colocados no Array (dentro do strings.xml)
        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);

        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);

        //Coloca os valores do Array nas respectivas opções
        opSeller.setText(optionsUsers[0]);
        opClient.setText(optionsUsers[1]);

        createAcount.setOnClickListener(v -> validationSingUp(v));

    }

    //Valida as Informações
    public void validationSingUp(View view) {

        //Aciona os metodos de validação
        if(ValidationEditText() && ValidationTypeUser() && ValidationTermsUse()){
            // Validação retornando True

            HandleSharedPreferences preferences = new HandleSharedPreferences(
                    getSharedPreferences(PREFERENCE_LOGIN, 0));
            // Define TRUE para login Realizado
            preferences.setLogin();

           System.out.println("Nome: " + name +
                   "\nEmail: " + email + "\nConfirmar Email: " + confirmEmail +
                   "\nSenha: "+ password + "\nConfirmar Senha: "+ confirmPassword +
                   "\nOpção Cliente: " + opClient.isChecked() + "\nOpção Vendedor: "+ optionUser +
                   "\nTermos de Uso: " + checkTermsUse.isChecked());

           // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
            Intent completeSingUp = new Intent(this, RegisterForPurchases.class);
            startActivity(completeSingUp);
            finish();
        }
        else {
            System.out.println("Não foi Possivel Cadastrar o Usuario");
        }
    }


    //Valida os campos de EditText e apresenta o erro caso exista
    public boolean ValidationEditText(){
        name = editName.getText().toString();
        email = editEmail.getText().toString();
        confirmEmail = editConfirmEmail.getText().toString();
        password = editPassword.getText().toString();
        confirmPassword = editConfirmPassword.getText().toString();

        //TODO - É possivel adicionar um Icone apos a msg de erro
        if(name.equals("")){
            editName.setError("Campo Obrigatório.");
            return false;
        } else if (email.equals("")){
            editEmail.setError("Campo Obrigatório.");
            return false;
        } else if(confirmEmail.equals("")){
            editConfirmPassword.setError("Campo Obrigatório.");
            return false;
        } else if (password.equals("")){
            editPassword.setError("Campo Obrigatório.");
            return false;
        }  else if(confirmPassword.equals("")){
            editConfirmPassword.setError("Campo Obrigatório.");
            return false;
        } else {
            return true;
        }
    }


    //Valida o Termo de Uso e apresenta o erro caso exista
    public boolean ValidationTermsUse(){
        if(checkTermsUse.isChecked()) {
            errorTermsUse.setVisibility(View.GONE);
            return true;
        }  else {
            errorTermsUse.setVisibility(View.VISIBLE);
            return false;
        }
    }

    //Valida o Tipo de Usuario e apresenta o erro caso exista
    public boolean ValidationTypeUser(){
        if(!opClient.isChecked() && !opSeller.isChecked()){
            errorOptionUser.setVisibility(View.VISIBLE);
            return false;
        } else if(opClient.isChecked()){
            optionUser="Cliente";
            errorOptionUser.setVisibility(View.GONE);
            return true;
        } else {
            optionUser="Vendedor";
            errorOptionUser.setVisibility(View.GONE);
            return true;
        }
    }

}




