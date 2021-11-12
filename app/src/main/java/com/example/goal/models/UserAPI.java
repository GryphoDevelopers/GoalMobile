package com.example.goal.models;

import android.content.Context;

public class UserAPI {
    /**
     * Insere um Usuario no Banco de Dados via API
     *
     * @param userSingUp Usuario inserido no Banco de Dados
     * @return boolean
     */
    public static boolean registerInAPI(User userSingUp) {
        //todo: implementação futura
        return true;
    }

    /**
     * Obtem o JWT (Json Web Token) da API
     *
     * @param email    Email do Usuario que será gerado o Token da API
     * @param password Senha do Usuario que será gerado o Token da API
     * @return {@link String}|""
     */
    public static String getTokenUser(String email, String password) {
        // todo Implementar Obtenção do Token da API
        return "todo: implementar token";
    }

    /**
     * Obtem os dados do Usuario da API
     *
     * @param email    Email do Usuario que será obtido na API
     * @param password Senha do Usuario que será obtida na API
     * @param token    Token do Usuario que será obtido
     * @param context  Context usado para criar um novo Usuario
     * @return {@link User}|null
     */
    public static User getInfoUserAPI(String email, String password, String token, Context context) {
        // todo: Remover e Implementar Obtenção do usuario da API
        User userAPI = new User(context);
        userAPI.setId_user(56);
        userAPI.setName("gasgas asfas");
        userAPI.setCpf("45123632511");
        userAPI.setPhone("011997774466");
        userAPI.setNickname("generic_test_nickname");
        userAPI.setDate_birth("01/04/2000");
        userAPI.setSeller(true);

        userAPI.setEmail(email);
        userAPI.setPassword(password);
        return userAPI;
    }

    /**
     * Atualiza as Informações do Usuario na API
     *
     * @return true|false
     */
    public static boolean updateUserAPI(User userRegister) {
        //todo: implementação futura
        return true;
    }
}
