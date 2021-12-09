package com.example.goal.views.activities;

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

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerResources;
import com.example.goal.models.Product;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    public static final String PARAM_ID = "id_product";
    public static final String PARAM_IMAGE = "url_image";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_IS_SELLER = "product_seller";
    public static final String PARAM_COLOR = "color";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_PRICE = "price";

    private ManagerDataBase database;
    private boolean isSellerProduct = false;
    private Product product;
    private Button remove_wishes;
    private Button add_wishes;
    private Button update_product;
    private Button remove_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        if (intent != null) {
            product = new Product(ProductActivity.this);
            product.setUrl_image(intent.getStringExtra(PARAM_IMAGE));
            product.setName_product(intent.getStringExtra(PARAM_NAME));
            product.setPrice(intent.getDoubleExtra(PARAM_PRICE, 0));
            product.setId_product(intent.getStringExtra(PARAM_ID));
            isSellerProduct = intent.getBooleanExtra(PARAM_IS_SELLER, false);

            instanceItems();
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
        database = new ManagerDataBase(ProductActivity.this);
    }

    /**
     * Configura a ToolBar Centralizando a Logo no Centro
     */
    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);

        // Adiciona o Icone de "Voltar"
        if (getSupportActionBar() != null) {
            // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Configura a Logo no Centro da ToolBar
        ImageView logo_goal = toolbar.findViewById(R.id.image_logo_goal);

        int margin_for_center = ManagerResources.dpToPixel(ProductActivity.this, 52);
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

        Picasso.get().load(product.getUrl_image()).error(R.drawable.error_image).into(image_product);
        txt_title.setText(product.getName_product());
        txt_price.setText(String.valueOf(product.getPrice()));

        if (isSellerProduct) {
            LinearLayout layout_seller = findViewById(R.id.layout_productSeller);
            layout_seller.setVisibility(View.VISIBLE);

            update_product.setOnClickListener(v -> {
                // TODO REMOVER e atualizar
                Log.e("Debug", "Clicou no UPDATE PRODUCT");
                finish();
            });

            remove_product.setOnClickListener(v -> {
                // TODO REMOVER e atualizar
                Log.e("Debug", "Clicou no EXCLUIR PRODUCT");
                finish();
            });
        }

        // Configura caso o Produto já seja um da Lista de Desejos
        if (database.isWhishes(product.getId_product())) visibleAddWishes(false);
    }

    /**
     * Configura os Botãoes e seus Listeners
     */
    private void setUpButtons() {
        Button details_product = findViewById(R.id.btn_details);
        details_product.setOnClickListener(view ->
                startActivity(new Intent(ProductActivity.this, DetailsActivity.class)));

        Button comments_product = findViewById(R.id.btn_moreComments);
        comments_product.setOnClickListener(view ->
                startActivity(new Intent(ProductActivity.this, CommentsActivity.class)));

        AlertDialogPersonalized alertPersonalized = new AlertDialogPersonalized(ProductActivity.this);

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
}