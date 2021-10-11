package com.example.goal.views.widgets;

import android.view.View;

import com.example.goal.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * Classe SnackBarPersonalized: Exibe uma SnackBar Configurada com o Texto
 */
public class SnackBarPersonalized {

    private final View view;

    /**
     * Construtor da Classe SnackBarPersonalized
     *
     * @param view View (Layouts...) onde será mostrado o SnackBar
     */
    public SnackBarPersonalized(View view) {
        this.view = view;
    }

    /**
     * Cria uma SnackBar Padrão. Ao clicar na SnackBar, ela fechará
     *
     * @param text_string String que será exibida
     * @return Snackbar
     */
    public Snackbar defaultSnackBar(String text_string) {
        return Snackbar.make(view, text_string, Snackbar.LENGTH_LONG)
                .setAction(view.getContext().getString(R.string.button_close), v -> { });
    }

}