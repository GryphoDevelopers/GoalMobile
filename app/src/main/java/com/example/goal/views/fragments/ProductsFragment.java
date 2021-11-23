package com.example.goal.views.fragments;

import static com.example.goal.managers.ManagerResources.isNullOrEmpty;
import static com.example.goal.managers.RecyclerAdapterProducts.POSITION_SMALL_ITEM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private final List<Product> productList;

    // Variaveis para Manipular os Itens
    private String type_fragment;
    private String category_fragment;
    private Context context_fragment;
    private RecyclerAdapterProducts recyclerAdapterProducts;

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

    /**
     * Metodo responsavel por Criar o Fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type_fragment = getArguments().getString(TYPE, "");
            category_fragment = getArguments().getString(CATEGORY, "");
        }
    }

    /**
     * Após ter criado o Fragment, configura os seus Itens
     *
     * @return {@link View} (Fragment Configurado)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instancia um Fragment (View) para ser configurada e usada
        View fragment = inflater.inflate(R.layout.fragment_products, container, false);

        // Obtem o Context do Fragment p/ evitar dar Erro de Hierarquia de Context/View
        context_fragment = fragment.getContext();

        // Configura RecyclerView e seu Titulo
        setUpTitleFragment(fragment);

        // Retorna o Fragment Configurado
        return fragment;
    }

    /**
     * Configura as diferentes formas de cada Tipo de Fragment
     */
    private void setUpTitleFragment(View viewConfigured) {
        if (type_fragment.equals(TYPE_HOME) || type_fragment.equals(TYPE_CATEGORY)) {

            // Evita erros de "Esquece" de passar a Categoria do Fragment
            String title_recyclerView = isNullOrEmpty(category_fragment) ?
                    context_fragment.getString(R.string.titleMenu_categories): category_fragment;

            // Configura o Adapter do RecyclerView
            recyclerAdapterProducts = new RecyclerAdapterProducts(productList,
                    type_fragment.equals(TYPE_CATEGORY), title_recyclerView);

            // Obtem o RecyclerView e Configura o Adapter
            RecyclerView recyclerView = viewConfigured.findViewById(R.id.recycler_products);
            recyclerView.setAdapter(recyclerAdapterProducts);

            // Configura o Layout do RecyclerView
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context_fragment, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // Apenas deixa ocupando 1 Espaço, se for o Item Pequeno
                    return recyclerAdapterProducts.getItemViewType(position) == POSITION_SMALL_ITEM ? 1 : 3;
                }
            });
        } else if (type_fragment.equals(TYPE_OTHERS)) {
            // todo: implementar obtenção das outras categorias
        } else {
            // todo: implementar texto de erro
        }
    }

}