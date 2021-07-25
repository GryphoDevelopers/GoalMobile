package com.example.goal.views;

import android.content.Context;
import android.view.View;

import com.example.goal.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackBarPersonalized {

    private View view;

    public SnackBarPersonalized(View view){
        this.view = view;
    }

    public Snackbar makeDefaultSnackBar(int string_text){
        Snackbar snackbar = Snackbar.make(view, string_text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.button_close, v -> {
            // Listener do Botão "Fechar"
            snackbar.dismiss();
        });

        return snackbar;
    }


}
