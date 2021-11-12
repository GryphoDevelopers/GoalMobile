package com.example.goal.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe HandlerSharedPreferences: Manipulaas Preferences do Aplicativo
 */
public class ManagerSharedPreferences {

    // Constantes dos nomes das Preferences
    public static final String NAME_PREFERENCE = "app_goal";
    // Constantes das Chaves das Preferences
    private static final String REMEMBER_LOGIN_KEY = "remember_login";
    private final SharedPreferences preferences;

    /**
     * Cria uma nova Instancia da HandlerSharedPreferences. Permite com que obtenha/manipule
     * as Preferences
     *
     * @param name_preferences Nome do Arquivo das Preferences
     * @param context          Context usado para obter as Preferences
     */
    public ManagerSharedPreferences(Context context, String name_preferences) {
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

    /**
     * Apaga todas as SharedPreferences do APP salvas
     */
    public void clearPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
