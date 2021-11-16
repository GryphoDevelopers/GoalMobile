package com.example.goal.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goal.R;
import com.example.goal.managers.RecyclerAdapterProducts;
import com.example.goal.models.Product;

import java.util.List;

/**
 * {@link Fragment Fragment} ProductsFragment: Fragment usado para Exibir os Produtos na Tela Index
 * (Catalogo) ou exibir os produtos por categoria.
 */
public class ProductsFragment extends Fragment {

    /**
     * Variavel que define o Fragment Inicio/Catalogo
     */
    public static final String TYPE_HOME = "type_home";

    /**
     * Variavel que define o Fragment Categoria
     */
    public static final String TYPE_CATEGORY = "type_category";

    /**
     * Variavel que define o Fragment das Outras Categorias
     */
    public static final String TYPE_OTHERS = "type_others_category";

    // Chaves usadas no Bundle
    private static final String TYPE = "type_fragment";
    private static final String CATEGORY = "category_fragment";

    // Variaveis para Manipular os Itens
    private String type_fragment;
    private String category_fragment;
    private List<Product> productList;
    private Context context_fragment;

    // Construtor Vazio e Obrigatorio
    public ProductsFragment() {
    }

    /**
     * Construtor que possui uma {@link List} dos {@link Product} que serão exibidos no RecyclerView
     */
    public ProductsFragment(List<Product> productList) {
        this.productList = productList;
    }

    /**
     * Cria uma Instancia do Fragment de Produtos.
     * <p>
     * Esse {@link Fragment Fragment} aceita 2 configurações: Exibir um Catalogo de Produtos ou
     * Exibir Produtos de uma Categoria Especifica
     *
     * @param productList {@link List} com os {@link Product} que serão exibidos
     * @param type        Define o Tipo de {@link Fragment Fragment}: Catalogo ou Categoria
     * @param category    Define a Categoria dos Produtos Buscados. Somente será usado quando o "Type"
     *                    for "TYPE_CATEGORY", caso contrario será ignorado
     * @return Nova instanceia de um {@link Fragment}
     */
    public static ProductsFragment newInstance(String type, String category, List<Product> productList) {
        // Instancia o Fragment
        ProductsFragment fragment = new ProductsFragment(productList);

        // Define os Itens
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(CATEGORY, category);

        // Informa os Arguments para o Fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type_fragment = getArguments().getString(TYPE, "");
            category_fragment = getArguments().getString(CATEGORY, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instancia um Fragment (View) para ser configurada e usada
        View fragment = inflater.inflate(R.layout.fragment_products, container, false);

        // Obtem o Context do Fragment p/ evitar dar Erro de Hierarquia de Context/View
        context_fragment = fragment.getContext();

        // Configura o Titulo do Fragment e o RecyclerView
        setUpTitleFragment(fragment);
        setUpRecyclerView(fragment);

        // Retorna o Fragment Configurado
        return fragment;
    }

    private void setUpTitleFragment(View viewConfigured) {
        // Configura de Formas diferentes para cada Tipo de Fragment
        TextView text_type = viewConfigured.findViewById(R.id.type_fragment);
        String text;

        switch (type_fragment) {
            case TYPE_HOME:
                text = "";
                text_type.setVisibility(View.GONE);
                break;
            case TYPE_CATEGORY:
                text = "Tipo: Categoria" + " - " + category_fragment;
                break;
            case TYPE_OTHERS:
                text = "Outras Categorias";
                break;
            default:
                text = context_fragment.getString(R.string.error_500);
                break;
        }

        text_type.setText(text);
    }

    private void setUpRecyclerView(View viewConfigured) {
        // Configurações do RecyclerView
        RecyclerView recyclerView = viewConfigured.findViewById(R.id.recycler_products);

        // Configura o Adapter
        RecyclerAdapterProducts recyclerAdapterProducts = new RecyclerAdapterProducts(productList);
        recyclerView.setAdapter(recyclerAdapterProducts);

        // Configura o Layout do RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context_fragment, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return recyclerAdapterProducts.isSmallItem(position) ? 1 : 3;
            }
        });
    }
}