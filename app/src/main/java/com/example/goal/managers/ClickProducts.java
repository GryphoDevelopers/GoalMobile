package com.example.goal.managers;

import com.example.goal.models.Product;

/**
 * Interface ClickProduct: Interface que controla a manipulação dos cliques nos Produtos
 */
public interface ClickProducts {
    /**
     * Metodo que recebe o {@link Product} que foi Sellecionado
     *
     * @param product {@link Product} selecionado
     */
    void clickProduct(Product product);

    /**
     * Metodo que recebe o {@link Product} que foi Sellecionado para ser Excluido
     *
     * @param product {@link Product} selecionado
     */
    void deleteProduct(Product product);

    /**
     * Metodo que recebe o {@link Product} que foi Sellecionado
     *
     * @param product {@link Product} selecionado
     */
    void updateProduct(Product product);
}
