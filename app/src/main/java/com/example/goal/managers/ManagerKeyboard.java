package com.example.goal.managers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Classe ManagerKeyboard: Controla as ações do Teclado (Fechar/Abrir)
 */
public class ManagerKeyboard {

    private final InputMethodManager keyboardManager;

    /**
     * Contrutor da Classe ManagerKeyboard
     *
     * @param context Context é usado para conseguir obter o serviço de manipulação do teclado
     */
    public ManagerKeyboard(Context context) {
        // Obtem o estado do Keyboard
        this.keyboardManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * Abra o Teclado na View Informada
     *
     * @param viewOpen View (ex: Activity, EditText) que deseja abrir o teclado
     */
    public void openKeyboard(View viewOpen) {
        // Se ontem o controlador do Teclado = Abre
        if (keyboardManager != null) {
            keyboardManager.showSoftInput(viewOpen, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Responsavel por fechar o Teclado
     *
     * @param activity Activity em que o Teclado será fechado
     */
    public void closeKeyboard(Activity activity) {
        // Se obtem o controlador do Teclado = Fecha
        if (keyboardManager != null) {
            keyboardManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
