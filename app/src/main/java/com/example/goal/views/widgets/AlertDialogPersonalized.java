package com.example.goal.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.goal.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;

/**
 * Classe AlertDialogPersonalized: Retorna um AlertDialog do Material UI de diferentes Layouts.
 * <p>
 * Para usar, é necessario passar os parametros e depois colocar ".show()" no metodo
 */
public class AlertDialogPersonalized {

    private final Context context;

    /**
     * Construtor do AlertDialogPersonalized com o Context
     *
     * @param context Usado para a criação do AlertDialog
     */
    public AlertDialogPersonalized(Context context) {
        this.context = context;
    }

    /**
     * Cria um AlertDialog do Material UI com um Titulo, Mensagem e um botão "Cancelar"
     *
     * @param title   Titulo do AlertDialog
     * @param message Mensagem do AlertDialog
     * @return AlertDialog
     */
    public AlertDialog defaultDialog(String title, String message) {
        // Cria o AlertDialog do MaterialUI. Configura Titulo, Mensagem e o Botão
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton(R.string.button_close, (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.setCancelable(false);

        return alertDialogBuilder.create();
    }

    /**
     * Cria um AlertDialog do Material UI com um Titulo, Mensagem e um botão "Cancelar" que fecha a
     * Activity atual
     *
     * @param activity Activity que será Fechada
     * @param title    Titulo do AlertDialog
     * @param message  Mensagem do AlertDialog
     * @return AlertDialog
     */
    public AlertDialog messageWithCloseWindow(Activity activity, String title, String message) {
        // Cria o AlertDialog do MaterialUI e Configura Titulo, Mensagem e Botão para Fechar a Activity
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton(R.string.button_close, (dialog, which) -> {
            dialog.dismiss();
            activity.finish();
        });
        alertDialogBuilder.setCancelable(false);

        return alertDialogBuilder.create();
    }

    /**
     * Cria um AlertDialog do Material UI com um Titulo e um ProgressIndicator (Material UI)
     *
     * @return AlertDialog
     */
    public AlertDialog loadingDialog() {
        // Cria o AlertDialog do MaterialUI e Configura Titulo, Mensagem e Botão para Fechar a Activity
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.title_loading));

        CircularProgressIndicator progressIndicator = new CircularProgressIndicator(context);
        progressIndicator.setIndeterminate(true);

        // Cria um LinearLayout e Configura sua Gravity
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER);

        // Insere a Margem no Layout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 50, 0, 50);

        // Adiciona o ProgressIndicator com os Parametros
        linearLayout.addView(progressIndicator, layoutParams);

        //Adiciona o CircularPorgress e Não permite com que o Usuario Feche o Dialog
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder.setCancelable(false);
        return alertDialogBuilder.create();
    }

}