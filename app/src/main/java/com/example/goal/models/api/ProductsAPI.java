package com.example.goal.models.api;

import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.managers.ManagerResources.getSlideText;
import static com.example.goal.managers.ManagerResources.isNullOrEmpty;
import static com.example.goal.managers.SearchInternet.GET;

import android.content.Context;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.SearchInternet;
import com.example.goal.models.Product;
import com.example.goal.models.SerializationInfo;

import java.util.ArrayList;
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
    public List<Product> getCatalog(ExecutorService executorService, String url) {
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
                // Obtem uma List com Arrays Strings dos dados do Produto
                SerializationInfo serializationInfo = new SerializationInfo(context);
                List<String[]> list_makeups = new SerializationInfo(context)
                        .jsonArrayToArray(json_products, new String[]{"name", "image_link"});

                // Instancia uma Lista com instancias da Classe Produto
                if (list_makeups != null) {
                    List<Product> listProduct = new ArrayList<>();
                    for (int i = 0; i < list_makeups.size(); i++) {
                        // Obtem o Nome do Produto
                        String name = list_makeups.get(i)[0];
                        name = isNullOrEmpty(name) ? "Sem Nome" : getSlideText(name, 40, true);

                        // Obtem a URL da Imagem
                        String url_image = list_makeups.get(i)[1];

                        Product productItem = new Product(context);
                        productItem.setName_product(name);
                        productItem.setUrl_image(isNullOrEmpty(url_image) ? "" : url_image);

                        listProduct.add(productItem);
                    }

                    return listProduct;
                } else error_operation = serializationInfo.getError_operation();
            } else error_operation = searchInternet.getError_search();

        } catch (Exception ex) {
            error_operation = MESSAGE_EXCEPTION;
            Log.e(EXCEPTION, "Product" + " - Erro ao Obter os Produtos - " + ex.getClass().getName());
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Se existir, retorna o erro da operação da Classe
     * @return {@link String}|""
     */
    public String getError_operation() {
        return error_operation;
    }

}
