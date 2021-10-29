package com.example.goal.views.widgets;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Classe MaskInputPersonalizedTest: Testes do Manipulador de Mascaras de Inputs de Textos
 */
public class MaskInputPersonalizedTest {

    private String[] possible_dates;
    private String[] possible_cpf;
    private String[] possible_cnpj;
    private String[] possible_cep;

    /**
     * Instancia os Itens que serão utilizadoss nos Testes
     */
    @Before
    public void instanceItens() {
        possible_dates = new String[]{"__/__/____", "0_/__/____", "00/__/____", "00/0_/____",
                "00/00/____", "00/00/0___", "00/00/00__", "00/00/000_", "00/00/0000"};

        possible_cpf = new String[]{"___.___.___-__", "0__.___.___-__", "00_.___.___-__",
                "000.___.___-__", "000.0__.___-__", "000.00_.___-__", "000.000.___-__",
                "000.000.0__-__", "000.000.00_-__", "000.000.000-__", "000.000.000-0_",
                "000.000.000-00"};

        possible_cnpj = new String[]{"__.___.___/____-__", "0_.___.___/____-__", "00.___.___/____-__",
                "00.0__.___/____-__", "00.00_.___/____-__", "00.000.___/____-__", "00.000.0__/____-__",
                "00.000.00_/____-__", "00.000.000/____-__", "00.000.000/0___-__", "00.000.000/00__-__",
                "00.000.000/000_-__", "00.000.000/0000-__", "00.000.000/0000-0_", "00.000.000/0000-00"};

        possible_cep = new String[]{"_____-___", "0____-___", "00___-___", "000__-___", "0000_-___",
                "00000-___", "00000-0__", "00000-00_", "00000-000"};
    }

    /**
     * Adiciona Mascara nos Inputs de CNPJ, CPF, Data e CEP
     */
    @Test
    public void testAddMask() {
        for (int i = 0; i < possible_cpf.length; i++) {
            StringBuilder cpf_amount = new StringBuilder();
            if (i != 0) {
                for (int u = 0; u < i; u++) {
                    // Adiciona "0" de acordo com o "i" do Loop
                    cpf_amount.append(0);
                }
            }
            assertEquals(possible_cpf[i], MaskInputPersonalized.add_mask(MaskInputPersonalized.MASK_CPF,
                    cpf_amount.toString(), MaskInputPersonalized.DEFAULT_WHITE_SPACE));
        }

        for (int i = 0; i < possible_dates.length; i++) {
            StringBuilder dates_amount = new StringBuilder();
            if (i != 0) {
                // Adiciona "0" de acordo com o "i" do Loop
                for (int u = 0; u < i; u++) {
                    dates_amount.append(0);
                }
            }
            assertEquals(possible_dates[i], MaskInputPersonalized.add_mask(MaskInputPersonalized.MASK_DATE,
                    dates_amount.toString(), MaskInputPersonalized.DEFAULT_WHITE_SPACE));
        }

        for (int i = 0; i < possible_cnpj.length; i++) {
            StringBuilder cnpj_amount = new StringBuilder();
            if (i != 0) {
                // Adiciona "0" de acordo com o "i" do Loop
                for (int u = 0; u < i; u++) {
                    cnpj_amount.append(0);
                }
            }
            assertEquals(possible_cnpj[i], MaskInputPersonalized.add_mask(MaskInputPersonalized.MASK_CNPJ,
                    cnpj_amount.toString(), MaskInputPersonalized.DEFAULT_WHITE_SPACE));
        }

        for (int i = 0; i < possible_cep.length; i++) {
            StringBuilder cep_amount = new StringBuilder();
            if (i != 0) {
                // Adiciona "0" de acordo com o "i" do Loop
                for (int u = 0; u < i; u++) {
                    cep_amount.append(0);
                }
            }
            assertEquals(possible_cep[i], MaskInputPersonalized.add_mask(MaskInputPersonalized.MASK_CEP,
                    cep_amount.toString(), MaskInputPersonalized.DEFAULT_WHITE_SPACE));
        }
    }

