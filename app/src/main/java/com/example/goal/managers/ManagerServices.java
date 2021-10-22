package com.example.goal.managers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Classe ManagerServices: Controla os Serviços (GPS, Internet, Teclado) Usados no Aplicativo
 */
public class ManagerServices {

    // Constantes Usadas
    private final String NAME_CLASS = "ManagerServices";
    private final String NO_SERVICE = "No Service";
    private final String NO_NETWORK_INFO = "Invalid Network";
    private final String RUNTIME_EXCEPTION = "Runtime Exception";
    private final Context context;
    private InputMethodManager keyboardManager;

    /**
     * Construtor da Classe que obtem o Context da Activity ou Fragment para acessar os Services
     */
    public ManagerServices(Context context) {
        this.context = context;
    }

    /**
     * Obtem o Serviço de Conexão e Valida se a Internet está disponivel ou não
     *
     * @return true/false
     */
    public boolean availableInternet() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager == null) {
                Log.e(NO_SERVICE, NAME_CLASS + " Erro ao Obter o serviço de Internet");
                return false;
            }

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) return true;
        } catch (RuntimeException ex) {
            Log.e(RUNTIME_EXCEPTION, NAME_CLASS + " Erro ao Obter o serviço de Internet");
            ex.printStackTrace();
            return false;
        }

        Log.e(NO_NETWORK_INFO, NAME_CLASS + " Erro ao Obter as Informações de Internet");
        return false;
    }

    /**
     * Abre o Teclado na View Informada
     *
     * @param viewOpen View (ex: Activity, EditText) que deseja abrir o teclado
     */
    public void openKeyboard(View viewOpen) {
        keyboardManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Se ontem o controlador do Teclado = Abre
        if (keyboardManager != null) {
            keyboardManager.showSoftInput(viewOpen, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Responsavel por fechar o Teclado
     *
     * @param activity Activity em que o Teclado será fechado
     */
    public void closeKeyboard(Activity activity) {
        keyboardManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Se obtem o controlador do Teclado = Fecha
        if (keyboardManager != null) {
            keyboardManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
