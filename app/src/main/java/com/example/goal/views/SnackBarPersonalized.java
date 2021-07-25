package com.example.goal.views;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarPersonalized {

    private final View view;

    public SnackBarPersonalized(View view){
        this.view = view;
    }

    public Snackbar makeDefaultSnackBar(int string_text){
        return Snackbar.make(view, string_text, Snackbar.LENGTH_LONG);
    }

}