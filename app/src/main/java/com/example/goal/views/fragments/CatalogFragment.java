package com.example.goal.views.fragments;

import static com.example.goal.managers.ManagerResources.isNullOrEmpty;
import static com.example.goal.managers.RecyclerAdapterProducts.DEFAULT_ITEMS_QUANTITY;
import static com.example.goal.managers.RecyclerAdapterProducts.INITIAL_ITEMS_QUANTITY;
import static com.example.goal.managers.RecyclerAdapterProducts.POSITION_SMALL_ITEM;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goal.R;
import com.example.goal.managers.ClickProducts;
import com.example.goal.managers.RecyclerAdapterProducts;
import com.example.goal.models.Product;
import com.example.goal.views.activities.ProductActivity;
import com.example.goal.views.widgets.AlertDialogPersonalized;

import java.util.List;

/**
 * {@link Fragment Fragment} ProductsFragment: Fragment usado para Exibir os Produtos na Tela Index
 * (Catalogo) ou exibir os produtos por categoria.
 */
public class CatalogFragment extends Fragment implements ClickProducts {

    /**
     * Variavel que define o Fragment Inicio/Catalogo
     */
    public static final String TYPE_HOME = "type_home";

    /**
     * Variavel que define o Fragment dos Produtos do Vendedor
     */
    public static final String TYPE_SELLER_PRODUCTS = "type_seller_product";

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

    /**
     * Lista com Todos os Produtos Carregados. N??o ocorrer?? modifica????es nessa lista
     */
    private final List<Product> productList;

    /**
     * Lista com os Produtos j?? Carregados e Exibidos no {@link RecyclerView}
     *
     * @see #loadMoreItems(RecyclerView)
     */
    private List<Product> productList_loaded;

    // Variaveis para Manipular os Itens
    private String type_fragment;
    private String category_fragment;
    private Context context_fragment;
    private RecyclerAdapterProducts recyclerAdapterProducts;

    // Controla se est?? carregando ou n??o os Itens
    private boolean is_loading = false;

    /**
     * Construtor que possui uma {@link List} dos {@link Product} que ser??o exibidos no RecyclerView
     */
    public CatalogFragment(List<Product> productList) {
        this.productList = productList;
    }

    /**
     * Cria uma Instancia do Fragment de Produtos.
     * <p>
     * Esse {@link Fragment Fragment} aceita 2 configura????es: Exibir um Catalogo de Produtos ou
     * Exibir Produtos de uma Categoria Especifica
     *
     * @param productList {@link List} com os {@link Product} que ser??o exibidos
     * @param type        Define o Tipo de {@link Fragment Fragment}: Catalogo ou Categoria
     * @param category    Define a Categoria dos Produtos Buscados. Somente ser?? usado quando o "Type"
     *                    for "TYPE_CATEGORY", caso contrario ser?? ignorado
     * @return Nova instanceia de um {@link Fragment}
     */
    public static CatalogFragment newInstance(String type, String category, List<Product> productList) {
        // Instancia o Fragment
        CatalogFragment fragment = new CatalogFragment(productList);

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
     * Ap??s ter criado o Fragment, configura os seus Itens
     *
     * @return {@link View} (Fragment Configurado)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instancia um Fragment (View) para ser configurada e usada
        View fragment = inflater.inflate(R.layout.fragment_catalog, container, false);

        // Obtem o Context do Fragment p/ evitar dar Erro de Hierarquia de Context/View
        context_fragment = fragment.getContext();

        // Configura RecyclerView e seu Titulo
        if (productList != null && !productList.isEmpty()) setUpFragment(fragment);

        // Retorna o Fragment Configurado
        return fragment;
    }

