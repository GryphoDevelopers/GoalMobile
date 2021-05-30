package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SingInActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private EditText editConfirmEmail;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private RadioButton opClient, opSeller;
    private CheckBox checkTermsUse;
    private TextView errorOptionUser;
    private TextView errorTermsUse;

    private String name, email, confirmEmail, password, confirmPassword, optionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        editName = findViewById(R.id.editText_name);
        editEmail = findViewById(R.id.editText_email);
        editConfirmEmail = findViewById(R.id.editText_confrimEmail);
        editPassword = findViewById(R.id.editText_password);
        editConfirmPassword = findViewById(R.id.editText_confirmPassword);
        checkTermsUse = findViewById(R.id.cbx_termsUse);

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
    }

    //Valida as Informações da Primeira Etapa do Cadastro (Parte Obrigatoria(Nome, Email, ...))
    public void ValidationFirstStage(View view) {

        //Aciona os metodos de validação
        if(ValidationEditText() && ValidationTypeUser() && ValidationTermsUse()){

           System.out.println("Nome: " + name +
                   "\nEmail: " + email + "\nConfirmar Email: " + confirmEmail +
                   "\nSenha: "+ password + "\nConfirmar Senha: "+ confirmPassword +
                   "\nOpção Cliente: " + opClient.isChecked() + "\nOpção Vendedor: "+ optionUser +
                   "\nTermos de Uso: " + checkTermsUse.isChecked());

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




