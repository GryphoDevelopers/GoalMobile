package com.example.goal.models;

import static com.example.goal.managers.ManagerDataBase.DATE_BIRTH;
import static com.example.goal.managers.ManagerDataBase.DOCUMENT_USER;
import static com.example.goal.managers.ManagerDataBase.EMAIL_USER;
import static com.example.goal.managers.ManagerDataBase.ID_USER;
import static com.example.goal.managers.ManagerDataBase.IS_USER_SELLER;
import static com.example.goal.managers.ManagerDataBase.NAME_USER;
import static com.example.goal.managers.ManagerDataBase.NICKNAME_USER;
import static com.example.goal.managers.ManagerDataBase.PASSWORD_USER;
import static com.example.goal.managers.ManagerDataBase.PHONE_USER;
import static com.example.goal.managers.ManagerResources.EXCEPTION;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.SearchInternet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe SerializationInfos: Serializa Dados para exibir para o usuario. Seja eles retornos da API
 * ou do Banco de Dados Local
 */
public class SerializationInfo {

    private final Context context;

    // Constantes de Possiveis Exception usadas nos Logs
    private final String NAME_CLASS = "SerializationInfo";

    // Caso haja algum erro nos metodos
    private String error_operation;

    /**
     * Contrutor da Classe SerializationInfos com Context
     *
     * @param context Usado para obter string do string.xml
     */
    public SerializationInfo(Context context) {
        this.context = context;
    }

    /**
     * Serializa um JSON (recebido pela busca da {@link SearchInternet}) que possui informações em Arrays.
     * A ordem dos resultados dos {@link String} Array será dada na sequencia dos parametros Passados
     *
     * @param raw_json   JSON resultante da Pesquisa na API (Resultande da {@link SearchInternet})
     * @param parameters Parametros que serão recuperados da API
     * @return String[]|null
     * @see SearchInternet
     */
    public List<String[]> jsonArrayToArray(String raw_json, String[] parameters) {
        try {
            // A partir da String, obtem um Array JSON e seu tamanho
            JSONArray jsonArray = new JSONArray(raw_json);
            int length_array = jsonArray.length();

            // Variavel que armazenará os resultados e laço de repetição para obte-los
            List<String[]> result_search = new ArrayList<>();
            for (int i = 0; i < length_array; i++) {
                // Lista que armazenará os Valores do Array em especifico
                List<String> items_array = new ArrayList<>();

                // Obtem as Informações do Array
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                for (String item : parameters) {
                    items_array.add(jsonObject.getString(item));
                }

                // Adiciona ao Array os Itens em um ArrayString
                result_search.add(items_array.toArray(new String[0]));
            }

            // Retorna o Resultado do Array
            if (!result_search.isEmpty()) return result_search;

        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Erro na Serialização do JSON Array - "
                    + ex.getClass().getName());
            ex.printStackTrace();
        }

