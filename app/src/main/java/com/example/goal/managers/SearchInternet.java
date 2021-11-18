package com.example.goal.managers;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;
import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.managers.ManagerResources.isNullOrEmpty;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.example.goal.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Classe SearchInternet: Busca/Manipula Dados em APIs Online
 */
public class SearchInternet {

    /**
     * Verbo HTTPs (Metodo) GET (Obtenção de Informações ou Envio de Informação não Sensiveis) : Não
     * há necessidade de Body
     */
    public static final String GET = "GET";

    /**
     * Verbo HTTPs (Metodo) POST (Envio de Dados Sensiveis): Há necessidade de Body
     */
    public static final String POST = "POST";

    /**
     * Verbo HTTPs (Metodo) PUT (Envio de Atualizações de Dados): Há necessidade de Body
     */
    public static final String PUT = "PUT";

    /**
     * Verbo HTTPs (Metodo) DELETE (Exclusão de Dados na API): Há necessidade de Body
     */
    public static final String DELETE = "DELETE";

    /**
     * URL de Pesquisa de Cidades na API Brasil
     *
     * @see <a href="https://brasilapi.com.br">API Brasil</a>
     */
    public static final String API_BRAZIL_CITY = "https://brasilapi.com.br/api/ibge/municipios/v1";

    /**
     * URL de Pesquisa de CEP na API Brasil
     *
     * @see <a href="https://brasilapi.com.br">API Brasil</a>
     */
    public static final String API_BRAZIL_CEP = "https://brasilapi.com.br/api/cep/v2/";

    /**
     * URL de Pesquisa de CNPJ na API Brasil
     *
     * @see <a href="https://brasilapi.com.br">API Brasil</a>
     */
    public static final String API_BRAZIL_CNPJ = "https://brasilapi.com.br/api/cnpj/v1/";

    /**
     * URL de Pesquisa de Emails na kickbox
     *
     * @see <a href="https://docs.kickbox.com/docs">Kickbox</a>
     */
    public static final String API_EMAIL_DISPOSABLE = "https://open.kickbox.com/v1/disposable/";

    /**
     * URL da Geração de Token na API Goal (API do Projeto)
     *
     * @see <a href="https://goalwebapi.herokuapp.com/swagger/index.html">API Goal</a>
     */
    public static final String API_GOAL_TOKEN = "https://goalwebapi.herokuapp.com/goal/api/v1/auth";

    /**
     * URL de Cadastro de Usuarios na API Goal (API do Projeto)
     *
     * @see <a href="https://goalwebapi.herokuapp.com/swagger/index.html">API Goal</a>
     */
    public static final String API_GOAL_INSERT_USER = "https://goalwebapi.herokuapp.com/goal/api/v1/auth/add-access";


    // todo: alterar para URL correta e colocar na Classe SearchInternet
    /**
     * URL que Obtem os Produtos que serão Exibidos (URL de Teste)
     */
    public static final String URL_PRODUCTS = "http://makeup-api.herokuapp.com/api/v1/products.json?rating_greater_than=4.5";

    // Constantes de Possiveis Exception usadads nos Logs
    private final String NAME_CLASS = "SearchInternet";

    // Variaveis usadas na Classe
    private final Context context;
    private String error_search = "";
    private String json_body = "";


    /**
     * Contrutor da Classe SearchInternet com o Context
     *
     * @param context Utilizado para obter as strings do string.xml
     */
    public SearchInternet(Context context) {
        this.context = context;
    }

    /**
     * Busca dados em uma API de Forma Assincrona.
     * <p>
     * Utiliza ExecutorService para Criar uma nova Thread e Callable para montar a Execução das Ações
     *
     * @param uri                 Caminho de Acesso da API
     * @param method              Metodo de Pesquisa (GET, POST, PUT, DELETE)
     * @param token_authorization JsonWebToken utilizado no Request. Caso não seja necessario, insira
     *                            "null" no valor
     * @return String|null
     */
    public String SearchInAPI(String uri, String method, String token_authorization) {

        if (!new ManagerServices(context).availableInternet()) {
            error_search = Html.fromHtml(context.getString(R.string.error_network)).toString();
            return null;
        }

        // Variaveis Usadas na Pesquisa da API
        BufferedReader bufferedReader;
        String response_json;

        // Tratamento de Erros
        try {
            URL url_search = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url_search.openConnection();
            urlConnection.setRequestMethod(method);

            // Configurando o Header (Tipo do Conteudo, Retorno e Token (Se Necessario))
            urlConnection.setRequestProperty("Accept", "application/json");
            if (token_authorization != null) {
                urlConnection.setRequestProperty("Authorization", String.format(
                        "Bearer %s", token_authorization));
            }

            // Conforme a necessidade do Verbo HTTPs, valida o Body
            if (!method.equals(GET)) {
                if (isNullOrEmpty(json_body)) {
                    // Body não Formado
                    if (isNullOrEmpty(error_search))
                        error_search = context.getString(R.string.error_500);
                    return null;
                } else {
                    // Configura as propriedades do Body
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);

                    // Conversão de String para Byte em UTF-8 e COnfiguração do Tamanho no Header
                    byte[] input = SDK_INT >= KITKAT ? json_body.getBytes(StandardCharsets.UTF_8)
                            : json_body.getBytes("UTF-8");
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(input.length));

                    // Cria o Body do Post
                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(input, 0, input.length);
                    outputStream.close();
                }
            }

