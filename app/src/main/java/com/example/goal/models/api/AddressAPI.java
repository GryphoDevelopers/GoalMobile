package com.example.goal.models.api;

import android.content.Context;

import com.example.goal.models.Address;

import java.util.concurrent.ExecutorService;

public class AddressAPI {
    private final Context context;

    private final String error_operation = "";

    /**
     * Cosntrutor da Classe AddressAPI
     *
     * @param context Usado na manipulação de itens na Classe
     */
    public AddressAPI(Context context) {
        this.context = context;
    }

    /**
     * Insere o Endereço passado na API GOAL
     *
     * @param executorService {@link ExecutorService} necessario para realizar as consultas na API
     *                        para obter as cidades e manter na mesma Thread Assincrona utilizada
     *                        nas Activity
     * @return true|false
     */
    public boolean insertAddress(ExecutorService executorService, Address address) {
        // todo adicionar inserção do endereço na API
        return true;
    }

    // Obtem a Mensagem de Erro
    public String getError_operation() {
        return error_operation;
    }

}
