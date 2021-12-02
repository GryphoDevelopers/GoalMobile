package com.example.goal.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goal.R;
import com.example.goal.models.Product;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    public static final String PARAM_IMAGE = "url_image";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_IS_SELLER = "product_seller";
    public static final String PARAM_COLOR = "color";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_PRICE = "price";

    private boolean isSellerProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        if (intent != null) {
            Product product = new Product(ProductActivity.this);
            product.setUrl_image(intent.getStringExtra(PARAM_IMAGE));
            product.setName_product(intent.getStringExtra(PARAM_NAME));
            product.setPrice(intent.getDoubleExtra(PARAM_PRICE, 0));
            
            setUpElements(product);
            setUpButtons();
        }
    }

    private void setUpButtons() {
        Button details_product = findViewById(R.id.btn_details);
        details_product.setOnClickListener(view ->
                startActivity(new Intent(ProductActivity.this, DetailsActivity.class)));

        Button comments_product = findViewById(R.id.btn_moreComments);
        comments_product.setOnClickListener(view ->
                startActivity(new Intent(ProductActivity.this, CommentsActivity.class)));
    }

    /**
     * Configura os Widgets que serão exibidos na Tela
     */
    private void setUpElements(Product product) {
        ImageView image_product = findViewById(R.id.image_product);
        TextView txt_title = findViewById(R.id.txt_name);
        TextView txt_price = findViewById(R.id.txt_price);

        Picasso.get().load(product.getUrl_image()).error(R.drawable.error_image).into(image_product);
        txt_title.setText(product.getName_product());
        txt_price.setText(String.valueOf(product.getPrice()));

    }
}