            // Executa a Conexão com os Valores Configurados
            urlConnection.connect();

            // todo: criar uma serialização propria para obter o erro e adicionar novos codigos erros
            switch (urlConnection.getResponseCode()) {
                case 200:
                    break;
                case 400:
                    error_search = context.getResources().getString(R.string.error_400);
                    return null;
                case 404:
                    error_search = context.getResources().getString(R.string.error_404);
                    return null;
                case 500:
                    error_search = context.getResources().getString(R.string.error_500);
                    return null;
                default:
                    error_search = context.getResources().getString(R.string.error_generic);
                    return null;
            }

            // Obtem a Resposta da API e Verifica se o Retorno está Vazio
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                // Obtem e Trata a Leitura dos Dados
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                // Verifica se o BufferedReader possui algum dado para ser Lido
                while ((line = bufferedReader.readLine()) != null) {
                    // Cria uma String dando um "Pulo" Linha a Linha
                    stringBuilder.append(line).append("\n");
                }

                // Verifica se há itens na String Formada e passa os Dados para a String final
                if (stringBuilder.length() > 0) {
                    response_json = stringBuilder.toString();
                    return response_json;
                }
            }
        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Erro na Pesquisa na API: " + ex.getClass().getName());
            ex.printStackTrace();
        }

        // Houve um Erro em alguma Etapa: Erro generico (Resposta da API em Branco ou Dados em Branco)
        error_search = context.getResources().getString(R.string.error_generic);
        return null;
    }

    /**
     * Cria o Body (Padrão) que será usados nos Metodos PUT, DELETE, POST e outros nas Requisições da API
     *
     * @param parameters Parametros que serão Inseridos no Body (não aceita valores nulos ou Vazios).
     *                   Para Cada Parametro deve haver um valor que respeite as
     *                   condições dos Valores
     * @param values     Valores dos Parametros que serão inseridos (não aceita valores nulos, mas
     *                   aceita Valores Vazios)
     *                   <p>
     *                   <p>
     *                   * Valores Vazios = ""
     */
    public void setBodyRequest(String[] parameters, String[] values) {

        StringBuilder body = new StringBuilder();
        if (parameters != null && values != null && parameters.length == values.length) {

            // Laço de Repetição para Acessar os Itens dos Arrays
            for (int i = 0; i < parameters.length; i++) {

                // Realiza uma Validação nos Valores. Caso passe, insere os valores na variavel body
                if (parameters[i] == null || parameters[i].equals("")) {
                    error_search = context.getString(R.string.error_500);
                    Log.e(EXCEPTION, NAME_CLASS + " - Erro na Formação do Body: " +
                            "Parametros Invalidos (Não Permitidos Valores Nulos ou Vazios)");
                    json_body = "";
                    return;
                } else if (values[i] == null) {
                    Log.e(EXCEPTION, NAME_CLASS + " - Erro na Formação do Body: " +
                            "Valores do Body Invalidos (Não Permitidos Valores Nulos ou Vazios)");
                    json_body = "";
                    return;
                } else {
                    try {
                        // Abre a Chave do JSON antes da inserção dos Primeiros Valores
                        if (i == 0) body = new StringBuilder("{");

                        // Caso Seja o Ultimo Item dos Arrays: Fechamento das Chaves, sem virgula no Final
                        if (i == parameters.length - 1) {
                            body.append(String.format("\"%1$s\": \"%2$s\" }", parameters[i], values[i]));
                        } else {
                            body.append(String.format("\"%1$s\": \"%2$s\",", parameters[i], values[i]));
                        }
                    } catch (Exception ex) {
                        // Tratamento de Exceção na String Format
                        Log.e(EXCEPTION, NAME_CLASS + " - Erro na Formação do Body: "
                                + ex.getClass().getName());
                        ex.printStackTrace();

                        error_search = context.getString(R.string.error_exception);
                        json_body = "";
                        return;
                    }
                }
            }

        } else {
            error_search = context.getString(R.string.error_500);
            Log.e(EXCEPTION, NAME_CLASS + " - Erro na Formação do Body: " +
                    "Parametros Invalidos (Não Permitidos Valores Nulos ou Vazios)");
            json_body = "";
        }

        // Define o valor do JsonBody usado no PUT, POST, DELETE
        json_body = body.toString();
    }


    /**
     * Caso exista, retorna a mensagem de erro
     */
    public String getError_search() {
        return error_search;
    }

}
