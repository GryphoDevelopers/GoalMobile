package com.example.goal.managers;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ManagerInputErrors {

    private final ManagerKeyboard managerKeyboard;

    public ManagerInputErrors(Context context) {
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
