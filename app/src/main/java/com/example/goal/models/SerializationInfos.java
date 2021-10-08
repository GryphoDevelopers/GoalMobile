package com.example.goal.models;

import android.content.Context;
import android.util.Log;

import com.example.goal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Classe SerializationInfos: Serializa Dados para exibir para o usuario. Seja eles retornos da API
 * ou do Banco de Dados Local
 */
public class SerializationInfos {

    private final Context context;
    // Constantes de Possiveis Exception usadads nos Logs
    private final String NAME_CLASS = "SearchInternet";
    private final String EXCEPTION_JSON = "Exception JSON";
    private final String EXCEPTION = "Exception";
    // Caso haja algum erro nos metodos
    private String error_operation;

    /**
     * Contrutor da Classe SerializationInfos com Context
     *
     * @param context Usado para obter string do string.xml
     */
    public SerializationInfos(Context context) {
        this.context = context;
    }

    /**
     * Serializa o JSON recebido da https://brasilapi.com.br, obtendo uma Lista das Cidades de acordo
     * com o Estado
     *
     * @param raw_json JSON resultante da Pesquisa na API (Consultar SearchInternet)
     * @return String[]|null
     */
    public String[] serializationCities(String raw_json) {
        try {
            // A partir da String, obtem um Array JSON e seu tamanho
            JSONArray jsonArray = new JSONArray(raw_json);
            int length_array = jsonArray.length();

            // Variavel que armazenará os resultados e laço de repetição para obte-los
            String[] result_cities = new String[length_array];
            for (int i = 0; i < length_array; i++) {
                // Obtem a Informação Desejada
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                result_cities[i] = jsonObject.getString("nome");
            }

            // Verifica se o Item foi Obtido com Sucesso
            if (result_cities[1] != null && result_cities[length_array - 1] != null) {
                // Retorna a Lista em Ordem Alfabetica
                Arrays.sort(result_cities);
                return result_cities;
            }

        } catch (JSONException ex) {
            Log.e(EXCEPTION_JSON, NAME_CLASS + " - Erro na Formaçao do JSON");
            ex.printStackTrace();
        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Ocorreu uma Exceção");
            ex.printStackTrace();
        }
        error_operation = context.getString(R.string.error_serialization);
        return null;
    }

    public String getError_operation() {
        return error_operation;
    }

}
