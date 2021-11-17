package com.example.goal.managers;

import static com.example.goal.managers.ManagerResources.dpToPixel;
import static com.example.goal.managers.ManagerResources.isNullOrEmpty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goal.R;
import com.example.goal.models.Product;

import java.util.Arrays;
import java.util.List;

public class RecyclerAdapterProducts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Contante com o Valor padrão dos itens que serão Exibidos
     */
    public static final int DEFAULT_ITEMS_QUANTITY = 9;

    /**
     * Constante com o valor da quantidade dos itens que serão carregados inicialmente
     */
    public static final int INITIAL_ITEMS_QUANTITY = (3 * DEFAULT_ITEMS_QUANTITY) + 1;

    /**
     * Valor do Item que fica no Layout Grande
     */
    private static final int POSITION_BIG_ITEM = 0;

    /**
     * Valor que define os itens menores
     */
    private static final int POSITION_SMALL_ITEM = 1;

    /**
     * Valor dos Itens normais
     */
    private static final int POSITION_NORMAL_ITEM = 2;

    /**
     * Valor da Posição do Loading ("Carregando")
     */
    private static final int POSITION_LOADING = 3;

    /**
     * Valor da Posição da Linhja da Goal
     */
    private static final int POSITION_LINE = 4;

    /**
     * Lista que contem os Produtos que serão Exibidos
     */
    private final List<Product> productList;

    /**
     * Lista com os Valores que definem as Posições dos Itens Menores
     */
    private final List<Integer> positions_smallItems = Arrays.asList(1, 2, 3, 7, 8, 9);

    private RecyclerView recyclerView;

    /**
     * Construtor da Classe RecyclerAdapterProducts
     *
     * @param productList Lista dos Produtos que serão exibidos
     */
    public RecyclerAdapterProducts(List<Product> productList) {
        this.productList = productList;
    }

    /**
     * A partir do Tipo da View, Cria e Configura os IDs para manipular os Widgets
     *
     * @param parent   Onde o Layout será exibido
     * @param viewType Tipo da View (Produtos, Loading, Quebra Linha)
     * @return {@link RecyclerView.ViewHolder}
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Faz o Tratamento da Configuração de Cada tipo de view
        switch (viewType) {
            case POSITION_LOADING:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_loading, parent, false));
            case POSITION_LINE:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.line_gol, parent, false));
            default:
                return new ItemsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_recycler_products, parent, false));
        }
    }

    /**
     * Configura os elementos que serão exibidos
     *
     * @param holder   Configuração e Instancia (IDs) dos itens que serão utilizados
     * @param position Posição do Item no RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Verifica de qual classe vem a Instancia do ViewHolder
        if (holder instanceof ItemsViewHolder) {
            // Obtem os Dados do Produto na Posição especifica do Cursor
            Product productItem = productList.get(position);

            // Configura os Diferentes tipos de Itens
            if (isSmallItem(position)) {
                ((ItemsViewHolder) holder).image_product.setMaxWidth(120);
                ((ItemsViewHolder) holder).image_product.setMaxHeight(120);
                //     ((ItemsViewHolder) holder).txt_nameProduct.setVisibility(View.GONE);
                ((ItemsViewHolder) holder).txt_nameProduct.setText(productItem.getName_product());
            } else {
                // Coloca o Texto nos Itens Grandes ou Medios
                ((ItemsViewHolder) holder).txt_nameProduct.setText(productItem.getName_product());

                // Configurações Adicionais para os Itens Maiores
                if (isBigItem(position)) {
                    ((ItemsViewHolder) holder).image_product.setMaxHeight(300);
                    ((ItemsViewHolder) holder).image_product.setMinimumHeight(300);
                    ((ItemsViewHolder) holder).txt_nameProduct.setText(productItem.getName_product());
                }
            }
        } else if (holder instanceof EmptyViewHolder && getItemViewType(position) == POSITION_LINE) {
            // Obtem os parametros do Layout
            GridLayoutManager.LayoutParams params =
                    (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();

            // Obtem o valor convertido de DP para Pixels e Insere no Layout
            int margin_top_pixel = dpToPixel(holder.itemView.getContext(), 16);
            params.setMargins(0, margin_top_pixel, 0, 0);
            holder.itemView.setLayoutParams(params);

            if (!isNullOrEmpty(productList.get(position).getName_product())) {
                productList.add(position, new Product(holder.itemView.getContext()));
                recyclerView.post(() -> notifyItemChanged((position) + 1));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    /**
     * Retorna a Quantidade de Itens que a Lista Possui
     *
     * @return int
     */
    @Override
    public int getItemCount() {
        return productList == null || productList.isEmpty() ? 0 : productList.size();
    }

    /**
     * Retorna qual o Tipo da View (Item, Quebra Linha, Loading)
     */
    @Override
    public int getItemViewType(int position) {
        if (isBigItem(position)) return POSITION_BIG_ITEM;
        else if (isLineGoal(position)) return POSITION_LINE;
        else if (isLoading(position)) return POSITION_LOADING;
        else if (isSmallItem(position)) return POSITION_SMALL_ITEM;
        return POSITION_NORMAL_ITEM;
    }

    /**
     * Caso a Posição seja a Primeira, exibirá o Item no Layout maior (Destaque)
     *
     * @return true|false
     */
    public boolean isBigItem(int position) {
        return position == 0;
    }

    /**
     * Caso a Posição seja divisivel por 10, será adicionado uma quebra linha
     *
     * @return true|false
     */
    public boolean isLineGoal(int position) {
        return position % 10 == 0;
    }

    /**
     * Quando o usuario chegar no ultimo item da lista, será adicionado um item nulo que define a
     * exibição da view Loading ("carregando....");
     *
     * @return true|false
     */
    public boolean isLoading(int position) {
        if (position < INITIAL_ITEMS_QUANTITY) return false;
        return productList.get(position) == null;
    }

    /**
     * Verifica se a Posição após ser divisivel por 10, resulta em uma das posições que definem os
     * Itens Menores
     *
     * @return true|false
     * @see #positions_smallItems
     */
    public boolean isSmallItem(int position) {
        // Obtem o Resto da Divisão por 10 (Obs: Sempre será um numero entre 0 a 9)
        int rest_division = position > 10 ? position % 10 : position;

        // As possiveis posições dos Itens Menores são 1,2,3,7,8,9
        return positions_smallItems.contains(rest_division);
    }


    // Classes que irão recuperar o Valor dos Layout Utilizados

    /**
     * Classe que retorna os Widgets  que serão usados, com suas referencias (IDs) e instancias
     */
    protected static class ItemsViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt_nameProduct;
        private final ImageView image_product;

        /**
         * Obtem uma Instancia dos Itens da View Passada
         *
         * @param itemView View que contem os elementos que serão recuperados
         */
        protected ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nameProduct = itemView.findViewById(R.id.txt_nameProduct);
            image_product = itemView.findViewById(R.id.img_product);
        }
    }

    /**
     * Classe que retorna o Layout que não demanda configurações, como o Loading e a Linha da Goal
     */
    protected static class EmptyViewHolder extends RecyclerView.ViewHolder {

        /**
         * Retorna a View Passada, ja que ela não demanada configuração ou recuperação de valores
         *
         * @param itemView View que contem os elementos que serão exibidos
         */
        protected EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
