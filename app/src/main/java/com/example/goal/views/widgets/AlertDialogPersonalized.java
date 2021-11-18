package com.example.goal.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.goal.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        alertDialogBuilder.setNegativeButton(R.string.button_close, (dialog, which) -> dialog.dismiss());
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
        alertDialogBuilder.setNegativeButton(R.string.button_close, (dialog, which) -> {
            dialog.dismiss();
            activity.finish();
        });
        alertDialogBuilder.setCancelable(false);

        return alertDialogBuilder.create();
    }

    /**
     * Cria um AlertDialog do Material UI com um Titulo, Mensagem e um ProgressIndicator (Material UI).
     * Esse AlertDialog não é possivel fechar clicando nna Tecla "Voltar" do Teclado
     *
     * @param message_body Mensagem informando o que está sendo carregado/realizado
     * @param isCancelable Define se o AlertDialog terá um botão para fechar caso o Usuario queira
     * @return AlertDialog
     */
    public AlertDialog loadingDialog(String message_body, boolean isCancelable) {
        // Cria o AlertDialog do MaterialUI e Configura Titulo, Mensagem e Botão para Fechar a Activity
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.title_loading));
        alertDialogBuilder.setMessage(message_body);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 24, 0, 32);

        //Adiciona o CircularPorgress
        alertDialogBuilder.setView(R.layout.layout_loading);

        // Define se o AlertDialog será fixo ou terá um botão para Finalizar
        if (isCancelable) {
            alertDialogBuilder.setNegativeButton(context.getString(R.string.button_close),
                    (dialog, which) -> dialog.dismiss());
        }
        alertDialogBuilder.setCancelable(false);

        return alertDialogBuilder.create();
    }

}