    /**
     * Remove Mascara nos Inputs de CNPJ, CPF, Data e CEP
     */
    @Test
    public void testRemoveMask() {
        String cnpj_numbers = "";
        for (int i = 0; i < possible_cnpj.length; i++) {
            if (i != 0) cnpj_numbers += String.valueOf(0);
            else cnpj_numbers = "";

            assertEquals(cnpj_numbers, MaskInputPersonalized.remove_mask(
                    possible_cnpj[i], MaskInputPersonalized.DEFAULT_REGEX));
        }

        String cpf_numbers = "";
        for (int i = 0; i < possible_cpf.length; i++) {
            if (i != 0) cpf_numbers += String.valueOf(0);
            else cpf_numbers = "";

            assertEquals(cpf_numbers, MaskInputPersonalized.remove_mask(
                    possible_cpf[i], MaskInputPersonalized.DEFAULT_REGEX));
        }

        String date_numbers = "";
        for (int i = 0; i < possible_dates.length; i++) {
            if (i != 0) date_numbers += String.valueOf(0);
            else date_numbers = "";

            assertEquals(date_numbers, MaskInputPersonalized.remove_mask(
                    possible_dates[i], MaskInputPersonalized.DEFAULT_REGEX));
        }

        String cep_numbers = "";
        for (int i = 0; i < possible_cep.length; i++) {
            if (i != 0) cep_numbers += String.valueOf(0);
            else cep_numbers = "";

            assertEquals(cep_numbers, MaskInputPersonalized.remove_mask(
                    possible_cep[i], MaskInputPersonalized.DEFAULT_REGEX));
        }
    }

    /**
     * Testa se a Posição do Cursor está correta
     */
    @Test
    public void testPositionCursor() {
        int[] position_dates = new int[]{0, 1, 3, 4, 6, 7, 8, 9, 10};
        assertEquals(possible_dates.length, position_dates.length);

        for (int i = 0; i < possible_dates.length; i++) {
            assertEquals(position_dates[i], MaskInputPersonalized.positionCursor(possible_dates[i],
                    MaskInputPersonalized.DEFAULT_WHITE_SPACE, MaskInputPersonalized.DEFAULT_REGEX));
        }

        int[] position_cpf = new int[]{0, 1, 2, 4, 5, 6, 8, 9, 10, 12, 13, 14};
        assertEquals(possible_cpf.length, position_cpf.length);

        for (int i = 0; i < possible_cpf.length; i++) {
            assertEquals(position_cpf[i], MaskInputPersonalized.positionCursor(possible_cpf[i],
                    MaskInputPersonalized.DEFAULT_WHITE_SPACE, MaskInputPersonalized.DEFAULT_REGEX));
        }

        int[] position_cnpj = new int[]{0, 1, 3, 4, 5, 7, 8, 9, 11, 12, 13, 14, 16, 17, 18};
        assertEquals(possible_cnpj.length, position_cnpj.length);

        for (int i = 0; i < possible_cnpj.length; i++) {
            assertEquals(position_cnpj[i], MaskInputPersonalized.positionCursor(possible_cnpj[i],
                    MaskInputPersonalized.DEFAULT_WHITE_SPACE, MaskInputPersonalized.DEFAULT_REGEX));
        }

        int[] position_cep = new int[]{0, 1, 2, 3, 4, 6, 7, 8, 9};
        assertEquals(possible_cep.length, position_cep.length);

        for (int i = 0; i < possible_cep.length; i++) {
            assertEquals(position_cep[i], MaskInputPersonalized.positionCursor(possible_cep[i],
                    MaskInputPersonalized.DEFAULT_WHITE_SPACE, MaskInputPersonalized.DEFAULT_REGEX));
        }
    }


    /**
     * Testa o REGEX Padrão dos Inputs
     */
    @Test
    public void testRegexMask() {
        // Itens que fazem parte do Regex
        String[] removed_for_regex = new String[]{" ", "-", "/", ".", "#", "_", " -/.#_"};
        for (String item : removed_for_regex) {
            assertEquals("", item.replaceAll(MaskInputPersonalized.DEFAULT_REGEX, ""));
        }

        // Itens que não fazem parte do Regex
        String[] not_removed_regex = new String[]{"asdas", "846", "****", "+++", ",,,", "===", "d0sd"};
        for (String item : not_removed_regex) {
            assertEquals(item, item.replaceAll(MaskInputPersonalized.DEFAULT_REGEX, ""));
        }
    }

}