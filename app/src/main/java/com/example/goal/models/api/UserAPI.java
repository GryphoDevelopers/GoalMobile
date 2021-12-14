package com.example.goal.models.api;

import static com.example.goal.managers.SearchInternet.API_ATTR_CHANGE_LEVEL;
import static com.example.goal.managers.SearchInternet.API_GOAL_INSERT_USER;
import static com.example.goal.managers.SearchInternet.API_GOAL_TOKEN;
import static com.example.goal.managers.SearchInternet.API_GOAL_USER;
import static com.example.goal.managers.SearchInternet.GET;
import static com.example.goal.managers.SearchInternet.PATCH;
import static com.example.goal.managers.SearchInternet.POST;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.SearchInternet;
import com.example.goal.models.SerializationInfo;
import com.example.goal.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UserAPI {

    private final Context context;
    private final String EXCEPTION_GENERAL = "Exception General";
    private final String NAME_CLASS = "UserAPI";
    private final String BLANK_GUID = "00000000-0000-0000-0000-000000000000";
    private final String ERROR_SEARCH = "Error Search";
    private String error_operation = "";

    public UserAPI(Context context) {
        this.context = context;
    }

    /**
     * Atualiza as Informações do Usuario na API
     *
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @param userRegister    {@link User} que será registrado na API
     * @return true|false
     */
    public static boolean updateUserAPI(ExecutorService executorService, User userRegister) {
        //todo: implementação futura
        return true;
    }

    /**
     * Insere um Usuario no Banco de Dados via API
     *
     * @param user_register   Usuario inserido no Banco de Dados
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @return {@link User}|null
     */
    public User registerInAPI(ExecutorService executorService, User user_register) {
        try {
            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
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

            SerializationInfo serializationInfo = new SerializationInfo(context);
            String[] return_user = serializationInfo.jsonStringToArray(json_email,
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
     * @param email           Email do Usuario que será gerado o Token da API
     * @param password        Senha do Usuario que será gerado o Token da API
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @return {@link String}|""
     * @see #registerInAPI(ExecutorService, User)
     */
    public User getTokenUser(ExecutorService executorService, String email, String password) {
        try {
            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
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
            String json_token = futureTasksList.get(0).get();

            // Valida o Resultado e Serializa
            if (json_token != null) {
                SerializationInfo serializationInfo = new SerializationInfo(context);
                String[] return_user = serializationInfo.jsonStringToArray(json_token,
                        new String[]{"id", "name", "surname", "sellerId", "token"});

                // Caso esteja disponivel, retorna o JWT (TOken) do Usuario
                if (return_user != null) {
                    User user = new User(context);
                    user.setId_user(return_user[0]);
                    user.setName(return_user[1]);
                    user.setLast_name(return_user[2]);
                    user.setId_seller(return_user[3]);
                    user.setSeller(return_user[3].equals(BLANK_GUID));
                    user.setToken_user(return_user[4]);
                    return user;
                }
            }

        } catch (Exception ex) {
            Log.e(EXCEPTION_GENERAL, NAME_CLASS + " - Execução ao Executar a Inserção na API: "
                    + ex.getClass().getName());
            ex.printStackTrace();
        }

        // Caso ocorra alguma Exception, API retornou algum erro ou Serialização retornou null
        error_operation = Html.fromHtml(context.getString(R.string.error_login_api)).toString();
        return null;
    }

    /**
     * Obtem os dados do Usuario da API
     *
     * @param id              ID do Usuario que será obtido da API
     * @param token           Token do Usuario que será obtido
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @return {@link User}|null
     */
    public User getInfoUserAPI(ExecutorService executorService, String id, String token) {
        try {
            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                // Realiza uma Requisição GET na API
                String uri_user = Uri.parse(API_GOAL_USER).buildUpon().appendPath(id).build().toString();
                String json_api = searchInternet.SearchInAPI(uri_user, GET, token);

                if (json_api == null) error_operation = searchInternet.getError_search();
                return json_api;
            });

            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_user = futureTasksList.get(0).get();

            if (json_user == null) return null;

            SerializationInfo serializationInfo = new SerializationInfo(context);
            String[] return_user = serializationInfo.jsonStringToArray(json_user,
                    new String[]{"id", "name", "surname", "email", "sellerId"});

            if (return_user != null) {
                User user = new User(context);
                user.setId_user(return_user[0]);
                user.setName(return_user[1]);
                user.setLast_name(return_user[2]);
                user.setEmail(return_user[3]);
                user.setId_seller(return_user[4]);
                user.setSeller(return_user[4].equals(BLANK_GUID));
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
     * Atualiza o Usuario para Vendedor
     *
     * @param token           Token do Usuario obtido da API
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @return {@link User}|null
     */
    public User changeLevelUser(ExecutorService executorService, String id_user, String token) {
        try {
            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                // Realiza uma Requisição GET na API
                String uri_user = Uri.parse(API_GOAL_USER).buildUpon()
                        .appendPath(id_user)
                        .appendPath(API_ATTR_CHANGE_LEVEL).build().toString();
                String json_api = searchInternet.SearchInAPI(uri_user, PATCH, token);

                if (json_api == null) error_operation = searchInternet.getError_search();
                return json_api;
            });

            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_user = futureTasksList.get(0).get();

            if (json_user == null) return null;

            SerializationInfo serializationInfo = new SerializationInfo(context);
            String[] return_user = serializationInfo.jsonStringToArray(json_user,
                    new String[]{"id", "name", "surname", "email", "sellerId"});

            if (return_user != null) {
                User user = new User(context);
                user.setId_user(return_user[0]);
                user.setName(return_user[1]);
                user.setLast_name(return_user[2]);
                user.setEmail(return_user[3]);
                user.setId_seller(return_user[4]);
                user.setSeller(return_user[4].equals(BLANK_GUID));
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


    // Getters e Setters
    public String getError_operation() {
        return error_operation;
    }

}
