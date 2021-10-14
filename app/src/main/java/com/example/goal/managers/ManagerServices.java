package com.example.goal.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Classe ManagerServices: Controla os Serviços (GPS, Internet) Usados no Aplicativo
 */
public class ManagerServices {

    // Constantes Usadas
    private final String NAME_CLASS = "ManagerServices";
    private final String NO_SERVICE = "No Service";
    private final String NO_NETWORK_INFO = "Invalid Network";
    private final String RUNTIME_EXCEPTION = "Runtime Exception";
    private final Context context;

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
    public boolean validationInternet() {
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

}
