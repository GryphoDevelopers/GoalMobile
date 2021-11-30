package com.example.goal.views.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.goal.R;
import com.example.goal.models.Product;
import com.example.goal.views.widgets.SnackBarPersonalized;

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
    private String parameter_type;
    private String text_title;
    private String text_button;
    private Button button_fragment;

    /**
     * Construtor da Classe {@link ProductFragment} Vazio (Não possui Atributos Necessarios)
     */
    public ProductFragment() {
    }

    /**
     * Cosntrutor da Classe {@link ProductFragment} que recebe um Produto que será Atualizado ou
     * Excluido
     */
    public ProductFragment(Product product) {
        this.product = product;
    }

    /**
     * Cria uma Nova Instancia do Fragment {@link ProductFragment}, que possui os Parametros usados
     * na Configuraçõa
     *
     * @param type Tipo do Fragment ({@link #TYPE_CREATE}|{@link #TYPE_UPDATE}|{@link #TYPE_DELETE})
     */
    public static ProductFragment newInstance(String type, Product product) {

        // Instancia a Classe do Fragment de Acordo com o Tipo
        ProductFragment fragment;
        if (product != null) fragment = new ProductFragment(product);
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
        setUpWidgets(fragment);
        if (parameter_type.equals(TYPE_DELETE)) setUpDelete(fragment);

        // Retorna o Fragment Configurado
        return fragment;
    }

    /**
     * Configura os Widgets que serão exibidos na Tela
     */
    private void setUpWidgets(View fragment) {
        TextView title_fragment = fragment.findViewById(R.id.title_fragment_product);
        title_fragment.setText(text_title);

        // todo adicionar metodos de validação dos inputs (Classe Product)
        button_fragment = fragment.findViewById(R.id.btn_fragment_product);
        button_fragment.setText(text_button);
        button_fragment.setOnClickListener(v -> new SnackBarPersonalized(fragment)
                .defaultSnackBar("Clicou no Botão de Teste").show());
    }

    private void setUpDelete(View fragment) {
        // todo não deixar visivel o Layout dos Inputs
        LinearLayout linearLayout = fragment.findViewById(R.id.layout_fragmentProduct);
        linearLayout.setGravity(Gravity.CENTER);

        TextView text_delete = fragment.findViewById(R.id.text_delete);
        text_delete.setText(Html.fromHtml(getString(R.string.text_confirmDelete, product.getName_product())));
        text_delete.setVisibility(View.VISIBLE);

        TextView text_warming = fragment.findViewById(R.id.text_warmingDelete);
        text_warming.setVisibility(View.VISIBLE);
    }
}