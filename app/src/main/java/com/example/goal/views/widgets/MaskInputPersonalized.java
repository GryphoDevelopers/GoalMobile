package com.example.goal.views.widgets;

import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.managers.ManagerResources.isNullOrEmpty;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

/**
 * Classe Abstrata MaskInputPersonalized: Classe que controla as Mascaras nos Inputs
 * <p>
 * Ela é 'abstract' pois serve exclusivamente como classe de modelo. Ela não é instanciada
 */
public abstract class MaskInputPersonalized {

    // Constantes das Mascaras Disponiveis. Espaços com # serão os Espaços em Branco
    public static final String MASK_CNPJ = "##.###.###/####-##";
    public static final String MASK_CPF = "###.###.###-##";
    public static final String MASK_DATE = "##/##/####";
    public static final String MASK_CEP = "#####-###";
    public static final String MASK_PHONE_BR = "(###)#####-####";

    // Constantes usadas na Formatação
    public static final char DEFAULT_WHITE_SPACE = '_';
    public static final String DEFAULT_REGEX = "[().\\-/#_\\s]*";

    private static final String NAME_CLASS = "MaskInputPersonalized";

    /**
     * Metodo que Define a Mascara em um TextInputEditText, controlando as ações do Input (Mudanças no Texto)
     *
     * @param editText EditText do Material UI que será formatado com a mascara
     * @param mask     Mascara aplicada no EditText
     * @return {@link TextWatcher TextWatcher}
     */
    public static TextWatcher managerMask(TextInputEditText editText, String mask) {
        return new TextWatcher() {
            // Controla se o texto já foi Atualizado ou não
            boolean isUpdate;
            String old_string = "";

            // Obtem o Texto no momento da Mudança do Texto
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    // Obtem a String do Texto do EditText e remove a Mascara (substitui os caracteres)
                    String string_receiver = remove_mask(s.toString(), DEFAULT_REGEX);
                    if (isNullOrEmpty(string_receiver)) string_receiver = "";

                    // Evita que o Metodo fique em um Loop Infinito
                    if (isUpdate) {
                        old_string = string_receiver;
                        isUpdate = false;
                        return;
                    }

                    // Define que o EditText foi Atualizado
                    isUpdate = true;

                    if (string_receiver.length() > old_string.length()) {
                        // Obtem o TEXTO INSERIDO e Define o Texto Formatado
                        String formatted_text = add_mask(mask, string_receiver, DEFAULT_WHITE_SPACE);
                        if (isNullOrEmpty(formatted_text)) formatted_text = "";
                        editText.setText(formatted_text);

                        // Obtem a Posição que o Cursor
                        int position_cursor = positionCursor(formatted_text, DEFAULT_WHITE_SPACE, DEFAULT_REGEX);
                        editText.setSelection(position_cursor);
                    } else {
                        // Define o Valor da String. Verifica se o Caraceter Removido era da Formatção
                        // Obtem a Posição que o Cursor Ficaria para Obter o Caracter usado na Mascara
                        int position_removed = positionCursor(s.toString(), DEFAULT_WHITE_SPACE, DEFAULT_REGEX);
                        char char_removed = mask.charAt(position_removed);
                        String string_removed;
                        if (char_removed != '#' && String.valueOf(char_removed).matches(DEFAULT_REGEX)) {
                            // Tentativa de Remover algum caracter de Formatação
                            string_removed = string_receiver.substring(0, string_receiver.length() - 1);
                        } else string_removed = string_receiver;

                        String formatted_text = add_mask(mask, string_removed, DEFAULT_WHITE_SPACE);
                        if (isNullOrEmpty(formatted_text)) formatted_text = " ";
                        editText.setText(formatted_text);

                        // Define a Posição do Cursor
                        int position_cursor = positionCursor(formatted_text, DEFAULT_WHITE_SPACE, DEFAULT_REGEX);
                        editText.setSelection(position_cursor);
                    }
                } catch (Exception ex) {
                    isUpdate = true;
                    Log.e(EXCEPTION, NAME_CLASS + " - Falha na Definição da Mascara");
                    ex.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    /**
     * Obtem uma Posição Valida, para o Cursor na Mascara Formatada.
     * <p>
     * * Retorna 0 caso haja algum erro
     *
     * @param text        Texto que será obtido a Posição do Cursor
     * @param white_space Caracter que define o Espaço em Branco
     * @return int/0
     */
    public static int positionCursor(String text, char white_space, String regex_format) {
        if (isNullOrEmpty(text)) return 0;

        // Obtem o Tamanho e um Array Letra por Letra
        int length_text = text.length();
        char[] char_array = text.toCharArray();

        // Loop para Verificar a Formatação e Espaço em Branco
        for (int i = 0; i < char_array.length; i++) {
            if (String.valueOf(char_array[i]).matches(regex_format)) {
                // A Letra (char) é um dos Caracteres de Formatação
                if (char_array[i] == white_space) length_text--;
                else if (text.charAt(i + 1) == white_space && text.charAt(i - 1) == white_space) {
                    // Obtem o Caracter Seguinte e Anterior para Verficar se é um Caracter Branco
                    length_text--;
                }
            }
        }
        // Cursor sempre estará na Proxima Posição
        //return length_text == text.length() ? length_text : length_text + 1;
        return length_text;
    }

    /**
     * Remove a Mascara de uma String. Remove os Caracteres de Acordo com o Regex.
     * <p>
     * Retornará null caso o Regex seja Invalido
     *
     * @param original_string    String que deseja remover os Caracteres
     * @param regex_substitution Regex de Exclusão dos Caracteres
     * @return {@link String String}|null
     */
    public static String remove_mask(String original_string, String regex_substitution) {
        try {
            return original_string.replaceAll(regex_substitution, "");
        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Falha na Remoção da Mascara");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Adiciona uma Mascara em uma String informada. Os caracteres que serão substituidos são "#".
     * <p>
     * *Asteristicos (#) não preenchidos, serão substituidos pelo Caracter do white_space
     *
     * @param text        Texto que irá ser colocado na Mascara
     * @param mask        Mascara que será inserida na String. Os Campos com o Caracter "#" serão
     *                    substituidos, os demais, permanecerão na formatação
     * @param white_space Caracteres que será colocados nos espaços não preenchidos
     * @return {@link String String}|null
     */
    public static String add_mask(String mask, String text, char white_space) {
        try {
            // Cria um Array das Letras do Texto Informado
            char[] chars_text = text.toCharArray();
            StringBuilder formatted_text = new StringBuilder();

            // Controla os Caracteres do Texto Informado. Evita obter Valores fora do Indice do Array
            int i = 0;
            // Laço de Repetição para os Caracteres da Mascara
            for (char item_mask : mask.toCharArray()) {
                if (item_mask == '#' && chars_text.length > i) {
                    // Substitui os Caracteres "#" com o Texto Inserido pelo Usuario
                    formatted_text.append(chars_text[i]);
                    i++;
                } else formatted_text.append(item_mask);
            }
            if (formatted_text.toString().equals("")) return null;
            else return formatted_text.toString().replaceAll("#", String.valueOf(white_space));

        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Falha na Formatação com a Mascara");
            ex.printStackTrace();
            return null;
        }
    }

}
