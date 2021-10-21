package com.example.goal.models;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.goal.R;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AddressTest {

    private Address address;
    private String[] array_states;
    private String[] array_uf;

    /**
     * Instancia os Items (Variaveis, Classes) que serão usados
     */
    @Before
    public void instanceItens() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        address = new Address(context);
        array_states = context.getResources().getStringArray(R.array.state);
        array_uf = context.getResources().getStringArray(R.array.uf);

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
            String uf = address.getUF(array_states[i]);
            assertEquals(uf, array_uf[i]);
        }
    }

    /**
     * Testa a obtenção de Municipios do Estado
     */
    @Test
    public void arrayCities() {
        for (String item : array_states) {
            String[] cities = address.getCities(address.getUF(item));
            assertNotEquals(null, cities);
        }
    }
}