package com.example.goal.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe HandlerSharedPreferences: Manipulaas Preferences do Aplicativo
 */
public class ManagerSharedPreferences {

    /**
     * Constante que define o Nome das SharedPreferences Padrão
     */
    public static final String NAME_PREFERENCE = "app_goal";

    // Constantes das Chaves das Preferences
    private static final String REMEMBER_LOGIN_KEY = "remember_login";
    private static final String USER_JSON_WEB_TOKEN = "jwt_user";
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
    public void setRememberLogin(boolean isRememberLogin) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(REMEMBER_LOGIN_KEY, isRememberLogin);
        editor.apply();
    }

    /**
     * Retorna o Token (JWT) do Usuario salvo nas SharedPreferences
     *
     * @return {@link String}|""
     */
    public String getJsonWebTokenUser() {
        return preferences.getString(USER_JSON_WEB_TOKEN, "");
    }

    /**
     * Salva o Token (JWT) do Usuario em uma SharedPreferences
     *
     * @param token Token (JWT) de Autenticação do Usuario
     */
    public void setJsonWebTokenUser(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_JSON_WEB_TOKEN, token);
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
