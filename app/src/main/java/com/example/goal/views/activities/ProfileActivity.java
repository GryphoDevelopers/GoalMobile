package com.example.goal.views.activities;

import static com.example.goal.managers.ManagerSharedPreferences.NAME_PREFERENCE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.goal.R;
import com.example.goal.managers.ManagerDataBase;
import com.example.goal.managers.ManagerResources;
import com.example.goal.managers.ManagerSharedPreferences;
import com.example.goal.models.User;
import com.example.goal.models.api.UserAPI;
import com.example.goal.views.widgets.AlertDialogPersonalized;
import com.example.goal.views.widgets.SnackBarPersonalized;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity ProfileActivity: Activity que irá mostrar os dados de Cadatro do Usuario
 */
public class ProfileActivity extends AppCompatActivity {

    private Context context;
    private User userProfile;
    private AlertDialogPersonalized dialogPersonalized;
    private ManagerDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Instancia e Configura a Activity
        context = ProfileActivity.this;
        dialogPersonalized = new AlertDialogPersonalized(context);
        database = new ManagerDataBase(context);

        setUpElements();
    }

    /**
     * Configura os Elementos da Activity
     */
    private void setUpElements() {
        userProfile = new ManagerDataBase(context).getUserDatabase();
        if (userProfile != null) {
            setUpToolBar();
            TextInputEditText edit_name = findViewById(R.id.edittext_nameProfile);
            edit_name.setText(userProfile.getName());

            TextInputEditText edit_lastName = findViewById(R.id.edittext_lastNameProfile);
            edit_lastName.setText(userProfile.getLast_name());

            TextInputEditText edit_email = findViewById(R.id.edittext_emailProfile);
            edit_email.setText(userProfile.getEmail());

            TextInputEditText edit_password = findViewById(R.id.edittext_passwordProfile);
            edit_password.setText(userProfile.getPassword());

            TextInputEditText edit_birthday = findViewById(R.id.edittext_birthdayProfile);
            edit_birthday.setText(userProfile.getString_dateBirth());

            ImageView image_profile = findViewById(R.id.imageProfile);
            image_profile.setImageResource(R.drawable.user_image);

            // Configura os Cliques no Botão
            setUpButtons();
        } else {
            dialogPersonalized.messageWithCloseWindow(this,
                    getString(R.string.title_input_invalid, "Usuario"),
                    getString(R.string.error_login_api)).show();
        }
    }


    /**
     * Configura a ToolBar Centralizando a Logo no Centro
     */
    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
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
     * Configura o Clique nos {@link Button Botões} da Activity
     */
    private void setUpButtons() {
        Button btn_updateUser = findViewById(R.id.button_updateUser);
        btn_updateUser.setOnClickListener(v -> startActivity(
                new Intent(context, ChangesActivity.class)));

        Button btn_deleteUser = findViewById(R.id.button_deleteUser);
        btn_deleteUser.setOnClickListener(v -> {
            AlertDialog alertDelete = dialogPersonalized.defaultDialog(
                    getString(R.string.btn_deleteUser),
                    Html.fromHtml(getString(R.string.txt_confirmDeleteUser)).toString());
            alertDelete.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btn_deleteUser),
                    (dialog, which) -> {
                        ExecutorService executorService = Executors.newCachedThreadPool();
                        executorService.execute(() -> {
                            UserAPI userAPI = new UserAPI(context);
                            if (userAPI.deleteUserAPI(executorService, userProfile)) {
                                database.clearTables();
                                new ManagerSharedPreferences(context, NAME_PREFERENCE).clearPreferences();
                                runOnUiThread(() -> {
                                    alertDelete.dismiss();
                                    startActivity(new Intent(context, OpenActivity.class));
                                    finishAffinity();
                                });
                            } else {
                                runOnUiThread(() -> {
                                    alertDelete.dismiss();
                                    dialogPersonalized.defaultDialog(
                                            getString(R.string.title_input_invalid, "Usuario"),
                                            getString(R.string.error_generic)).show();
                                });
                            }
                        });
                    });
            alertDelete.show();
        });

        Button bnt_orders = findViewById(R.id.bnt_orders);
        bnt_orders.setOnClickListener(v -> {
            //todo implementar
            ScrollView scrollView = findViewById(R.id.scrollview_profile);
            new SnackBarPersonalized(scrollView).defaultSnackBar("Clicou no Extrato").show();
        });

        Button btn_clearDatabase = findViewById(R.id.bnt_deleteLocalData);
        btn_clearDatabase.setOnClickListener(v -> {
            database.clearTables();
            new ManagerSharedPreferences(context, NAME_PREFERENCE).clearPreferences();
            startActivity(new Intent(context, OpenActivity.class));
            finishAffinity();
        });

        Button btn_home = findViewById(R.id.btn_goHome);
        btn_home.setOnClickListener(v -> finish());
    }
}