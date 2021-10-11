package com.example.goal.models;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.controllers.SearchInternet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Classe User: Classe Usada para manipular as informações dos Usuario (Clientes e Vendedores)
 */
public class User {

    // Constantes da Força da Senha
    private static final int LOW_STRENGTH = 1;
    private static final int OK_STRENGTH = 2;
    // Constantes de Possiveis Exception usadads nos Logs
    private final String NAME_CLASS = "User";
    private final String EXECUTION_EXCEPTION = "Exception Execution";
    private final String INTERRUPTED_EXCEPTION = "Exception Interrupted";
    // Constantes Usadas nos Erros
    private final String INPUT_NULL;
    private final String INPUT_MIN_LENGTH;
    private final String INPUT_MAX_LENGTH;
    private final String INPUT_NOT_CHARS_ACCEPT;
    private final String INPUT_NOT_FORMAT;
    private final String INPUT_INVALID;
    private final String EMAIL_DISPOSABLE;
    private final String CNPJ_INVALID;
    private final String MESSAGE_EXCEPTION;

    private final Context context;
    private String name;
    private String email;
    private String nickname;
    private String cpf;
    private String password;
    private String confirmPassword;
    private String phone;
    private String cnpj;
    private boolean isSeller;
    private boolean checkedTermsUse;

    private String error_validation;

    /**
     * Construtor da Classe User
     *
     * @param context Context é utilizado para obter Strings pré-definidas de Erro
     */
    public User(Context context) {
        this.context = context;

        // Obtem as Strings de Erro
        INPUT_NULL = context.getString(R.string.validation_empty);
        INPUT_MIN_LENGTH = context.getString(R.string.validation_min_length);
        INPUT_MAX_LENGTH = context.getString(R.string.validation_max_length);
        INPUT_NOT_CHARS_ACCEPT = context.getString(R.string.validation_format_char);
        INPUT_NOT_FORMAT = context.getString(R.string.validation_format);
        INPUT_INVALID = context.getString(R.string.validation_not_disponible);
        EMAIL_DISPOSABLE = Html.fromHtml(context.getString(R.string.error_disposable_email)).toString();
        CNPJ_INVALID = Html.fromHtml(context.getString(R.string.error_cnpj)).toString();
        MESSAGE_EXCEPTION = context.getString(R.string.error_exception);
    }

