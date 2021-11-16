package com.example.goal.managers;

import android.content.Context;
import android.util.Log;

import java.util.Locale;

/**
 * Classe ManagerResources: Classe que adiciona novos recursos que serão utilizados no aplicativo
 */
public class ManagerResources {

    /**
     * Configura o Fuso Horario Padrão do APP
     */
    public static final String TIME_ZONE = "America/Sao_Paulo";

    /**
     * Configura o Local Padrão do APP utilizado
     */
    public static final Locale LOCALE_BR = new Locale("pt", "BR");

    /**
     * Formato padrão de Data
     */
    public static final String PATTERN_DATE = "dd/MM/yyyy";

    /**
     * Tag que aparece como Titulo nas Exceptions
     */
    public static final String EXCEPTION = "Exception General";

    /**
     * Verifica se uma String é nula ou Vazia
     *
     * @return true|false
     */
    public static boolean isNullOrEmpty(String check_string) {
        return check_string == null || check_string.equals("");
    }

    public static int dpToPixel(Context context, int value_dp) {
        try {
            // Obtem unidade de Conversão e Converte o dp para Pixels
            float dpRatio = context.getResources().getDisplayMetrics().density;
            return (int) (value_dp * dpRatio);
        } catch (Exception ex) {
            Log.e(EXCEPTION, "ManagerResources - Exceção ao Converter DP em Pixel: " + ex.getClass().getName());
            ex.printStackTrace();
            return 0;
        }
    }
}
