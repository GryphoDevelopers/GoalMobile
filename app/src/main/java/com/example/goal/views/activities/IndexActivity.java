package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerResources.isNullOrEmpty;
import static com.example.goal.managers.SearchInternet.URL_PRODUCTS;
import static com.example.goal.views.fragments.CatalogFragment.TYPE_CATEGORY;
import static com.example.goal.views.fragments.CatalogFragment.TYPE_HOME;
import static com.example.goal.views.fragments.CatalogFragment.TYPE_OTHERS;
import static com.example.goal.views.fragments.CatalogFragment.TYPE_SELLER_PRODUCTS;
import static com.example.goal.views.fragments.CatalogFragment.TYPE_WISHES;
import static com.example.goal.views.fragments.ProductFragment.TYPE_CREATE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerResources;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.Product;
import com.example.goal.models.User;
import com.example.goal.models.api.ProductsAPI;
import com.example.goal.views.fragments.CatalogFragment;
import com.example.goal.views.fragments.ProductFragment;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity IndexActivity: Activity Inicial, onde será mostrado os Produtos e toda a parte da
 * Navegação no Aplicativo
 */
public class IndexActivity extends AppCompatActivity {

    // Constantes Usadas no Caso de Passar dados Entre Intents
    public static final String TYPE_PRODUCT_FRAGMENT = "product_fragments";
    public static final String TYPE_CATALOG_FRAGMENT = "catalog_fragments";
    public static final String ID_PRODUCT = "id_product";

    // Contantes das Opções do Menu Lateral
    private static final int HOME = R.id.option_home;
    private static final int PROFILE = R.id.option_profile;
    private static final int SEARCH = R.id.option_search;
    private static final int WISHES = R.id.option_wishes;
    private static final int EQUIPMENTS = R.id.option_equipment;
    private static final int ACCESSORIES = R.id.option_accessories;
    private static final int VITAMINS = R.id.option_vitamins;
    private static final int CLOTHES = R.id.option_clothes;
    private static final int SHOES = R.id.option_shoes;
    private static final int BAGS = R.id.option_bags;
    private static final int OTHERS = R.id.option_others;
    private static final int REGISTER_PRODUCT = R.id.option_registerProduct;
    private static final int MY_PRODUCTS = R.id.option_myProducts;
    private static final int CONTACT = R.id.option_contact;
    private static final int ABOUT = R.id.option_about;
    private static final int PRIVACY = R.id.option_privacy;
    private static final int EXIT = R.id.option_exit;

    // Variaveis Utilziadas
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private List<Product> productList;
    private AlertDialogPersonalized alertDialogPersonalized;
    private ManagerSharedPreferences sharedPreferences;
    private ManagerDataBase dataBase;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // Configura os elementos da Activity
        instanceItems();
        setUpToolBar();
        setUpLateralMenu();