    /**
     * Configura as diferentes formas de cada Tipo de Fragment
     */
    private void setUpFragment(View viewConfigured) {
        if (type_fragment.equals(TYPE_HOME) || type_fragment.equals(TYPE_CATEGORY)
                || type_fragment.equals(TYPE_SELLER_PRODUCTS)) {

            // Obtem uma Lista com a Quatidade Inicial de Itens no RecyclerView
            productList_loaded = productList.subList(0, INITIAL_ITEMS_QUANTITY);

            boolean hasTitle = type_fragment.equals(TYPE_CATEGORY) ||
                    type_fragment.equals(TYPE_SELLER_PRODUCTS);

            // Evita erros de "Esquece" de passar a Categoria do Fragment
            String title_recyclerView = getString(R.string.titleMenu_categories);
            if (hasTitle) {
                title_recyclerView = type_fragment.equals(TYPE_SELLER_PRODUCTS)
                        ? getString(R.string.title_sellerProducts)
                        : !isNullOrEmpty(category_fragment) ? category_fragment : title_recyclerView;
            }

            // Configura o Adapter do RecyclerView e Obtem o Tamanho da Lista
            recyclerAdapterProducts = new RecyclerAdapterProducts(productList_loaded, hasTitle,
                    title_recyclerView, this);

            // Obtem o RecyclerView e Configura o Adapter
            RecyclerView recyclerView = viewConfigured.findViewById(R.id.recycler_products);
            recyclerView.setAdapter(recyclerAdapterProducts);

            // Configura o Layout do RecyclerView
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context_fragment, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // Apenas deixa ocupando 1 Espa??o, se for o Item Pequeno
                    return recyclerAdapterProducts.getItemViewType(position) == POSITION_SMALL_ITEM ? 1 : 3;
                }
            });

            // Configura o Scroll do RecyclerView
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    // Est?? abaixando (dy > 0 = Abaixar da Tela) e n??o est?? tendo carregamento na Tela
                    if (dy > 0 && !is_loading) {
                        // Obtem o Controlador do Layout e o Tamanho da Lista dos Itens
                        LinearLayoutManager layoutRecycler = (LinearLayoutManager) recyclerView
                                .getLayoutManager();

                        // Obtem o Tamanho Final da Lista carregada do RecyclerView
                        int last_position_loaded = recyclerAdapterProducts.getLastPositionList();

                        if (layoutRecycler != null &&
                                layoutRecycler.findLastCompletelyVisibleItemPosition() == last_position_loaded) {

                            // Ultimo Item a Mostra = Obtem a Lista Carregada para Adicionar os Itens
                            is_loading = true;
                            productList_loaded = recyclerAdapterProducts.productList;
                            loadMoreItems(recyclerView);
                        }
                    }
                }
            });

        } else if (type_fragment.equals(TYPE_OTHERS)) {
            // todo: implementar obten????o das outras categorias
        } else {
            // todo: implementar texto de erro
        }
    }

    /**
     * Adiciona a nova quantidade de Itens ao RecyclerView
     */
    private void loadMoreItems(RecyclerView recyclerView) {
        // Adiciona um Item Nulo (Loading)
        productList_loaded.add(null);
        int last_position = recyclerAdapterProducts.getLastPositionList();
        recyclerView.post(() -> {
            // Notifica o RecyclerView que foi adicionado um Item Nullo e joga a Tela para baixo
            recyclerAdapterProducts.notifyItemInserted(last_position);
            recyclerView.scrollToPosition(last_position);
        });

        // Realiza as Opera????es em Background, dando uma pausa de 2 segundos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            int last_positon_list = recyclerAdapterProducts.getLastPositionList();
            // Remove o Loading (Ultimo Item) da Lista e Notifica o RecyclerView
            productList_loaded.remove(last_positon_list);
            recyclerAdapterProducts.notifyItemRemoved(last_positon_list);
            last_positon_list = recyclerAdapterProducts.getLastPositionList();

            // Define um novo tamanho da Lista com os novos itens
            int new_position_list = last_positon_list + DEFAULT_ITEMS_QUANTITY;

            // Insere na Lista e Notifica o RecyclerView das Inser????es
            for (int i = last_positon_list; i <= new_position_list; i++) {
                // todo Fix: Est?? sendo adicionado o Item na Lista com todos os Produtos
                productList_loaded.add(productList.get(i));
                recyclerAdapterProducts.notifyItemInserted(recyclerAdapterProducts.getLastPositionList());
            }

            is_loading = false;
        }, 2000);
    }

    // Metodos Implementados do Clique nos Produtos do RecyclerView
    // todo implementar documenta????o e metodos
    @Override
    public void clickProduct(Product product) {
        if (product != null) {
            Intent intentProduct = new Intent(context_fragment, ProductActivity.class);
            intentProduct.putExtra(ProductActivity.PARAM_IMAGE, product.getUrl_image());
            intentProduct.putExtra(ProductActivity.PARAM_ID, String.valueOf(product.getId_product()));
            intentProduct.putExtra(ProductActivity.PARAM_NAME, product.getName_product());
            intentProduct.putExtra(ProductActivity.PARAM_PRICE, product.getPrice());
            intentProduct.putExtra(ProductActivity.PARAM_IS_SELLER, type_fragment.equals(TYPE_SELLER_PRODUCTS));
            /*intentProduct.putExtra(ProductActivity.PARAM_COLOR);
            intentProduct.putExtra(ProductActivity.PARAM_SIZE);*/
            startActivity(intentProduct);
        } else {
            String title = context_fragment.getString(R.string.title_input_invalid, "Produto");
            new AlertDialogPersonalized(context_fragment).defaultDialog(title,
                    context_fragment.getString(R.string.error_product)).show();
        }
    }

    @Override
    public void deleteProduct(Product product) {

    }

    @Override
    public void updateProduct(Product product) {

    }
}