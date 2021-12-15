package com.example.goal.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.goal.R;
import com.example.goal.models.Product;
import com.example.goal.models.User;
import com.example.goal.models.api.ProductsAPI;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link Fragment Fragment} ProductsFragment: Fragment usado para Exibir os Metodos dos Produtos
 * (Cadastrar/Alterar) para os Vendedores
 */
public class ProductFragment extends Fragment {

    /**
     * Define o Fragment de Criar/Cadastrar um Produto
     */
    public static final String TYPE_CREATE = "create_product";
    /**
     * Define o Fragment de Atualizar um Produto
     */
    public static final String TYPE_UPDATE = "update_product";
    /**
     * Define o Fragment de Excluir um Produto
     */
    public static final String TYPE_DELETE = "delete_product";

    // Chave que armazenará o Valor
    private static final String ARG_TYPE = "type_fragment";

    // Variaveis Usadas
    private Product product;
    private User user;
    private String parameter_type;
    private String text_title;
    private String text_button;
    private TextView title_fragment;
    private TextInputEditText edit_name;
    private TextInputEditText edit_category;
    private TextInputEditText edit_price;
    private TextInputEditText edit_size;
    private TextInputEditText edit_color;

    /**
     * Construtor da Classe {@link ProductFragment} Vazio (Não possui Atributos Necessarios)
     */
    public ProductFragment() {
    }

    /**
     * Cosntrutor da Classe {@link ProductFragment} que recebe um Produto que será Atualizado ou
     * Excluido
     */
    public ProductFragment(Product product, User user) {
        this.product = product;
        this.user = user;
    }

    /**
     * Cria uma Nova Instancia do Fragment {@link ProductFragment}, que possui os Parametros usados
     * na Configuraçõa
     *
     * @param type Tipo do Fragment ({@link #TYPE_CREATE}|{@link #TYPE_UPDATE}|{@link #TYPE_DELETE})
     */
    public static ProductFragment newInstance(String type, Product product, User user) {

        // Instancia a Classe do Fragment de Acordo com o Tipo
        ProductFragment fragment;
        if (product != null) fragment = new ProductFragment(product, user);
        else fragment = new ProductFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Cria o Fragment, obtendo os Paramentros Configurados
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            // Configura Cada Tipo de Fragment
            parameter_type = getArguments().getString(ARG_TYPE);
            switch (parameter_type) {
                case TYPE_CREATE:
                    text_title = getString(R.string.title_product, getString(R.string.text_register));
                    text_button = getString(R.string.option_registerProduct);
                    return;
                case TYPE_UPDATE:
                    text_title = getString(R.string.title_product, getString(R.string.text_update));
                    text_button = getString(R.string.option_updateProduct);
                    return;
                case TYPE_DELETE:
                    text_title = getString(R.string.title_product, getString(R.string.text_delete));
                    text_button = getString(R.string.option_deleteProduct);
                    return;
            }
        }
        // todo: adicionar activity de erro
    }

    /**
     * Configura os Widgets da View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_product, container, false);

        // Configura os Widgets
        title_fragment = fragment.findViewById(R.id.title_fragment_product);
        edit_name = fragment.findViewById(R.id.edit_nameProduct);
        edit_category = fragment.findViewById(R.id.edit_categoryProduct);
        edit_price = fragment.findViewById(R.id.edit_priceProduct);
        edit_size = fragment.findViewById(R.id.edit_sizeProduct);
        edit_color = fragment.findViewById(R.id.edit_colorProduct);
        ImageView imageView = fragment.findViewById(R.id.image_fragmentProduct);
        imageView.setImageResource(R.drawable.error_image);

        if (parameter_type.equals(TYPE_DELETE) && product != null) setUpDelete(fragment);
        else if (parameter_type.equals(TYPE_UPDATE) && product != null) setUpUpdate(fragment);
        else setUpCreate(fragment);


        // Retorna o Fragment Configurado
        return fragment;
    }

    private void setUpCreate(View fragment) {
        title_fragment.setText(getString(R.string.title_product, "Cadastre"));

        Button btn_createProduct = fragment.findViewById(R.id.btn_registerProduct);
        btn_createProduct.setVisibility(View.VISIBLE);
        btn_createProduct.setOnClickListener(v -> {
        });
    }

    private void setUpUpdate(View fragment) {
        title_fragment.setText(getString(R.string.title_product, "Atualize"));
        showData();

        Button btn_changeProduct = fragment.findViewById(R.id.btn_changeProduct);
        btn_changeProduct.setVisibility(View.VISIBLE);
        btn_changeProduct.setOnClickListener(v -> {
        });
    }

    private void setUpDelete(View fragment) {
        title_fragment.setText(getString(R.string.title_product, "Exclua"));
        showData();

        LinearLayout linearLayout = fragment.findViewById(R.id.layout_fragmentProduct);
        linearLayout.setGravity(Gravity.CENTER);

        TextView text_delete = fragment.findViewById(R.id.text_delete);
        text_delete.setText(Html.fromHtml(getString(R.string.text_confirmDelete, product.getName_product())));
        text_delete.setVisibility(View.VISIBLE);

        TextView text_warming = fragment.findViewById(R.id.text_warmingDelete);
        text_warming.setVisibility(View.VISIBLE);

        Button btn_delete = fragment.findViewById(R.id.btn_deleteProduct);
        btn_delete.setVisibility(View.VISIBLE);
        btn_delete.setOnClickListener(v -> {
            Handler handler = new Handler(Looper.getMainLooper());
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(() -> {
                AlertDialogPersonalized dialogPersonalized = new AlertDialogPersonalized(fragment.getContext());
                ProductsAPI productAPI = new ProductsAPI(fragment.getContext());
                if (productAPI.deleteProduct(executorService, product.getId_product(),
                        user.getId_seller(), user.getToken_user())) {
                    handler.post(() -> dialogPersonalized.defaultDialog(getString(R.string.message_ok),
                            getString(R.string.message_deleteProduct)).show());
                } else {
                    handler.post(() -> dialogPersonalized.defaultDialog(getString(R.string.title_error_api),
                            getString(R.string.error_exception)).show());
                }
            });
        });
    }

    private void showData() {
        if (product != null) {
            edit_name.setText(product.getName_product());
            edit_category.setText(product.getCategory_product());
            edit_price.setText(String.valueOf(product.getPrice()));
            if (product.getColors() != null) edit_color.setText(product.getColors()[0]);
            if (product.getSizes() != null) edit_size.setText(product.getSizes()[0]);

            if (parameter_type.equals(TYPE_DELETE)) {
                edit_name.setEnabled(false);
                edit_category.setEnabled(false);
                edit_price.setEnabled(false);
                edit_color.setEnabled(false);
                edit_size.setEnabled(false);
            }
        }
    }
}