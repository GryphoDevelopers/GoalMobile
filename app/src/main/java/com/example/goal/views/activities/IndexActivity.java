package com.example.goal.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.goal.R;
import com.example.goal.managers.ManagerSharedPreferences;
import com.google.android.material.navigation.NavigationView;

/**
 * Activity IndexActivity: Activity Inicial, onde será mostrado os Produtos e toda a parte da
 * Navegação no Aplicativo
 */
public class IndexActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // Configura os elementos da Activity
        configToolBar();
        setUpLateralMenu();

        // TODO RETIRAR: Usado para Testes
        Button btn_home = findViewById(R.id.button_home);
        btn_home.setOnClickListener(v -> {
            // Retira a opção "Lembrar Login"
            ManagerSharedPreferences preferences = new ManagerSharedPreferences(this,
                    ManagerSharedPreferences.NAME_PREFERENCE);
            preferences.rememberLogin(false);
            // INicia a primeira Activity
            startActivity(new Intent(this, OpenActivity.class));
            finish();
        });

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
        DrawerLayout drawerLayout;
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
        switch (menuItem.getItemId()) {
            case R.id.equipment:
                System.out.println("Item: Equipamentos");
                return true;
            case R.id.acessories:
                System.out.println("Item: Acessorios");
                return true;
            case R.id.vitamins:
                System.out.println("Item: Vitaminas");
                return true;
            case R.id.clothes:
                System.out.println("Item: Roupas");
                return true;
            case R.id.shoes:
                System.out.println("Item: Sapatos");
                return true;
            case R.id.bags:
                System.out.println("Item: Bolsas");
                return true;
            case R.id.others:
                System.out.println("Item: Outros");
                return true;
            case R.id.contact:
                System.out.println("Item: Contato");
                return true;
            case R.id.about:
                System.out.println("Item: Sobre");
                return true;
            case R.id.privacy:
                System.out.println("Item: Privacidade");
                return true;
            default:
                System.out.println("Item Selecionado não Reconhecido");
                return false;
        }
    }

}