        Intent intent_index = getIntent();
        if (intent_index != null) {
            if (!isNullOrEmpty(intent_index.getStringExtra(TYPE_PRODUCT_FRAGMENT))) {
                String type_product = intent_index.getStringExtra(TYPE_PRODUCT_FRAGMENT);

                String id_product = intent_index.getStringExtra(ID_PRODUCT);
                if (!isNullOrEmpty(id_product)) {
                    // todo: obter o Produto da API
                    Product product = new Product(context);
                    product.setId_product(id_product);
                    product.setName_product("Random Name");
                    product.setPrice(569.6);

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments,
                            ProductFragment.newInstance(type_product, product)).commit();

                    navigationView.getMenu().findItem(MY_PRODUCTS).setCheckable(true);
                    navigationView.getMenu().findItem(MY_PRODUCTS).setChecked(true);
                } else {
                    alertDialogPersonalized.defaultDialog(
                            getString(R.string.title_input_invalid, "Produto"),
                            getString(R.string.error_product)).show();
                }
                return;
            } else if (!isNullOrEmpty(intent_index.getStringExtra(TYPE_CATALOG_FRAGMENT))) {
                getProducts(intent_index.getStringExtra(TYPE_CATALOG_FRAGMENT), "");
                navigationView.getMenu().findItem(MY_PRODUCTS).setCheckable(true);
                navigationView.getMenu().findItem(MY_PRODUCTS).setChecked(true);
                return;
            }
        }

        // Obtem uma Lista com os Produtos
        getProducts(TYPE_HOME, "");
        navigationView.getMenu().findItem(HOME).setCheckable(true);
        navigationView.getMenu().findItem(HOME).setChecked(true);
    }

    /**
     * Instancia os Itens que serão utilizados na Classe
     */
    private void instanceItems() {
        context = IndexActivity.this;
        navigationView = findViewById(R.id.navigationView_categories);
        toolbar = findViewById(R.id.toolbar_category);
        alertDialogPersonalized = new AlertDialogPersonalized(context);
        dataBase = new ManagerDataBase(context);
        sharedPreferences = new ManagerSharedPreferences(context, ManagerSharedPreferences.NAME_PREFERENCE);
    }

    /**
     * Configura a ToolBar, centralizando a Imagem no Meio
     */
    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Configura a Logo no Centro da ToolBar
        ImageView logo_goal = toolbar.findViewById(R.id.image_logo_goal);

        int margin_for_center = ManagerResources.dpToPixel(context, 52);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, margin_for_center, 0);

        logo_goal.setLayoutParams(layoutParams);
    }

    /**
     * Obtem os produtos da API e Chama o metodo que configura o Fragment
     *
     * @param category Categoria do Produto (Caso seja Catalogo, passar Vazio ("")
     */
    private void getProducts(String type, String category) {
        boolean isCategory = type.equals(TYPE_CATEGORY);
        String category_api;
        Uri uri_search;

        if (isCategory) {
            //todo remover/alterar para as categorias existentes
            switch (category) {
                case "Equipamentos":
                    category_api = "Blush";
                    break;
                case "Acessorios":
                    category_api = "Bronzer";
                    break;
                case "Vitaminas":
                    category_api = "Eyebrow";
                    break;
                case "Roupas":
                    category_api = "Eyeliner";
                    break;
                case "Calçados":
                    category_api = "Eyeshadow";
                    break;
                case "Bolsas":
                    category_api = "Foundation";
                    break;
                default:
                    category_api = "";
                    break;
            }
            uri_search = Uri.parse(URL_PRODUCTS).buildUpon().appendQueryParameter(
                    "product_type", category_api).build();
        } else if (type.equals(TYPE_SELLER_PRODUCTS)) {
            // todo alterar para api goal
            uri_search = Uri.parse(URL_PRODUCTS).buildUpon().appendQueryParameter(
                    "rating_greater_than", "4.8").build();
        } else uri_search = Uri.parse(URL_PRODUCTS).buildUpon().build();

        // Configura o AlertDialog que exibe "Carregando" enquanto busca os Itens
        AlertDialog dialogLoading = alertDialogPersonalized.loadingDialog(
                getString(R.string.message_loadingDownload, "dos Produtos"), false);

        // Cria uma Thread para atividade em Segundo Plano e Define um valor Fixo à categoria da API
        ExecutorService executorService = Executors.newCachedThreadPool();
        Uri finalUri_search = uri_search;
        executorService.execute(() -> {
            runOnUiThread(dialogLoading::show);

            // Obtem os Itens que serão Exibidos
            ProductsAPI productAPI = new ProductsAPI(context);
            List<Product> listCatalogProducts = productAPI.getProducts(executorService,
                    finalUri_search.toString());

            // Exibe o Resultado na Tela
            runOnUiThread(() -> {
                if (listCatalogProducts == null || listCatalogProducts.size() == 0) {

                    if (type.equals(TYPE_SELLER_PRODUCTS)) {
                        alertDialogPersonalized.defaultDialog(
                                getString(R.string.title_input_invalid, "Produtos"), getString(R.string.text_noProducts)).show();
                    } else {
                        // Exibe o Erro do Catalogo para o Usuario
                        alertDialogPersonalized.defaultDialog(
                                getString(R.string.title_input_invalid, "Produtos"), productAPI.getError_operation()).show();
                    }
                } else {
                    // Configura a Lista dos Produtos Exibe o Fragment da Tela Principal
                    productList = listCatalogProducts;

                    // Instancia a Classe do Fragment e Insere no Local de Exibição do Fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments,
                            isCategory
                                    ? CatalogFragment.newInstance(type, category, productList)
                                    : CatalogFragment.newInstance(type, productList)).commit();
                }
                dialogLoading.dismiss();
            });
        });
    }

    /**
     * Obtem a Lista de Desejos do Usuario
     */
    private void getWishes() {
        List<String> list_idProducts = dataBase.getIdWishes();
        if (list_idProducts == null) {
            alertDialogPersonalized.defaultDialog(
                    getString(R.string.title_input_invalid, "Produtos"),
                    getString(R.string.error_noMoreProducts)).show();
            return;
        }

        // Configura o AlertDialog que exibe "Carregando" enquanto busca os Itens
        AlertDialog dialogLoading = alertDialogPersonalized.loadingDialog(
                getString(R.string.message_loadingDownload, "dos Produtos"), false);

        // Cria uma Thread para atividade em Segundo Plano e Define um valor Fixo à categoria da API
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            runOnUiThread(dialogLoading::show);

            // Obtem os Itens que serão Exibidos
            ProductsAPI productAPI = new ProductsAPI(context);
            List<Product> listProductSelected = new ArrayList<>();
            for (String item : list_idProducts) {
                // adcionar busca 1 por 1 dos produtos
                Product product_selected = productAPI.getProduct(executorService,
                        sharedPreferences.getJsonWebTokenUser(), item);
                listProductSelected.add(product_selected);
            }
            // Exibe o Resultado na Tela
            runOnUiThread(() -> {
                if (listProductSelected.size() != 0) {
                    // Configura a Lista dos Produtos Exibe o Fragment da Tela Principal
                    productList = listProductSelected;

                    // Instancia a Classe do Fragment e Insere no Local de Exibição do Fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments,
                            CatalogFragment.newInstance(TYPE_WISHES, productList)).commit();
                } else {
                    alertDialogPersonalized.defaultDialog(
                            getString(R.string.title_input_invalid, "Produtos"),
                            getString(R.string.error_noMoreProducts)).show();
                }
                dialogLoading.dismiss();
            });
        });
    }

    /**
     * Configura o Drawer e o NavigationView (Toda a parte do Menu lateral)
     */
    private void setUpLateralMenu() {
        // Variaveis utilizadas
        ActionBarDrawerToggle actionToggle;
        DrawerArrowDrawable arrowDrawable;

        drawerLayout = findViewById(R.id.drawerLayout_categories);

        // Cria o Botão do Menu Lateral
        actionToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Personaliza a cor do Botão
        arrowDrawable = actionToggle.getDrawerArrowDrawable();
        arrowDrawable.setColor(getResources().getColor(R.color.white_light));

        // Deixa o Botão ativo e Sincronizado com o Estado definido
        actionToggle.setDrawerIndicatorEnabled(true);
        actionToggle.syncState();

        // Exibe ou não as Opções do Vendedor
        User user = dataBase.getUserDatabase();
        if (user != null && user.isSeller()) {
            navigationView.getMenu().findItem(R.id.menu_seller).setVisible(true);
        }

        // Listener do Botão do Drawer e dos Cliques no Menu
        drawerLayout.addDrawerListener(actionToggle);
        navigationView.setNavigationItemSelectedListener(this::itemSelect);
    }

    /**
     * Faz o Tratametno dos Cliques no Menu Lateral. É um metodo Sobrescrito.
     *
     * @param menuItem Item do Menu selecionado
     * @return true/false
     */
    private boolean itemSelect(MenuItem menuItem) {
        int id_item = menuItem.getItemId();
        if (id_item != PROFILE && id_item != SEARCH && id_item != EXIT) {
            unselectedItemsMenu();
            menuItem.setChecked(true);
            menuItem.setCheckable(true);
        }

        // todo: Terminar Implementação (Contato, Sobre, Privacidade)
        switch (id_item) {
            case HOME:
                getProducts(TYPE_HOME, "");
                break;
            case PROFILE:
                startActivity(new Intent(context, ProfileActivity.class));
                break;
            case SEARCH:
                startActivity(new Intent(context, SearchActivity.class));
                break;
            case WISHES:
                getWishes();
                break;
            case EQUIPMENTS:
                getProducts(TYPE_CATEGORY, getString(R.string.option_equipment));
                break;
            case ACCESSORIES:
                getProducts(TYPE_CATEGORY, getString(R.string.option_accessories));
                break;
            case VITAMINS:
                getProducts(TYPE_CATEGORY, getString(R.string.option_vitamins));
                break;
            case CLOTHES:
                getProducts(TYPE_CATEGORY, getString(R.string.option_clothes));
                break;
            case SHOES:
                getProducts(TYPE_CATEGORY, getString(R.string.option_shoes));
                break;
            case BAGS:
                getProducts(TYPE_CATEGORY, getString(R.string.option_bags));
                break;
            case OTHERS:
                getProducts(TYPE_OTHERS, "");
                break;
            case CONTACT:
                System.out.println("Item: Contato");
                break;
            case ABOUT:
                System.out.println("Item: Sobre");
                break;
            case PRIVACY:
                System.out.println("Item: Privacidade");
                break;
            case REGISTER_PRODUCT:
                // Instancia a Classe do Fragment e Insere no Local de Exibição do Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments,
                        ProductFragment.newInstance(TYPE_CREATE, null)).commit();
                break;
            case MY_PRODUCTS:
                getProducts(TYPE_SELLER_PRODUCTS, "");
                break;
            case EXIT:
                dataBase.clearTables();
                sharedPreferences.clearPreferences();
                startActivity(new Intent(context, OpenActivity.class));
                finishAffinity();
                break;
            default:
                new SnackBarPersonalized(drawerLayout).defaultSnackBar(
                        getString(R.string.validation_unavailable, "Opção")).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
        }
        // Fecha o Menu Lateral
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Remove a Seleção de todos os Itens e SubMenus do Menu Lateral
     */
    private void unselectedItemsMenu() {
        //Laço de Repetição para desselecionar todos os Itens do Menu
        int sizeMenu = navigationView.getMenu().size();
        for (int i = 0; i < sizeMenu; i++) {

            MenuItem menuItem = navigationView.getMenu().getItem(i);
            if (menuItem.hasSubMenu()) {
                // Caso tenha um SubMenu, acessa eles para retirar a seleção
                for (int u = 0; u < menuItem.getSubMenu().size(); u++) {
                    menuItem.getSubMenu().getItem(u).setChecked(false);
                    menuItem.getSubMenu().getItem(u).setCheckable(false);
                }
            } else {
                navigationView.getMenu().getItem(i).setChecked(false);
                navigationView.getMenu().getItem(i).setCheckable(false);
            }
        }
    }

}