package com.example.goal.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProductsTest {

    private Product product;

    /**
     * Instancia os Itens que serão usados
     */
    @Before
    public void instanceItems() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        product = new Product(context);
    }

    /**
     * Teste de Validação do Nome de Produtos
     */
    @Test
    public void validationName() {
        String[] valid_names = new String[]{
                "Roupa Atletiva Feminina - Modelo 506A425.", "Bola de Futebol 18cm",
                "Bicicleta Dropp Z3 Aro 29 Câmbios Shimano 21 Marchas Freio a Disco Mecânico com Suspensão - Preto+Vermelho",
                "Kit 5 Mini Bands Funcional Faixa Elástica Thera Band MBFit - Preto+verde",
                "Kit Colchonete + Par de Caneleira 1kg Profissional + Par de Halter emborrachado 1kg - Preto",
                "Calção de Academia", "Eletrólitos pós Treino"
        };

        String[] invalid_names = new String[]{
                "Produto Imperdivel ! Roupa vermleha", "Deseja um novo tenis ? Tenha esse agora",
                "Min", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce a dolor " +
                "arcu. Proin pretium, erat in aliquet convallis, nisi eros finibus leo, ut scelep",
                " ", ""
        };

        // Valida os Nomes Aceitos
        assertTrue(product.validationName(valid_names[0]));
        assertTrue(product.validationName(valid_names[1]));
        assertTrue(product.validationName(valid_names[2]));
        assertTrue(product.validationName(valid_names[3]));
        assertTrue(product.validationName(valid_names[4]));
        assertTrue(product.validationName(valid_names[5]));
        assertTrue(product.validationName(valid_names[6]));

        // Valida os Nomes Invalidos
        assertFalse(product.validationName(invalid_names[0]));
        assertFalse(product.validationName(invalid_names[1]));
        assertFalse(product.validationName(invalid_names[2]));
        assertFalse(product.validationName(invalid_names[3]));
        assertFalse(product.validationName(invalid_names[4]));
        assertFalse(product.validationName(invalid_names[5]));
    }

    /**
     * Valida o Preço dos Produtos
     */
    @Test
    public void validationPrice() {
        String[] prices_valid = new String[]{"5656.65", "656.45", "545.2", "3.00", "10", "65416",
                "5.50", "3.63", "34.5", "999999999.99"};
        String[] prices_invalid = new String[]{"0", "2.99", "1000000001.01", "-1.6", "-3.65", "", " ",
                "-85456.00", "das23", "312983fjafas", "38912j", "652,65", " 62.65"};

        assertTrue(product.validationPrice(prices_valid[0]));
        assertTrue(product.validationPrice(prices_valid[1]));
        assertTrue(product.validationPrice(prices_valid[2]));
        assertTrue(product.validationPrice(prices_valid[3]));
        assertTrue(product.validationPrice(prices_valid[4]));
        assertTrue(product.validationPrice(prices_valid[5]));
        assertTrue(product.validationPrice(prices_valid[6]));
        assertTrue(product.validationPrice(prices_valid[7]));
        assertTrue(product.validationPrice(prices_valid[8]));
        assertTrue(product.validationPrice(prices_valid[9]));

        assertFalse(product.validationPrice(null));
        assertFalse(product.validationPrice(prices_invalid[0]));
        assertFalse(product.validationPrice(prices_invalid[1]));
        assertFalse(product.validationPrice(prices_invalid[2]));
        assertFalse(product.validationPrice(prices_invalid[3]));
        assertFalse(product.validationPrice(prices_invalid[4]));
        assertFalse(product.validationPrice(prices_invalid[5]));
        assertFalse(product.validationPrice(prices_invalid[6]));
        assertFalse(product.validationPrice(prices_invalid[7]));
        assertFalse(product.validationPrice(prices_invalid[8]));
        assertFalse(product.validationPrice(prices_invalid[9]));
        assertFalse(product.validationPrice(prices_invalid[10]));
        assertFalse(product.validationPrice(prices_invalid[11]));
        assertFalse(product.validationPrice(prices_invalid[12]));
    }


    /**
     * Valida o Estoque dos Produtos
     */
    @Test
    public void validationStock() {
        String[] stock_valids = new String[]{"1", "3", "65", "20", "0245", "03"};
        String[] stock_invalids = new String[]{"0", "-2", " ", "20", "0245", "03"};

        assertTrue(product.validationStock(stock_valids[0]));
        assertTrue(product.validationStock(stock_valids[1]));
        assertTrue(product.validationStock(stock_valids[2]));
        assertTrue(product.validationStock(stock_valids[3]));
        assertTrue(product.validationStock(stock_valids[4]));
        assertTrue(product.validationStock(stock_valids[5]));

        assertFalse(product.validationStock(null));
        assertFalse(product.validationStock(stock_valids[0]));
        assertFalse(product.validationStock(stock_valids[1]));
        assertFalse(product.validationStock(stock_valids[2]));
        assertFalse(product.validationStock(stock_valids[3]));
        assertFalse(product.validationStock(stock_valids[4]));
        assertFalse(product.validationStock(stock_valids[5]));

    }

    public void validationDescription() {
    }

}
