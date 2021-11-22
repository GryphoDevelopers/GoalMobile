package com.example.goal.managers;

import static com.example.goal.managers.ManagerResources.EXCEPTION;

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
                Log.e("Sem Serviço", NAME_CLASS + " - Erro ao Obter o serviço de Internet: " +
                        "Serviço de Conexão Indisponivel");
                return false;
            }

            NetworkInfo networkInfoWifi = connectivityManager.getActiveNetworkInfo();
            if (networkInfoWifi != null && networkInfoWifi.isConnected()) return true;

            Log.e("Sem Internet", NAME_CLASS + " - Internet Indisponivel");
            return false;
        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " Execeção ao Obter o serviço de Internet: " +
                    ex.getClass().getName());
            ex.printStackTrace();
            return false;
        }
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
