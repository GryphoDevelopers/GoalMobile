package com.example.goal.managers;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Classe ManagerInputErrors: Manipula a exibição de erros nos Inputs
 */
public class ManagerInputErrors {

    private final Context context;

    /**
     * Contrutor da Classe
     *
     * @param context Utiliza o Context para usa-lo nas Operações
     */
    public ManagerInputErrors(Context context) {
        this.context = context;
    }

    /**
     * Exibe um Erro no EditText (Material UI)
     *
     * @param inputEditText Onde será aplicado o Erro
     * @param text_error    Texto do Erro que será exibido
     * @param show_icon     Define se será mostrado ou não o Icone de Erro Padrão do Androd
     */
    public void errorInputEditText(TextInputEditText inputEditText, String text_error, boolean show_icon) {
        if (show_icon) inputEditText.setError(text_error);
        else inputEditText.setError(text_error, null);
        inputEditText.requestFocus();
        new ManagerServices(context).openKeyboard(inputEditText);
    }

    /**
     * Exibe um Erro no Layout do EditText (Material UI)
     *
     * @param textInputLayout Onde será aplicado o Erro
     * @param textError       Texto do Erro que será exibido
     */
    public void errorInputLayout(TextInputLayout textInputLayout, String textError) {
        textInputLayout.setError(textError);
        textInputLayout.requestFocus();
        new ManagerServices(context).openKeyboard(textInputLayout);
    }

}
