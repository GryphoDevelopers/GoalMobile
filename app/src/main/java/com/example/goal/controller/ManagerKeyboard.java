package com.example.goal.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class ManagerKeyboard {

    private Context context;

    public ManagerKeyboard(Context context){
        this.context = context;
    }

    // Abre o Teclado
    public void openKeyboard(View viewOpen){
        // Obtem o estado do Keyboard
        InputMethodManager keyboardManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Se ontem o controlador do Teclado = Abre
        if(keyboardManager != null){
            keyboardManager.showSoftInput(viewOpen, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // Fecha o Teclado
    public void closeKeyboard(Activity activity){
        // Obtem o estado do Keyboard
        InputMethodManager keyboardManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Se obtem o controlador do Teclado = Fecha
        if(keyboardManager != null){
            keyboardManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
