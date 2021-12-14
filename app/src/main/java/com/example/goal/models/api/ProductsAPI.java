package com.example.goal.models.api;

import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.managers.SearchInternet.GET;
import static com.example.goal.managers.SearchInternet.URL_PRODUCTS;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.SearchInternet;
import com.example.goal.models.Product;
import com.example.goal.models.SerializationInfo;
import com.example.goal.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Classe ProductsAPI: Classe responsavel pelas buscas na API dos Produtos
 */
public class ProductsAPI {

    private final Context context;

    // Constantes de Erros
    private final String MESSAGE_EXCEPTION;
    private String error_operation = "";

    /**
     * Construtor que recebe um {@link Context} para utilizar nos metodos da Classe
     */
    public ProductsAPI(Context context) {
        this.context = context;
        MESSAGE_EXCEPTION = context.getString(R.string.error_exception);
    }

    /**
     * A partir de uma URL, obtem os Produtos da API
     *
     * @param url             URL que será obtida os Produtos
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @return {@link List < Product >}|null
     */
    public List<Product> getProducts(ExecutorService executorService, String url) {
        try {
            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            SearchInternet searchInternet = new SearchInternet(context);
            callable.add(() -> searchInternet.SearchInAPI(url, GET, null));

            // Obtem o Resultado da Busca Assincrona
            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_products = futureTasksList.get(0).get();

            if (json_products != null) {
                // Obtem uma List com os Produtos Serializados
                SerializationInfo serializationInfo = new SerializationInfo(context);
                List<Product> productList = new SerializationInfo(context).serializationProduct(json_products);

                // Instancia uma Lista com instancias da Classe Produto
                if (productList != null) return productList;
                else error_operation = serializationInfo.getError_operation();
            } else error_operation = searchInternet.getError_search();

        } catch (Exception ex) {
            error_operation = MESSAGE_EXCEPTION;
            Log.e(EXCEPTION, "Product" + " - Erro ao Obter os Produtos - " + ex.getClass().getName());
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Obtem uma Lista dos {@link Product} de um {@link User}
     *
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @param token           Token do Vendedor
     * @param user            Dados do vendedor que serão utilizados
     * @return {@link List<Product> List< Product>}|null
     */
    public List<Product> getSellerProducts(ExecutorService executorService, String token, User user) {
        try {
            // todo: obter dados da api goal
            Uri uri = Uri.parse(URL_PRODUCTS).buildUpon().build();
            //          .appendQueryParameter("rating_greater_than", "4.0").build();
            return getProducts(executorService, uri.toString());

        } catch (Exception ex) {
            error_operation = MESSAGE_EXCEPTION;
            Log.e(EXCEPTION, "Product" + " - Erro ao Obter os Produtos - " + ex.getClass().getName());
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Se existir, retorna o erro da operação da Classe
     *
     * @return {@link String}|""
     */
    public String getError_operation() {
        return error_operation;
    }

}
