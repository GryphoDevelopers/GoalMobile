package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.managers.ManagerResources.dpToPixel;
import static com.example.goal.managers.ManagerResources.isNullOrEmpty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerResources;
import com.example.goal.models.Product;
import com.example.goal.views.fragments.CatalogFragment;
import com.example.goal.views.fragments.ProductFragment;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.SquareText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    public static final String PARAM_ID = "id_product";
    public static final String PARAM_IMAGE = "url_image";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_IS_SELLER = "product_seller";
    public static final String PARAM_COLOR = "color";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_CATEGORY = "category";
    public static final String PARAM_PRICE = "price";
    public static final String PARAM_STOCK = "stock";

    private static final int POSITION_LIST_COLOR = 0;
    private static final int POSITION_LIST_SIZE = 1;

    private ManagerDataBase database;
    private boolean isSellerProduct = false;
    private List<String[]> attr_product;
    private Product product;
    private Button remove_wishes;
    private Button add_wishes;
    private Button update_product;
    private Button remove_product;
    private Context context;
    private SquareText[] square_colors;
    private SquareText[] square_sizes;
    private String selected_color;
    private String selected_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        if (intent != null) {
            instanceItems();

            product = new Product(context);
            product.setUrl_image(intent.getStringExtra(PARAM_IMAGE));
            product.setName_product(intent.getStringExtra(PARAM_NAME));
            product.setPrice(intent.getDoubleExtra(PARAM_PRICE, 0));
            product.setId_product(intent.getStringExtra(PARAM_ID));
            product.setCategory_product(intent.getStringExtra(PARAM_CATEGORY));
            product.setStock_product(intent.getIntExtra(PARAM_STOCK, 0));
            isSellerProduct = intent.getBooleanExtra(PARAM_IS_SELLER, false);

            // Obtem e Configura a Visibilidade dos Campos de Cor e Tamanho
            attr_product.add(intent.getStringArrayExtra(PARAM_COLOR));
            attr_product.add(intent.getStringArrayExtra(PARAM_SIZE));
            if (attr_product.get(POSITION_LIST_COLOR) == null) {
                TextView text_color = findViewById(R.id.txt_colors);
                GridLayout gridLayout = findViewById(R.id.layoutGrid_colors);
                text_color.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
            }
            if (attr_product.get(POSITION_LIST_SIZE) == null) {
                TextView text_size = findViewById(R.id.txt_size);
                GridLayout gridLayout = findViewById(R.id.layoutGrid_size);
                text_size.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
            }

            setUpToolBar();
            setUpElements();
            setUpButtons();
        }
    }

    /**
     * Instancia os Itens que serão utilizados na Activity
     */
    private void instanceItems() {
        remove_wishes = findViewById(R.id.btn_removeWishes);
        add_wishes = findViewById(R.id.btn_listWishes);
        update_product = findViewById(R.id.btn_updateProduct);
        remove_product = findViewById(R.id.btn_deleteProduct);
        context = ProductActivity.this;
        database = new ManagerDataBase(context);
        attr_product = new ArrayList<>();
    }

    /**
     * Configura a ToolBar Centralizando a Logo no Centro
     */
    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);

        // Adiciona o Icone de "Voltar"
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Configura a Logo no Centro da ToolBar
        ImageView logo_goal = toolbar.findViewById(R.id.image_logo_goal);

        int margin_for_center = ManagerResources.dpToPixel(context, 52);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, margin_for_center, 0);

        logo_goal.setLayoutParams(layoutParams);
    }

    /**
     * Configura os Widgets que serão exibidos na Tela
     */
    private void setUpElements() {
        ImageView image_product = findViewById(R.id.image_product);
        TextView txt_title = findViewById(R.id.txt_name);
        TextView txt_price = findViewById(R.id.txt_price);
        TextView txt_category = findViewById(R.id.txt_category);
        TextView txt_stock = findViewById(R.id.txt_stock);

        if (isNullOrEmpty(product.getUrl_image())) image_product.setImageResource(R.drawable.error_image);
        else Picasso.get().load(product.getUrl_image()).error(R.drawable.error_image).into(image_product);

        txt_title.setText(product.getName_product());
        txt_price.setText(getString(R.string.text_price, String.valueOf(product.getPrice())));
        txt_category.setText(getString(R.string.text_category, product.getCategory_product()));
        txt_stock.setText(getString(R.string.text_stock, String.valueOf(product.getStock_product())));

        if (isSellerProduct) {
            LinearLayout layout_seller = findViewById(R.id.layout_productSeller);
            layout_seller.setVisibility(View.VISIBLE);

            update_product.setOnClickListener(v -> {
                Intent intent_index = new Intent(context, IndexActivity.class);
                intent_index.putExtra(IndexActivity.TYPE_PRODUCT_FRAGMENT,
                        ProductFragment.TYPE_UPDATE);
                intent_index.putExtra(IndexActivity.ID_PRODUCT, product.getId_product());
                finish();
                startActivity(intent_index);
            });

            remove_product.setOnClickListener(v -> {
                Intent intent_index = new Intent(context, IndexActivity.class);
                intent_index.putExtra(IndexActivity.TYPE_PRODUCT_FRAGMENT,
                        ProductFragment.TYPE_DELETE);
                intent_index.putExtra(IndexActivity.ID_PRODUCT, product.getId_product());
                finish();
                startActivity(intent_index);
            });

            add_wishes.setVisibility(View.GONE);
            remove_wishes.setVisibility(View.GONE);
        } else {
            // Configura caso o Produto já seja um da Lista de Desejos
            if (database.isWishes(product.getId_product())) visibleAddWishes(false);
        }

        // Configura os Layout da CustomView do Produto
        GridLayout[] gridLayouts = new GridLayout[]{findViewById(R.id.layoutGrid_colors),
                findViewById(R.id.layoutGrid_size)};

        if (attr_product.get(POSITION_LIST_COLOR) != null) {
            square_colors = new SquareText[attr_product.get(POSITION_LIST_COLOR).length];
        }
        if (attr_product.get(POSITION_LIST_SIZE) != null) {
            square_sizes = new SquareText[attr_product.get(POSITION_LIST_SIZE).length];
        }

        for (int i = 0; i < gridLayouts.length; i++) {
            if (attr_product.get(i) != null && gridLayouts[i] != null)
                setAttrProduct(gridLayouts[i], attr_product.get(i), i);
        }
    }

    /**
     * Configura os Botãoes e seus Listeners
     */
    private void setUpButtons() {
        AlertDialogPersonalized alertPersonalized = new AlertDialogPersonalized(context);

        remove_wishes.setOnClickListener(v -> {
            if (!database.removeWishes(product.getId_product())) {
                alertPersonalized.defaultDialog(getString(R.string.title_input_invalid, "Produto"),
                        getString(R.string.error_generic)).show();
            } else visibleAddWishes(true);
        });

        add_wishes.setOnClickListener(v -> {
            if (!database.insertWishes(product.getId_product())) {
                alertPersonalized.defaultDialog(getString(R.string.title_input_invalid, "Produto"),
                        getString(R.string.error_generic)).show();
            } else visibleAddWishes(false);
        });
    }

    /**
     * Define se os Botões "Adicionar"/"Remover" Favorito estará visivel ou não
     *
     * @param isVisible Define se exibirá o Botaõ "adicionar Desejo"
     */
    private void visibleAddWishes(boolean isVisible) {
        int visibleAdd = isVisible ? View.VISIBLE : View.GONE;
        int visibleRemove = isVisible ? View.GONE : View.VISIBLE;

        add_wishes.setVisibility(visibleAdd);
        remove_wishes.setVisibility(visibleRemove);
    }

    /**
     * Insere os Elementos da Custom View {@link SquareText} nos {@link GridLayout}
     * <p>
     * * Ambos os Atributos não podem ser Nulos
     *
     * @param gridLayout {@link GridLayout} que será inserido a CustomView
     * @param items      String Array que contem os elementos que serão adicionados
     *                   <p>
     */
    private void setAttrProduct(GridLayout gridLayout, String[] items, int postion_list) {
        try{
            if (items != null && gridLayout != null) {
                // Definem o Tamanho da Linha e Coluna
                gridLayout.setRowCount(2);
                gridLayout.setColumnCount(items.length % 2 == 0 ? items.length / 2 : (items.length + 1) / 2);

                for (int i = 0; i < items.length; i++) {
                    // Instancia a Classe da CustomVIew com os Atributos Padrões
                    SquareText squareText = new SquareText(context, items[i], 0, 0, 0);

                    // Configura a Posição do Item na Coluna e os Parametros do layout
                    int column = i % 2 == 0 ? 0 : 1;

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(gridLayout.getLayoutParams());
                    params.columnSpec = GridLayout.spec(column, 1, GridLayout.FILL, 1f);
                    params.width = dpToPixel(context, 20);
                    params.height = dpToPixel(context, 40);
                    params.setMargins(0, 0, dpToPixel(context, 5), column == 1 ? dpToPixel(context, 5) : 0);
                    squareText.setLayoutParams(params);

                    // Adiciona à um Array que controlará as CustomViews
                    if (postion_list == POSITION_LIST_COLOR) square_colors[i] = squareText;
                    else if (postion_list == POSITION_LIST_SIZE) square_sizes[i] = squareText;

                    // Controla os Cliques na Custom View
                    squareText.setOnClickListener(v -> {
                        for (SquareText custom_item : postion_list == POSITION_LIST_COLOR ? square_colors : square_sizes) {
                            if (custom_item.isSelectedSquareText()) custom_item.clickSquareText();
                        }
                        squareText.clickSquareText();

                        if (postion_list == POSITION_LIST_COLOR)
                            selected_color = squareText.getTextTitle();
                        else if (postion_list == POSITION_LIST_SIZE)
                            selected_size = squareText.getTextTitle();
                    });

                    // Adiciona a View no GridLayout
                    gridLayout.addView(squareText);
                }
            }
        }
        catch (Exception ex){
            Log.e(EXCEPTION, "ProductActivity" + " - Erro ao Adicionar as CustomViews - " + ex.getClass().getName());
            ex.printStackTrace();
        }
    }

    /**
     * Metodo Responsavel pelo Clique de Retornar da Tela
     */
    @Override
    public void onBackPressed() {
        if (isSellerProduct) {
            Intent intent_index = new Intent(context, IndexActivity.class);
            intent_index.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent_index.putExtra(IndexActivity.TYPE_CATALOG_FRAGMENT,
                    CatalogFragment.TYPE_SELLER_PRODUCTS);
            startActivity(intent_index);
            finish();
        } else super.onBackPressed();
    }
}