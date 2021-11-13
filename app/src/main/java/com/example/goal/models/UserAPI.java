package com.example.goal.models;

import static com.example.goal.managers.SearchInternet.API_GOAL_INSERT_USER;
import static com.example.goal.managers.SearchInternet.API_GOAL_TOKEN;
import static com.example.goal.managers.SearchInternet.POST;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.SearchInternet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserAPI {

    private final Context context;
    private final String EXCEPTION_GENERAL = "Exception General";
    private final String NAME_CLASS = "UserAPI";
    private final String BLANK_GUID = "00000000-0000-0000-0000-000000000000";
    private final String ERROR_SEARCH = "Error Search";
    private String error_operation;

    public UserAPI(Context context) {
        this.context = context;
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

    /**
     * Insere um Usuario no Banco de Dados via API
     *
     * @param user_register Usuario inserido no Banco de Dados
     * @return {@link User}|null
     */
    public User registerInAPI(User user_register) {
        try {
            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {

                // Cria o Body do POST do Usuario
                String[] parameters = new String[]{"id", "name", "surname", "email", "password"};
                String[] values_user = new String[]{BLANK_GUID, user_register.getName(),
                        user_register.getLast_name(), user_register.getEmail(), user_register.getPassword()};
                searchInternet.setBodyRequest(parameters, values_user);

                String json_api = searchInternet.SearchInAPI(API_GOAL_INSERT_USER, POST, null);

                if (json_api == null) error_operation = searchInternet.getError_search();
                return json_api;
            });

            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_email = futureTasksList.get(0).get();

            if (json_email == null) return null;

            SerializationInfos serializationInfos = new SerializationInfos(context);
            String[] return_user = serializationInfos.jsonStringToArray(json_email,
                    new String[]{"id", "name", "surname", "email", "password"});

            if (return_user != null) {
                User user = new User(context);
                user.setId_user(return_user[0]);
                user.setName(return_user[1]);
                user.setLast_name(return_user[2]);
                user.setEmail(return_user[3]);
                user.setPassword(return_user[4]);
                return user;
            }

        } catch (Exception ex) {
            Log.e(EXCEPTION_GENERAL, NAME_CLASS + " - Execução ao Executar a Inserção na API: "
                    + ex.getClass().getName());
            ex.printStackTrace();
        }

        // Ocorreu alguma Exception ou o Usuario não foi possivel de ser Cadastrado na API
        error_operation = context.getString(R.string.error_register_api);
        return null;
    }

    /**
     * Obtem o JWT (Json Web Token) / Token de um Usuario cadastrado na API
     *
     * @param email    Email do Usuario que será gerado o Token da API
     * @param password Senha do Usuario que será gerado o Token da API
     * @return {@link String}|""
     * @see #registerInAPI(User)
     */
    public String getTokenUser(String email, String password) {
        try {
            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                // Cria o Body do POST do Usuario
                searchInternet.setBodyRequest(new String[]{"email", "password"}, new String[]{email, password});

                String json_api = searchInternet.SearchInAPI(API_GOAL_TOKEN, POST, null);

                if (json_api == null) Log.e(ERROR_SEARCH, NAME_CLASS +
                        " - Erro na Busca na API: " + searchInternet.getError_search());
                return json_api;
            });


            // Obtem o Resultado da Busca na API
            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_email = futureTasksList.get(0).get();

            // Valida o Resultado e Serializa
            if (json_email != null) {
                SerializationInfos serializationInfos = new SerializationInfos(context);
                String[] return_user = serializationInfos.jsonStringToArray(json_email,
                        new String[]{"token"});

                // Caso esteja disponivel, retorna o JWT (TOken) do Usuario
                if (return_user != null && return_user[0] != null) return return_user[0];
            }

        } catch (Exception ex) {
            Log.e(EXCEPTION_GENERAL, NAME_CLASS + " - Execução ao Executar a Inserção na API: "
                    + ex.getClass().getName());
            ex.printStackTrace();
        }

        // Caso ocorra alguma Exception, API retornou algum erro ou Serialização retornou null
        error_operation = Html.fromHtml(context.getString(R.string.error_login_api)).toString();
        return "";
    }

    /**
     * Obtem os dados do Usuario da API
     *
     * @param email    Email do Usuario que será obtido na API
     * @param password Senha do Usuario que será obtida na API
     * @param token    Token do Usuario que será obtido
     * @return {@link User}|null
     */
    public User getInfoUserAPI(String email, String password, String token) {
        // todo: Remover e Implementar Obtenção do usuario da API
        User userAPI = new User(context);
        userAPI.setId_user("56");
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

    // Getters e Setters
    public String getError_operation() {
        return error_operation;
    }

}
