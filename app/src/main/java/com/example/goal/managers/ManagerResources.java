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

    /**
     * A partir das Dimensões do Dispositivo, converte DP para Pixel
     *
     * @param context  {@link Context} utilizado para obter as Dimensões
     * @param value_dp Tamanho do DP que será convertido
     * @return int
     */
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

    /**
     * A partir das Dimensões do Dispositivo, converte SP para Pixel
     *
     * @param context  {@link Context} utilizado para obter as Dimensões
     * @param value_sp Tamanho do SP que será convertido
     * @return float
     */
    public static float spToPixel(Context context, int value_sp) {
        try {
            // Obtem unidade de Conversão e Converte o dp para Pixels
            float dpRatio = context.getResources().getDisplayMetrics().scaledDensity;
            return value_sp * dpRatio;
        } catch (Exception ex) {
            Log.e(EXCEPTION, "ManagerResources - Exceção ao Converter DP em Pixel: " + ex.getClass().getName());
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Retorna uma String cortada com reticencias (...) no Final caso seja maior que
     *
     * @param old_string  String que será cortada
     * @param max_length  Tamanho maximo que a String terá
     * @param putEllipsis Define se será ou não adicionado Reticencias (...) ao fim da String (Somente
     *                    se ela for maior que o "max_length"
     * @return {@link String}|""
     */
    public static String getSlideText(String old_string, int max_length, boolean putEllipsis) {
        if (isNullOrEmpty(old_string)) return "";
        else if (old_string.length() > max_length) {
            String new_string = old_string.substring(0, max_length + 1);
            if (putEllipsis) new_string += "...";

            return new_string;
        } else return old_string;
    }
}
