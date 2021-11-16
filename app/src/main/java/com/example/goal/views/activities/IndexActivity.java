package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.Product;
import com.example.goal.views.fragments.ProductsFragment;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity IndexActivity: Activity Inicial, onde será mostrado os Produtos e toda a parte da
 * Navegação no Aplicativo
 */
public class IndexActivity extends AppCompatActivity {

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
    private static final int UPDATE_PRODUCT = R.id.option_updateProduct;
    private static final int DELETE_PRODUCT = R.id.option_deleteProduct;
    private static final int CONTACT = R.id.option_contact;
    private static final int ABOUT = R.id.option_about;
    private static final int PRIVACY = R.id.option_privacy;
    private static final int EXIT = R.id.option_exit;

    // Variaveis Utilziadas
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // Configura os elementos da Activity
        instanceItems();
        setSupportActionBar(toolbar);
        setUpLateralMenu();

        // Obtem uma Lista com os Produtos
        getCatalogProducts();

        // Exibe o Fragment da Tela Principal
        setUpProductsFragment(ProductsFragment.TYPE_HOME, "");
    }

    /**
     * Instancia os Itens que serão utilizados na Classe
     */
    private void instanceItems() {
        navigationView = findViewById(R.id.navigationView_categories);
        toolbar = findViewById(R.id.toolbar_category);
    }

    /**
     * Obtem os Produtos que serão exibidos na Tela Inicial
     */
    private void getCatalogProducts() {
        // todo Implementar busca na api
        productList = new ArrayList<>();
        String[] names = new String[]{"Produto 1", "Produto 2", "Produto 3", "Produto 4", "Produto 5",
                "Produto 6", "Produto 7", "Produto 8", "Produto 9", "Produto 10", "Produto 11", "Produto 12"};
        for (String item : names) {
            Product product = new Product(IndexActivity.this);
            product.setName_product(item);
            productList.add(product);
        }
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
                R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);

        // Personaliza a cor do Botão
        arrowDrawable = actionToggle.getDrawerArrowDrawable();
        arrowDrawable.setColor(getResources().getColor(R.color.white_light));

        // Deixa o Botão ativo e Sincronizado com o Estado definido
        actionToggle.setDrawerIndicatorEnabled(true);
        actionToggle.syncState();

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
        if (id_item != PROFILE && id_item != SEARCH && id_item != WISHES && id_item != EXIT) {
            unselectedItemsMenu();
            menuItem.setChecked(true);
            menuItem.setCheckable(true);
        }

        // todo: Terminar Implementação (Contato, Sobre, Privacidade)
        switch (id_item) {
            case HOME:
                setUpProductsFragment(ProductsFragment.TYPE_HOME, "");
                break;
            case PROFILE:
                startActivity(new Intent(IndexActivity.this, ProfileActivity.class));
                break;
            case SEARCH:
                startActivity(new Intent(IndexActivity.this, SearchActivity.class));
                break;
            case WISHES:
                startActivity(new Intent(IndexActivity.this, WishesActivity.class));
                break;
            case EQUIPMENTS:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_equipment));
                break;
            case ACCESSORIES:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_accessories));
                break;
            case VITAMINS:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_vitamins));
                break;
            case CLOTHES:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_clothes));
                break;
            case SHOES:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_shoes));
                break;
            case BAGS:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_bags));
                break;
            case OTHERS:
                setUpProductsFragment(ProductsFragment.TYPE_OTHERS, "");
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
                // todo: adicionar fragment de cadastro/atualização de produtos
                System.out.println("Item: Adicionar Produto");
                break;
            case UPDATE_PRODUCT:
                System.out.println("Item: Atualizar Produto");
                break;
            case DELETE_PRODUCT:
                System.out.println("Item: Excluir Produto");
                break;
            case EXIT:
                new ManagerDataBase(IndexActivity.this).clearTables();
                new ManagerSharedPreferences(IndexActivity.this,
                        ManagerSharedPreferences.NAME_PREFERENCE).clearPreferences();
                startActivity(new Intent(IndexActivity.this, OpenActivity.class));
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
     * Configura a Exibição de Fragments dos Produtos
     *
     * @param type     Tipo do Fragment
     * @param category Categoria do Fragment (Somente é utilizada no "type" "CATEGORY_PRODUCTS")
     */
    private void setUpProductsFragment(String type, String category) {
        // Instancia a Classe do Fragment e Insere no Local de Exibição do Fragment
        ProductsFragment productsFragment = ProductsFragment.newInstance(type, category, productList);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.frame_fragments, productsFragment).commit();
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