package com.example.goal.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.goal.R;

/**
 * {@link Fragment Fragment} ProductsFragment: Fragment usado para Exibir os Produtos na Tela Index
 * (Catalogo) ou exibir os produtos por categoria.
 */
public class ProductsFragment extends Fragment {

    // Constantes Usadas para definir o Tipo do Fragment
    public static final String TYPE_HOME = "type_home";
    public static final String TYPE_CATEGORY = "type_category";
    public static final String TYPE_OTHERS = "type_others_category";
    private static final String TYPE = "type_fragment";
    private static final String CATEGORY = "category_fragment";

    // Variaveis para Manipular os Itens
    private String type_fragment;
    private String category_fragment;

    // Construtor Vazio e Obrigatorio
    public ProductsFragment() {
    }

    /**
     * Cria uma Instancia do Fragment de Produtos.
     * <p>
     * Esse {@link Fragment Fragment} aceita 2 configurações: Exibir um Catalogo de Produtos ou
     * Exibir Produtos de uma Categoria Especifica
     *
     * @param type     Define o Tipo de {@link Fragment Fragment}: Catalogo ou Categoria
     * @param category Define a Categoria dos Produtos Buscados. Valor somente utilizado quando o
     *                 "Type" for "PRODUCTS_CATEGORY", caso Contrario, será ignorado
     * @return A new instance of fragment ProductsFragment.
     */
    public static ProductsFragment newInstance(String type, String category) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type_fragment = getArguments().getString(TYPE);
            category_fragment = getArguments().getString(CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Instancia um Fragment (View) para ser configurada e usada
        View fragment = inflater.inflate(R.layout.fragment_products, container, false);

        // Obtem o Context do Fragment p/ evitar dar Erro de Hierarquia de Context/View
        Context context = fragment.getContext();
        TextView text_type = fragment.findViewById(R.id.type_fragment);

        // Configura de Formas diferentes para cada Tipo de Fragment
        String text;
        switch (type_fragment) {
            case TYPE_HOME:
                text = "Tipo: " + context.getString(R.string.option_home);
                break;
            case TYPE_CATEGORY:
                text = "Tipo: Categoria" + " - " + category_fragment;
                break;
            case TYPE_OTHERS:
                text = "Outras Categorias";
                break;
            default:
                text = context.getString(R.string.error_500);
                break;
        }
        text_type.setText(text);

        // Retorna o Fragment Configurado
        return fragment;
    }
}