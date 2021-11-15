package com.example.goal.managers;

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
}