        error_operation = context.getString(R.string.error_serialization);
        return null;
    }

    /**
     * Serializa um JSON (recebido pela busca da {@link SearchInternet}) que possui informações
     * dos Produtos em Arrays.
     *
     * @param raw_json JSON resultante da Pesquisa na API (Resultande da {@link SearchInternet})
     * @return String[]|null
     * @see SearchInternet
     * @see Product
     */
    public List<Product> serializationProduct(String raw_json) {
        try {
            // A partir da String, obtem um Array JSON e seu tamanho
            JSONArray jsonArray = new JSONArray(raw_json);
            int length_array = jsonArray.length();

            // Variavel que armazenará os resultados e laço de repetição para obte-los
            List<Product> result_search = new ArrayList<>();
            for (int i = 0; i < length_array; i++) {

                // Obtem as Informações do Array
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                Product product = new Product(context);

                product.setId_product(jsonObject.isNull("id") ? "0"
                        : jsonObject.getString("id"));
                product.setName_product(jsonObject.isNull("name") ? ""
                        : jsonObject.getString("name"));
                product.setUrl_image(jsonObject.isNull("image_link") ? ""
                        : jsonObject.getString("image_link"));

                if (!jsonObject.isNull("price")) {
                    String price = String.valueOf(jsonObject.get("price"));
                    product.setPrice(product.validationPrice(price) ? Double.parseDouble(price) : 0);
                }

                if (!jsonObject.isNull("product_colors") &&
                        jsonObject.get("product_colors") instanceof JSONArray) {

                    JSONArray array_colors = (JSONArray) jsonObject.get("product_colors");
                    String[] values_colors = new String[array_colors.length()];

                    for (int u = 0; u < array_colors.length(); u++) {
                        JSONObject json_color = new JSONObject(array_colors.getString(u));

                        if (!json_color.isNull("colour_name"))
                            values_colors[u] = json_color.getString("colour_name");
                    }

                    product.setColors(values_colors);
                }

                // Adiciona ao Array os Itens do Produto
                result_search.add(product);
            }

            // Retorna o Resultado do Array
            if (!result_search.isEmpty()) return result_search;

        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Erro na Serialização do JSON Array - "
                    + ex.getClass().getName());
            ex.printStackTrace();
        }

        error_operation = context.getString(R.string.error_serialization);
        return null;
    }

    /**
     * Serializa um JSON (recebido pela busca da {@link SearchInternet}) que possui uma unica String
     *
     * @param raw_json   JSON resultante da Pesquisa na API
     * @param parameters Parametros que serão recuperados da API
     * @return String[] | null
     * @see SearchInternet
     */
    public String[] jsonStringToArray(String raw_json, String[] parameters) {
        try {
            // Obtem o JSON e Instancia e a String de Resposta
            JSONObject jsonObject = new JSONObject(raw_json);
            List<String> response_api = new ArrayList<>();

            // Obtem os Itens que foram passados no parameters, se existirem
            for (String item_recover : parameters) {
                if (!jsonObject.isNull(item_recover)) {
                    response_api.add(jsonObject.getString(item_recover));
                }
            }

            return response_api.toArray(new String[0]);
        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Erro na Serialização do JSON String - " +
                    ex.getClass().getName());
            ex.printStackTrace();
        }

        error_operation = context.getString(R.string.error_serialization);
        return null;
    }


    /**
     * Obtem dados de um {@link Cursor} e retorna uma Instancia de um Usuario
     *
     * @param cursor Cursor que será serializado
     * @return {@link User}|null
     */
    public User serializationUserDatabase(Cursor cursor) {
        // Verifica se o Cursor está acessivel. Caso esteja, Intanancia a Classe que será usada
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(context);

            // Define um valor fixo caso sejam nulos ou obtem os valores
            user.setId_user(cursor.isNull(cursor.getColumnIndex(ID_USER)) ? ""
                    : cursor.getString(cursor.getColumnIndex(ID_USER)));
            user.setName(cursor.isNull(cursor.getColumnIndex(NAME_USER)) ? ""
                    : cursor.getString(cursor.getColumnIndex(NAME_USER)));
            user.setNickname(cursor.isNull(cursor.getColumnIndex(NICKNAME_USER)) ? ""
                    : cursor.getString(cursor.getColumnIndex(NICKNAME_USER)));
            user.setEmail(cursor.isNull(cursor.getColumnIndex(EMAIL_USER)) ? ""
                    : cursor.getString(cursor.getColumnIndex(EMAIL_USER)));
            user.setPassword(cursor.isNull(cursor.getColumnIndex(PASSWORD_USER)) ? ""
                    : cursor.getString(cursor.getColumnIndex(PASSWORD_USER)));
            user.setPhone(cursor.isNull(cursor.getColumnIndex(PHONE_USER)) ? ""
                    : cursor.getString(cursor.getColumnIndex(PHONE_USER)));
            user.setDate_birth(cursor.isNull(cursor.getColumnIndex(DATE_BIRTH)) ? ""
                    : cursor.getString(cursor.getColumnIndex(DATE_BIRTH)));

            // Verifica se é um Vendedor
            int is_seller = (cursor.isNull(cursor.getColumnIndex(IS_USER_SELLER)) ? ManagerDataBase.FALSE
                    : cursor.getInt(cursor.getColumnIndex(IS_USER_SELLER)));
            user.setSeller(is_seller == ManagerDataBase.TRUE);

            // Configura o CPF e CNPJ
            String document = (cursor.isNull(cursor.getColumnIndex(DOCUMENT_USER)) ? ""
                    : cursor.getString(cursor.getColumnIndex(DOCUMENT_USER)));
            if (document.length() == 11) user.setCpf(document);
            else if (document.length() == 14) user.setCnpj(document);
            else {
                user.setCpf("");
                user.setCnpj("");
            }

            return user;
        } else {
            error_operation = context.getString(R.string.error_generic);
            return null;
        }
    }

    /**
     * Obtem a mensagem de erro de algum Metodo
     */
    public String getError_operation() {
        return error_operation;
    }

}
