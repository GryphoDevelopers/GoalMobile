package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email;
    private  EditText edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.editText_email);
        edit_password = findViewById(R.id.editText_password);
    }

    public void SingUp(View view){
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();
        if (email.equals("")) {
            edit_email.setError("Campo obrigatório");
        } else if (password.equals("")) {
            //TODO - É possivel adicionar um Icone apos a msg de erro
            edit_password.setError("Campo obrigatório");
        }
        else {
            System.out.println("Campos Preenchidos");
        }
    }

}