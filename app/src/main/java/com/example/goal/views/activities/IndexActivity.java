package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.goal.R;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.views.fragments.ProductsFragment;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.navigation.NavigationView;

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
    private static final int CONTACT = R.id.option_contact;
    private static final int ABOUT = R.id.option_about;
    private static final int PRIVACY = R.id.option_privacy;
    private static final int EXIT = R.id.option_exit;

    // Variaveis Utilziadas
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // Configura os elementos da Activity
        configToolBar();
        setUpLateralMenu();
    }

    /**
     * Configura e Instancia a ToolBar (Parte Superior da Activity)
     */
    private void configToolBar() {
        toolbar = findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);
    }

    /**
     * Configura o Drawer e o NavigationView (Toda a parte do Menu lateral)
     */
    private void setUpLateralMenu() {
        // Variaveis utilizadas
        ActionBarDrawerToggle actionToggle;
        DrawerArrowDrawable arrowDrawable;
        NavigationView navigationView;

        drawerLayout = findViewById(R.id.drawerLayout_categories);
        navigationView = findViewById(R.id.navigationView_categories);

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
     * @param menuItem Item selecionado no Menu
     * @return true/false
     */
    private boolean itemSelect(MenuItem menuItem) {
        // todo: Terminar Implementação (Contato, Sobre, Privacidade)
        switch (menuItem.getItemId()) {
            case HOME:
                setUpProductsFragment(ProductsFragment.TYPE_HOME, "");
                return true;
            case PROFILE:
                startActivity(new Intent(IndexActivity.this, ProfileActivity.class));
                return true;
            case SEARCH:
                startActivity(new Intent(IndexActivity.this, SearchActivity.class));
                return true;
            case WISHES:
                startActivity(new Intent(IndexActivity.this, WishesActivity.class));
                return true;
            case EQUIPMENTS:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_equipment));
                return true;
            case ACCESSORIES:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_accessories));
                return true;
            case VITAMINS:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_vitamins));
                return true;
            case CLOTHES:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_clothes));
                return true;
            case SHOES:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_shoes));
                return true;
            case BAGS:
                setUpProductsFragment(ProductsFragment.TYPE_CATEGORY, getString(R.string.option_bags));
                return true;
            case OTHERS:
                setUpProductsFragment(ProductsFragment.TYPE_OTHERS, "");
                return true;
            case CONTACT:
                System.out.println("Item: Contato");
                return true;
            case ABOUT:
                System.out.println("Item: Sobre");
                return true;
            case PRIVACY:
                System.out.println("Item: Privacidade");
                return true;
            case EXIT:
                // todo: limpar banco de dados local
                // Altera a Opção do Login nas SharedPreferences e Inicia a Activity de Login
                new ManagerSharedPreferences(IndexActivity.this,
                        ManagerSharedPreferences.NAME_PREFERENCE).rememberLogin(false);
                startActivity(new Intent(IndexActivity.this, OpenActivity.class));
                finish();
                return true;
            default:
                new SnackBarPersonalized(drawerLayout).defaultSnackBar(
                        getString(R.string.validation_unavailable, "Opção")).show();
                return false;
        }
    }

    /**
     * Configura a Exibição de Fragments dos Produtos
     *
     * @param type     Tipo do Fragment
     * @param category Categoria do Fragment (Somente é utilizada no "type" "CATEGORY_PRODUCTS")
     */
    private void setUpProductsFragment(String type, String category) {
        // Instancia a Classe do Fragment e Insere no Local de Exibição do Fragment
        ProductsFragment productsFragment = ProductsFragment.newInstance(type, category);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.frame_fragments, productsFragment).commit();
    }


}