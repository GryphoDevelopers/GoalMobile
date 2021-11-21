package com.example.goal.models;

import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.managers.ManagerResources.getSlideText;
import static com.example.goal.managers.ManagerResources.isNullOrEmpty;
import static com.example.goal.managers.SearchInternet.GET;

import android.content.Context;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.SearchInternet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Classe Produtct: Classe usada para manipular as informações dos Produtos
 */
public class Product {

    // Mensagens de Erro
    private final String MESSAGE_EXCEPTION;
    private final String INPUT_NULL;
    private final String INPUT_NOT_CHARS_ACCEPT;
    private final String INPUT_MIN_LENGTH;
    private final String INPUT_MAX_LENGTH;
    private final String INPUT_WRONG_STOCK;
    private final String INPUT_UNAVAILABLE;

    // Atributos da Classe Produto
    private final Context context;
    private int id_product = 0;
    private String name_product = "";
    private String description_product = "";
    private String url_image = "";
    private int stock_product = 0;
    private double price = 0;

    /**
     * Variavel que armazenará a Mensagem de Erro
     */
    private String error_operation = "";

    /**
     * Instancia da Classe {@link Product}
     *
     * @param context {@link Context} usado na manipulação e obtenção de dados nos metodos
     */
    public Product(Context context) {
        this.context = context;
        MESSAGE_EXCEPTION = context.getString(R.string.error_exception);
        INPUT_NULL = context.getString(R.string.validation_empty);
        INPUT_NOT_CHARS_ACCEPT = context.getString(R.string.validation_format_char);
        INPUT_MIN_LENGTH = context.getString(R.string.validation_min_length);
        INPUT_MAX_LENGTH = context.getString(R.string.validation_max_length);
        INPUT_WRONG_STOCK = context.getString(R.string.validation_stock);
        INPUT_UNAVAILABLE = context.getString(R.string.validation_unavailable);
    }

    /**
     * Valida o Nome do Produto. O nome deve ter entre 5 à 150 Caracteres (Letras (Com acentuação
     * + ç), Numeros, Ponto, Virgula ou Hifen)
     *
     * @param name Nome que será validado
     * @return true|false
     */
    public boolean validationName(String name) {
        if (isNullOrEmpty(name)) {
            error_operation = INPUT_NULL;
            return false;
        } else if (name.length() < 5) {
            error_operation = String.format(INPUT_MIN_LENGTH, "Nome", "5");
            return false;
        } else if (name.length() > 150) {
            error_operation = String.format(INPUT_MAX_LENGTH, "Nome", "150");
            return false;
        } else if (!name.matches("^[A-ZÀ-úà-úa-zçÇ\\s-+.,0-9]*")) {
            error_operation = String.format(INPUT_NOT_CHARS_ACCEPT, "Nome", "Letras, Numeros, Ponto," +
                    " Virgula ou Hifen");
            return false;
        } else return true;
    }

    /**
     * A partir de uma String (para evitar erros de conversão) converte o preço e Valida. O Preço
     * deve ser maior que 3 e menor que 1.000.000.000 reais
     *
     * @param price Preço que será validado
     * @return true|false
     */
    public boolean validationPrice(String price) {
        if (isNullOrEmpty(price)) {
            error_operation = INPUT_NULL;
            return false;
        } else if (!price.matches("^[0-9.]*")) {
            error_operation = String.format(INPUT_NOT_CHARS_ACCEPT, "Preço", "Numeros");
            return false;
        }
        try {
            double value_price = Double.parseDouble(price);
            if (value_price < 3.0) {
                error_operation = String.format(INPUT_WRONG_STOCK, "pelo menos 1");
                return false;
            } else if (value_price > 1000000001.00) {
                error_operation = String.format(INPUT_WRONG_STOCK, "até 1.000.000.000");
                return false;
            } else return true;
        } catch (Exception ex) {
            Log.e(EXCEPTION, "Product - Erro na Conversão do Valor do Preço: " + ex.getClass().getName());
            ex.printStackTrace();
            error_operation = INPUT_UNAVAILABLE;
            return false;
        }
    }

    /**
     * A partir de um String (para evitar erros de conversão) valida a quatidade de Itens do Estoque.
     * O estoque deve ser um numero entre 1 à 1.000.000.000 Itens
     *
     * @param stock Quantidade do Estoque que será validado
     * @return true|false
     */
    public boolean validationStock(String stock) {
        // todo: verificar erro dos testes
        if (isNullOrEmpty(stock)) {
            error_operation = INPUT_NULL;
            return false;
        } else if (!stock.matches("^[0-9\\S]*")) {
            error_operation = String.format(INPUT_NOT_CHARS_ACCEPT, "Preço", "Numeros");
            return false;
        }
        try {
            int value_stock = Integer.parseInt(stock);
            if (value_stock <= 0) {
                error_operation = String.format(INPUT_WRONG_STOCK, "pelo menos 1");
                return false;
            } else if (value_stock > 1000000001) {
                error_operation = String.format(INPUT_WRONG_STOCK, "até 1.000.000.000");
                return false;
            } else return true;
        } catch (Exception ex) {
            Log.e(EXCEPTION, "Product - Erro na Conversão do Valor do Estoque: " + ex.getClass().getName());
            ex.printStackTrace();
            error_operation = INPUT_UNAVAILABLE;
            return false;
        }
    }

    public boolean validationDescription(String description) {
        return false;
    }


    // Getters e Setters
    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getDescription_product() {
        return description_product;
    }

    public void setDescription_product(String description_product) {
        this.description_product = description_product;
    }

    public int getStock_product() {
        return stock_product;
    }

    public void setStock_product(int stock_product) {
        this.stock_product = stock_product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    /**
     * A partir de uma URL, obtem os Produtos da API
     *
     * @param url             URL que será obtida os Produtos
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @return {@link List<Product>}|null
     */
    public List<Product> getProducts_catalog(ExecutorService executorService, String url) {
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
     * Retorna o erro de alguma etapa de validação
     *
     * @return {@link String}|""
     */
    public String getError_operation() {
        return error_operation;
    }

}
