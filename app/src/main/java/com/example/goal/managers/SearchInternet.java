package com.example.goal.managers;

import android.content.Context;
import android.util.Log;

import com.example.goal.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe SearchInternet: Busca/Manipula Dados em APIs Online
 */
public class SearchInternet {

    // Constantes de URLs das APIs Utilizadas
    public static final String API_BRAZIL_CITY = "https://brasilapi.com.br/api/ibge/municipios/v1";
    public static final String API_BRAZIL_CEP = "https://brasilapi.com.br/api/cep/v2/";
    public static final String API_BRAZIL_CNPJ = "https://brasilapi.com.br/api/cnpj/v1/";
    public static final String API_EMAIL_DISPONABLE = "https://open.kickbox.com/v1/disposable/";

    // Constantes de Possiveis Exception usadads nos Logs
    private final String NAME_CLASS = "SearchInternet";
    private final String EXCEPTION_URL = "Exception URL";
    private final String EXCEPTION_IO = "Exception IO";
    private final Context context;
    // Mensagem de Erro na Operação
    public String error_search;

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
     * @param uri    Caminho de Acesso da API
     * @param method Metodo de Pesquisa (GET, POST, PUT, DELETE)
     * @return String|null
     */
    public String SearchInAPI(String uri, String method) {
        // Variaveis Usadas na Pesquisa da API
        BufferedReader bufferedReader;
        String response_json;

        // Tratamento de Erros
        try {
            URL url_search = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url_search.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.connect();

            // todo: criar uma serialização propria para obter o erro
            switch (urlConnection.getResponseCode()) {
                case 200:
                    break;
                case 404:
                    error_search = context.getResources().getString(R.string.error_404);
                    return null;
                case 500:
                    error_search = context.getResources().getString(R.string.error_500);
                    return null;
                default:
                    // todo: adicionar codigos de erro da API
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
        } catch (MalformedURLException ex) {
            Log.e(EXCEPTION_URL, NAME_CLASS + " - Erro na Formaçao da URL");
            ex.printStackTrace();
        } catch (IOException ex) {
            Log.e(EXCEPTION_IO, NAME_CLASS + " - Erro em algum Processamento IO");
            ex.printStackTrace();
        }

        // Houve um Erro em alguma Etapa: Erro generico (Resposta da API em Branco ou Dados em Branco)
        error_search = context.getResources().getString(R.string.error_generic);
        return null;
    }

    public String getError_search() {
        return error_search;
    }

}
