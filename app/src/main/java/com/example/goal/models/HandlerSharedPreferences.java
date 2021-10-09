package com.example.goal.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe HandlerSharedPreferences: Manipulaas Preferences do Aplicativo
 */
public class HandlerSharedPreferences {

    // Constantes dos nomes das Preferences
    public static final String NAME_PREFERENCE = "app_goal";
    // Constantes das Chaves das Preferences
    private static final String REMEMBER_LOGIN_KEY = "remember_login";
    private static final String NOT_LOGGED = "not_logged";
    private final SharedPreferences preferences;

    /**
     * Cria uma nova Instancia da HandlerSharedPreferences. Permite com que obtenha/manipule
     * as Preferences
     *
     * @param name_preferences Nome do Arquivo das Preferences
     * @param context          Context usado para obter as Preferences
     */
    public HandlerSharedPreferences(Context context, String name_preferences) {
        this.preferences = context.getSharedPreferences(name_preferences, 0);
    }

    /**
     * Verifica se o usuario escolheu ou não o "Lembrar login"
     *
     * @return true/false
     */
    public boolean isRememberLogin() {
        return preferences.getBoolean(REMEMBER_LOGIN_KEY, false);
    }

    /**
     * Altera a Variavel que armazena o "Lembrar Login"
     *
     * @param isRememberLogin valor se irá lembrar o Login ou não
     */
    public void rememberLogin(boolean isRememberLogin) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(REMEMBER_LOGIN_KEY, isRememberLogin);
        editor.apply();
    }

}
