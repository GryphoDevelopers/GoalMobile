package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private TextView errorSingUp;
    private EditText editName;
    private EditText editNickname;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private RadioButton opClient, opSeller;
    private CheckBox checkTermsUse;
    private Button createAcount;
    private Button see_termsUse;
    private Button next_stage;
    private Button back_stage;
    private View circle1;
    private View circle2;
    private View circle3;

    ConstraintLayout layout_personal, layout_login, layout_terms;

    private String name, nickname, email, password, confirmPassword, optionUser;
    private final int empty_drawable = R.drawable.circle_view;
    private final int fill_drawable = R.drawable.circle_view_fill;
    private int position = 1;

    private static final String PREFERENCE_LOGIN = "EXISTS_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        layout_personal = findViewById(R.id.layout_dataPersonal);
        layout_login = findViewById(R.id.layout_dataLogin);
        layout_terms = findViewById(R.id.layout_termsUse);

        circle1 = findViewById(R.id.view_stage1);
        circle2 = findViewById(R.id.view_stage2);
        circle3 = findViewById(R.id.view_stage3);

        editName = findViewById(R.id.editText_name);
        editNickname = findViewById(R.id.editText_nickname);
        editEmail = findViewById(R.id.editText_email);
        editPassword = findViewById(R.id.editText_password);
        editConfirmPassword = findViewById(R.id.editText_confirmPassword);
        checkTermsUse = findViewById(R.id.cbx_termsUse);

        errorOptionUser = findViewById(R.id.error_optionUser);
        errorTermsUse = findViewById(R.id.error_termsUse);
        errorSingUp = findViewById(R.id.textView4);

        next_stage = findViewById(R.id.button_nextSingUp);
        back_stage = findViewById(R.id.button_backStage);
        createAcount = findViewById(R.id.button_createAcount);
        see_termsUse = findViewById(R.id.see_termsUse);

        //Recupera os valores colocados no Array (dentro do strings.xml)
        Resources res = getResources();
        String[] optionsUsers = res.getStringArray(R.array.options_peopleType);

        opClient = findViewById(R.id.rbtn_client);
        opSeller = findViewById(R.id.rbtn_seller);

        //Coloca os valores do Array nas respectivas opções
        opSeller.setText(optionsUsers[0]);
        opClient.setText(optionsUsers[1]);

        listenersButtonsStages();
        checkTermsUse.setOnClickListener(v ->{
            if (checkTermsUse.isChecked()) {
                circle3.setBackgroundResource(fill_drawable);
            } else{
                circle3.setBackgroundResource(empty_drawable);
            }
        });
        // TODO IMPLEMENTAR LEITURA DO TERMO DE USO ---> NOVA ACTIVITY
        see_termsUse.setOnClickListener(v ->{});
        createAcount.setOnClickListener(v -> listenerSingUp());
    }


    // Listener dos Botões Inferiores
    private void listenersButtonsStages() {

        // Listener do Botão "Proxima Etapa"
        next_stage.setOnClickListener(v -> {
            if (position == 1 & validationPersonalInfos()){
                // Mostra o Proximo Layout, Preenche o Circulo e muda o valor da variavel
                layout_personal.setVisibility(View.GONE);
                layout_login.setVisibility(View.VISIBLE);
                back_stage.setVisibility(View.VISIBLE);
                circle1.setBackgroundResource(fill_drawable);
                position = 2;

            } else if(position == 2 & validationLoginInfos()){
                // Abre a ultima parte do 1° Cadastro
                layout_login.setVisibility(View.GONE);
                layout_terms.setVisibility(View.VISIBLE);
                next_stage.setVisibility(View.INVISIBLE);
                circle2.setBackgroundResource(fill_drawable);
                position = 3;

            }
            closeKeyboard();
        });

        // Listener do Botão "Voltar"
        back_stage.setOnClickListener(v -> {
            if(position == 2){
                // Volta p/ os Dados Pessoais
                back_stage.setVisibility(View.GONE);
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
        });
    }

    // Validação do Nome, Nickname e Tipo do Usuario
    public boolean validationPersonalInfos(){
        name = editName.getText().toString();
        nickname = editNickname.getText().toString();

        if (!validationTypeUser()){
            return false;
        } else if(name.equals("")){
            editName.setError("Campo Obrigatório.");
            return false;
        } else if(nickname.equals("")){
            editNickname.setError("Campo Obrigatório.");
            return false;
        } else {
            return true;
        }
    }

    // Validação do Tipo de Usuario
    public boolean validationTypeUser(){
        if(opClient.isChecked()){
            optionUser="Cliente";
            errorOptionUser.setVisibility(View.GONE);
            return true;
        } else if (opSeller.isChecked()){
            optionUser="Vendedor";
            errorOptionUser.setVisibility(View.GONE);
            return true;
        } else{
            errorOptionUser.setVisibility(View.VISIBLE);
            return false;
        }
    }

    // Validação do Email e Senha
    public boolean validationLoginInfos(){
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        confirmPassword = editConfirmPassword.getText().toString();

        //TODO - É possivel adicionar um Icone apos a msg de erro
        if (email.equals("")){
            editEmail.setError("Campo Obrigatório.");
            return false;
        }  else if (password.equals("")){
            editPassword.setError("Campo Obrigatório.");
            return false;
        }  else if(confirmPassword.equals("")){
            editConfirmPassword.setError("Campo Obrigatório.");
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

    // Fecha o Teclaso (se estiver Aberto)
    public void closeKeyboard(){
        // Obtem o estado do Keyboard
        InputMethodManager keyboardManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        // Se for diferente de nullo = Fecha
        if(keyboardManager != null){
            keyboardManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // Cadastra o Usuario
    public void listenerSingUp() {
        // Valida Novamente as Etapas de Cadastros
        if (validationPersonalInfos()){
            if (validationLoginInfos()){
                if (validationTermsUse()){
                    // Todas as validações estão corretas ---> Fim do Cadastro
                    HandleSharedPreferences preferences = new HandleSharedPreferences(
                            getSharedPreferences(PREFERENCE_LOGIN, 0));

                    // Define TRUE para login Realizado
                    preferences.setLogin(true);

                    System.out.println("Nome: " + name + "\nEmail: " + email + "\nNickname:" +
                            nickname + "\nSenha: " + password + "\nConfirmar Senha: " +
                            confirmPassword + "\nOpção Cliente: " + opClient.isChecked() +
                            "\nOpção Vendedor: " + optionUser + "\nTermos de Uso: " +
                            checkTermsUse.isChecked());

                    // Finaliza essa Activity e Inicia a Activity do Cadastro Completo
                    startActivity(new Intent(this, RegisterForPurchases.class));
                    finish();

                    // Deixa os Circulos Cinza das Etapas Erradas
                } else {
                    circle3.setBackgroundResource(empty_drawable);
                    errorSingUp.setVisibility(View.VISIBLE);
                }
            } else {
                circle2.setBackgroundResource(empty_drawable);
                errorSingUp.setVisibility(View.VISIBLE);
            }
        } else {
            circle1.setBackgroundResource(empty_drawable);
            errorSingUp.setVisibility(View.VISIBLE);
        }

    }

}