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
    private static final String LOGIN_KEY = "login";
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
     * Verifica se Existe um Login no Aplicativo
     */
    public boolean existLogin() {
        return preferences.getBoolean(LOGIN_KEY, false);
    }

    /**
     * Altera a Variavel que diz se existe ou não Login no Aplicativo
     *
     * @param isLogin valor da existencia ou não do login
     */
    public void setLogin(boolean isLogin) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGIN_KEY, isLogin);
        editor.apply();
    }

}
