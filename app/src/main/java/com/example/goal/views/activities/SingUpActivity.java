package com.example.goal.views.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.goal.R;
import com.example.goal.controller.ManagerKeyboard;
import com.example.goal.models.HandleSharedPreferences;
import com.example.goal.views.SnackBarPersonalized;
import com.google.android.material.textfield.TextInputEditText;

public class SingUpActivity extends AppCompatActivity {

    private TextView errorOptionUser;
    private TextView errorTermsUse;
    private TextView progress_page;

    private TextInputEditText editName;
    private TextInputEditText editNickname;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirmPassword;

    private RadioButton opClient, opSeller;
    private CheckBox checkTermsUse;
    private Button createAcount;
    private Button see_termsUse;
    private Button next_stage;
    private Button back_stage;

    private ConstraintLayout layout_personal, layout_login, layout_terms;

    private ManagerKeyboard managerKeyboard;
    private String name, nickname, email, password, confirmPassword, optionUser;
    private int position = 1;

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        recoveryIds();
        managerKeyboard = new ManagerKeyboard(getApplicationContext());

        //Recupera os valores colocados no Array (dentro do strings.xml)
        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);

        //Coloca os valores do Array nas respectivas opções
        opSeller.setText(optionsUsers[0]);
        opClient.setText(optionsUsers[1]);

        // Configura o Texto das Etapas
        progress_page.setText(getString(R.string.status_page, 1, 3));

        listenersButtonsStages();

        see_termsUse.setOnClickListener(v ->{
            // TODO IMPLEMENTAR LEITURA DO TERMO DE USO ---> NOVA ACTIVITY});
        });

        createAcount.setOnClickListener(v -> listenerSingUp());
    }

    private void recoveryIds(){
        layout_personal = findViewById(R.id.layout_dataPersonal);
        layout_login = findViewById(R.id.layout_dataLogin);
        layout_terms = findViewById(R.id.layout_termsUse);

        editName = findViewById(R.id.edittext_name);
        editNickname = findViewById(R.id.edittext_nickname);
        editEmail = findViewById(R.id.edittext_email);
        editPassword = findViewById(R.id.edittext_password);
        editConfirmPassword = findViewById(R.id.edittext_confirmPassword);

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);
        progress_page = findViewById(R.id.txt_statusProgress);

        next_stage = findViewById(R.id.button_nextSingUp);
        back_stage = findViewById(R.id.button_backStage);
        createAcount = findViewById(R.id.button_createAcount);
        see_termsUse = findViewById(R.id.see_termsUse);
        checkTermsUse = findViewById(R.id.cbx_termsUse);
        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);
    }

    // Listener dos Botões Inferiores
    private void listenersButtonsStages() {

        // Listener do Botão "Proxima Etapa"
        next_stage.setOnClickListener(v -> {

            if (position == 1 && validationPersonalInfo()){
                // Mostra o Proximo Layout, Preenche o Circulo e muda o valor da variavel
                layout_personal.setVisibility(View.GONE);
                layout_login.setVisibility(View.VISIBLE);
                back_stage.setVisibility(View.VISIBLE);
                position = 2;
            } else if(position == 2 && validationLoginInfo()){
                // Abre a ultima parte do 1° Cadastro
                layout_login.setVisibility(View.GONE);
                layout_terms.setVisibility(View.VISIBLE);
                next_stage.setVisibility(View.INVISIBLE);
                position = 3;
            }

            managerKeyboard.closeKeyboard(this);
            progress_page.setText(getString(R.string.status_page, position, 3));

        });

        // Listener do Botão "Voltar"
        back_stage.setOnClickListener(v -> {

            if(position == 2){
                // Volta p/ os Dados Pessoais
                back_stage.setVisibility(View.INVISIBLE);
                layout_login.setVisibility(View.GONE);
                layout_personal.setVisibility(View.VISIBLE);
                position = 1;
            } else if(position == 3){
                // Volta p/ o Login
                layout_terms.setVisibility(View.GONE);
                next_stage.setVisibility(View.VISIBLE);
                layout_login.setVisibility(View.VISIBLE);
                position = 2;
            }

            managerKeyboard.closeKeyboard(this);
            progress_page.setText(getString(R.string.status_page, position, 3));
        });
    }

    // Validação do Nome, Nickname e Tipo do Usuario
    public boolean validationPersonalInfo(){
        name = editName.getText().toString();
        nickname = editNickname.getText().toString();

         if(name.equals("")){
             errorInput(editName);
            return false;
        } else if(nickname.equals("")){
             errorInput(editNickname);
            return false;
        } else if (!validationTypeUser()) {
             errorOptionUser.setVisibility(View.VISIBLE);
            return false;
        } else {
             errorOptionUser.setVisibility(View.GONE);
            return true;
        }
    }

    // Validação do Tipo de Usuario
    public boolean validationTypeUser(){
        if(opClient.isChecked()){
            optionUser="Cliente";
            return true;
        } else if (opSeller.isChecked()){
            optionUser="Vendedor";
            return true;
        } else return false;
    }

    // Validação do Email e Senha
    public boolean validationLoginInfo(){

        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        confirmPassword = editConfirmPassword.getText().toString();

        if (email.equals("")){
            errorInput(editEmail);
            return false;
        }  else if (password.equals("")){
            editPassword.setError(getString(R.string.errorInputs), null);
            editPassword.requestFocus();
            managerKeyboard.openKeyboard(editPassword);
            return false;
        }  else if(confirmPassword.equals("")){
            editConfirmPassword.setError(getString(R.string.errorInputs), null);
            editConfirmPassword.requestFocus();
            managerKeyboard.openKeyboard(editConfirmPassword);
            return false;
        } else {
            return true;
        }
    }

    // Valida do Termo de Uso
    public boolean validationTermsUse(){
        if(checkTermsUse.isChecked()) {
            errorTermsUse.setVisibility(View.GONE);
            return true;
        }  else {
            errorTermsUse.setVisibility(View.VISIBLE);
            return false;
        }
    }

    // Cadastra o Usuario
    public void listenerSingUp() {
        if (validationTermsUse()){
            // Valida Novamente as Etapas de Cadastros
            if (validationPersonalInfo() && validationLoginInfo()){

                // Todas as validações estão corretas ---> Fim do Cadastro
                HandleSharedPreferences preferences = new HandleSharedPreferences(
                        getSharedPreferences(PREFERENCE_LOGIN, 0));

                // Define TRUE para login Realizado
                preferences.setLogin(true);

                // TODO RETIRAR
                Log.e("SING UP", "Nome: " + name + "\nEmail: " + email + "\nNickname:" +
                        nickname + "\nSenha: " + password + "\nConfirmar Senha: " +
                        confirmPassword + "\nOpção Cliente: " + opClient.isChecked() +
                        "\nOpção Vendedor: " + optionUser + "\nTermos de Uso: " +
                        checkTermsUse.isChecked());

                // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
                startActivity(new Intent(this, RegisterForPurchases.class));
                finish();
            } else {
                // Erro no Cadastro
                SnackBarPersonalized snackBar = new SnackBarPersonalized(findViewById(R.id.layouts_register));
                snackBar.makeDefaultSnackBar(R.string.error_singup).show();
            }
        }
    }

    private void errorInput(TextInputEditText inputEditText){
        inputEditText.setError(getString(R.string.errorInputs));
        inputEditText.requestFocus();
        managerKeyboard.openKeyboard(inputEditText);
    }

}