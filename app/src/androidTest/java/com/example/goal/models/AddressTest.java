package com.example.goal.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.goal.R;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;

public class AddressTest {

    private Context context;
    private Address address;
    private String[] array_states;
    private String[] array_uf;
    private String[] array_cep_valid;
    private String[] array_cep_invalid;

    /**
     * Instancia os Items (Variaveis, Classes) que serão usados
     */
    @Before
    public void instanceItens() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        address = new Address(context);
        array_states = context.getResources().getStringArray(R.array.state);
        array_uf = context.getResources().getStringArray(R.array.uf);
        array_cep_valid = new String[]{"69313035", "69099096", "77015387", "97037174"};
        array_cep_invalid = new String[]{"148100055", "999999999", "100000000", "0", "", " ",
                "gyasdha", "05 6 6 6"};

    }

    /**
     * Realiza a Verificação dos Pre-requisitos do CEP
     */
    @Test
    public void validationCEP() {
        for (String item : array_cep_valid) {
            assertTrue(address.validationCEP(item));
        }

        for (String item : array_cep_invalid) {
            assertFalse(address.validationCEP(item));
        }
    }

    /**
     * Realiza a verificação do CEP com o Endereço, Bairro e Estado
     */
    @Test
    public void checkCEP() {
        String[] array_address_cep = new String[]{"Rua Korak", "Avenida Coronel Sávio Belota",
                "ARSO 31 Alameda 22", "Rua Povos Romanos"};
        String[] array_state_cep = new String[]{"Roraima", "Amazonas", "Tocantins",
                "Rio Grande do Sul"};
        String[] array_district_cep = new String[]{"Jóquei Clube", "Novo Aleixo",
                "Plano Diretor Sul", "Nova Santa Marta"};

        // Testa os CEPs e seus Endereços respectivamente
        for (int i = 0; i < array_cep_valid.length; i++) {
            Address address_cep = new Address(context);

            address_cep.setCep(array_cep_valid[i]);
            address_cep.setState(address_cep.getUF(array_state_cep[i]));
            address_cep.setAddress(array_address_cep[i]);
            address_cep.setDistrict(array_district_cep[i]);

            assertTrue(address.checkCEP(Executors.newSingleThreadExecutor(), address_cep));
        }

        // Testa os CEPs Invalidos com os Endereços Validos
        for (String s : array_cep_invalid) {
            Address address_cep = new Address(context);

            address_cep.setCep(s);
            for (int i = 0; i < array_address_cep.length; i++) {
                address_cep.setState(address_cep.getUF(array_state_cep[i]));
                address_cep.setAddress(array_address_cep[i]);
                address_cep.setDistrict(array_district_cep[i]);
            }

            assertFalse(address.checkCEP(Executors.newSingleThreadExecutor(), address_cep));
        }
    }

    /**
     * Testa o metodo de obter a Unidade Federativa pelo Estado
     */
    @Test
    public void getUF() {
        // Compara se tem o mesmo tamanho
        assertEquals(array_states.length, array_uf.length);

        // Verifica se o Estado está compativel com a UF
        for (int i = 0; i < array_states.length; i++) {
            String state = array_states[i];
            String uf = address.getUF(array_states[i]);

            assertNull(address.getUF(state + " "));
            assertNull(address.getUF(state.substring(0, 3)));
            assertNull(address.getUF(state + "null"));

            assertNotNull(address.getUF(state));
            assertEquals(uf, array_uf[i]);
        }
    }

    /**
     * Testa a obtenção de Municipios do Estado
     */
    @Test
    public void arrayCities() {
        for (String item : array_states) {
            assertNull(address.getCities(Executors.newSingleThreadExecutor(), address.getUF(item + " ")));
            assertNull(address.getCities(Executors.newSingleThreadExecutor(), address.getUF(item.substring(0, 3))));
            assertNull(address.getCities(Executors.newSingleThreadExecutor(), address.getUF(null)));
            assertNull(address.getCities(Executors.newSingleThreadExecutor(), address.getUF("null")));
            assertNull(address.getCities(Executors.newSingleThreadExecutor(), address.getUF("")));
            assertNull(address.getCities(Executors.newSingleThreadExecutor(), address.getUF(" ")));

            assertNotNull(address.getCities(Executors.newSingleThreadExecutor(), address.getUF(item)));
        }
    }
}