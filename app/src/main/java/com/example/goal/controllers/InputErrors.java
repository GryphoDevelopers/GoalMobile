package com.example.goal.controllers;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InputErrors {

    private final ManagerKeyboard managerKeyboard;

    public InputErrors(Context context) {
        managerKeyboard = new ManagerKeyboard(context);
    }

    public void errorInputEditText(TextInputEditText inputEditText, String textError) {
        inputEditText.setError(textError);
        inputEditText.requestFocus();
        managerKeyboard.openKeyboard(inputEditText);
    }

    public void errorInputLayout(TextInputLayout textInputLayout, String textError) {
        textInputLayout.setError(textError);
        textInputLayout.requestFocus();
        managerKeyboard.openKeyboard(textInputLayout);
    }

    public void errorInputWithoutIcon(TextInputEditText inputEditText, String textError) {
        inputEditText.setError(textError, null);
        inputEditText.requestFocus();
        managerKeyboard.openKeyboard(inputEditText);
    }

}