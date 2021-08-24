package com.example.goal.controller;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;

public class InputErrors {

    private final ManagerKeyboard managerKeyboard;

    public InputErrors(Context context){
        managerKeyboard = new ManagerKeyboard(context);
    }

    public void errorInput(TextInputEditText inputEditText, String textError) {
        inputEditText.setError(textError);
        inputEditText.requestFocus();
        managerKeyboard.openKeyboard(inputEditText);
    }

    public void errorInputWithoutIcon(TextInputEditText inputEditText, String textError) {
        inputEditText.setError(textError, null);
        inputEditText.requestFocus();
        managerKeyboard.openKeyboard(inputEditText);
    }

}