    /**
     * Valida o Nome Baseando nos Parametros:
     * <p>
     * Tamanho de 3 a 80 Letras, com acentuação e espaços em branco
     *
     * @return true/false
     */
    public boolean validationName(String name) {
        if (name == null || name.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (name.length() < 3) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Nome", 3);
            return false;
        } else if (name.length() > 80) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Nome", 80);
            return false;
        } else if (!name.matches("^[A-ZÀ-úà-úa-zçÇ\\s]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Nome", "Letras");
            return false;
        } else return true;
    }

    /**
     * Valida o email informado nos seguintes parametros:
     * <p>
     * Tamanho de 15 à 120 Letras, numeros, pontos, underlines e @, mas sem espaços em branco
     *
     * @return true/false
     */
    public boolean validationEmail(String email) {
        if (email == null || email.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (email.length() < 10) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Email", 10);
            return false;
        } else if (email.length() > 120) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Email", 120);
            return false;
        } else if (!email.matches("[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?")) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Email", "email@*****.XX");
            return false;
        } else return true;
    }

    /**
     * Valida o email a partir de uma API
     *
     * @param email Email utilizado na Validação
     * @return true/false
     */
    public boolean validationEmailAPI(String email) {
        try {
            // URI de Pesquisa
            Uri build_uri = Uri.parse(SearchInternet.API_EMAIL_DISPONABLE)
                    .buildUpon().appendPath(email).build();

            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                String json_email = searchInternet.SearchInAPI(build_uri.toString(), "GET");

                if (json_email == null) return searchInternet.getError_search();
                return json_email;
            });

            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_email = futureTasksList.get(0).get();

            if (json_email != null) {
                SerializationInfos serializationInfos = new SerializationInfos(context);
                String[] infos_email = serializationInfos.jsonToArray(json_email, new String[]{"disposable"});

                if (infos_email != null) {
                    if (infos_email[0].equals("false")) return true;
                    error_validation = EMAIL_DISPOSABLE;
                } else error_validation = serializationInfos.getError_operation();

            } else error_validation = searchInternet.getError_search();

        } catch (InterruptedException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(INTERRUPTED_EXCEPTION, NAME_CLASS + " - Tarefa Assincrona Interrompida");
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(EXECUTION_EXCEPTION, NAME_CLASS + " - Falha na Execução da Tarefa Assincrona");
            ex.printStackTrace();
        }
        // Alguma etapa não foi bem sucedida
        return false;
    }

    /**
     * Valida o Nickname (Nome de Usario) informado nos seguintes parametros:
     * <p>
     * Tamanho de 5 à 60 Letras, podendo ter numeros, pontos, underlines, mas sem espaço em branco
     *
     * @return true/false
     */
    public boolean validationNickname(String nickname) {
        if (nickname == null || nickname.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (nickname.length() < 5) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Nome de Usuario", 5);
            return false;
        } else if (nickname.length() > 60) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Nome de Usuario", 60);
            return false;
        } else if (!nickname.matches("^[A-Za-z0-9._]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Nome de Usuario",
                    "Letras, Numeros, Underline ou Pontos");
            return false;
        } else return true;
    }

    /**
     * Valida o CPF informado. Somente é permitido numeros e esse CPF é validado pela API BRASIL
     *
     * @return true/false
     */
    public boolean validationCpf(String cpf) {
        if (cpf == null || cpf.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (cpf.length() != 11) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "CPF",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
            return false;
        } else if (!cpf.matches("^[0-9]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "CPF",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
            return false;
        } else if (!validationNumberCpf(cpf)) {
            error_validation = String.format(INPUT_INVALID, "CPF");
            return false;
        } else return true;
    }

    /**
     * Valida o CPF na API BRASIL
     *
     * @return true/false
     */
    private boolean validationNumberCpf(String cpf) {
        // todo: Codigo de Validação do CPF
        return true;
    }

    /**
     * Valida o CNPJ. Somente é permitido Numeros e tambem é validado na API Externa> API BRASIL
     *
     * @return true/false
     */
    public boolean validationCnpj(String cnpj) {
        if (cnpj == null || cnpj.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (cnpj.length() != 14) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "CNPJ",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
            return false;
        } else return true;
    }

    /**
     * Valida o CNPJ na API BRASIL
     *
     * @return true/false
     */
    public boolean validationNumberCnpj(String cnpj) {
        try {
            // URI de Pesquisa
            Uri build_uri = Uri.parse(SearchInternet.API_BRAZIL_CNPJ)
                    .buildUpon().appendPath(cnpj).build();

            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                String json_cnpj = searchInternet.SearchInAPI(build_uri.toString(), "GET");

                if (json_cnpj == null) return searchInternet.getError_search();
                return json_cnpj;
            });

            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_cnpj = futureTasksList.get(0).get();

            if (json_cnpj != null) {
                SerializationInfos serializationInfos = new SerializationInfos(context);
                String[] cnpj_reciver = serializationInfos.jsonToArray(json_cnpj,
                        new String[]{"cnpj", "descricao_situacao_cadastral"});

                if (cnpj_reciver != null) {
                    if (cnpj_reciver[0].equals(cnpj) && cnpj_reciver[1].equals("Ativa"))
                        return true;

                    error_validation = CNPJ_INVALID;
                } else error_validation = serializationInfos.getError_operation();

            } else error_validation = searchInternet.getError_search();

        } catch (InterruptedException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(INTERRUPTED_EXCEPTION, NAME_CLASS + " - Tarefa Assincrona Interrompida");
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(EXECUTION_EXCEPTION, NAME_CLASS + " - Falha na Execução da Tarefa Assincrona");
            ex.printStackTrace();
        }
        // Alguma etapa não foi bem sucedida
        return false;
    }

    /**
     * Valida a senha nos seguintes parametros
     * <p>
     * Tamanho de 5 à 40 Caracteres, porem sem espaços em branco. Tambem há uma validação na Força
     * da Senha inserida
     *
     * @return true/false
     */
    public boolean validationPassword(String password) {
        if (password == null || password.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (password.length() < 5) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Senha", 5);
            return false;
        } else if (password.length() > 40) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Senha", 40);
            return false;
        } else if (!password.matches("^[\\S]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Senha",
                    "Caracteres (Sem espaços em Branco)");
            return false;
        } else if (strengthPassword(password) == LOW_STRENGTH) {
            //TODO : mensagem de erro da senha
            error_validation = context.getString(R.string.validation_password,
                    "todo: alterar aqui");
            return false;
        } else return true;
    }

    /**
     * Retorna o da Força Valor da Senha Inserida
     *
     * @return int (1 para fraca e 2 para forte)
     */
    private int strengthPassword(String password) {
        // todo: implementar requisitos  para força da senha
        return OK_STRENGTH;
    }

    /**
     * Valida a confirmação da senha informada. Passa pela mesma validação da senha anterior e
     * confere se as senhas são compativeis
     *
     * @return true/false
     */
    public boolean validationConfirmPassword(User user) {
        String confirmPassword = user.getConfirmPassword();
        String password = user.getPassword();

        if (confirmPassword == null || confirmPassword.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (validationPassword(confirmPassword)) {
            if (password.equals(confirmPassword)) return true;

            // As senhas são diferentes
            error_validation = context.getString(R.string.validation_equal_password);
            return false;
        } else return false;
        // No ultimo else, a mensagem de erro foi definido na "validationPassword()"
    }

    /**
     * Valida o Numero de Telefone do Usario. Podendo ter apenas Numeros
     *
     * @return true/false
     */
    public boolean validationPhone(String phone) {
        if (phone == null || phone.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (phone.length() != 11) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Telefone", "11 Digitos (DDD + Numero)");
            return false;
        } else if (!phone.matches("^[0-9]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Telefone",
                    "Numeros (Sem Hifen, Ponto, Virgula ou Espaços em Branco)");
            return false;
        } else return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCheckedTermsUse() {
        return checkedTermsUse;
    }

    public void setCheckedTermsUse(boolean checkedTermsUse) {
        this.checkedTermsUse = checkedTermsUse;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getError_validation() {
        return error_validation;
    }
}
