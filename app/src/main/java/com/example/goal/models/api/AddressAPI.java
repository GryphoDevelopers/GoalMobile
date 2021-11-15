package com.example.goal.models.api;

import android.content.Context;

import com.example.goal.models.Address;

public class AddressAPI {
    private final Context context;

    private String error_operation = "";

    public AddressAPI(Context context) {
        this.context = context;
    }

    public boolean insertAddress(Address address) {
        // todo adicionar inserção do endereço na API
        return true;
    }


    // Getters e Setters
    public String getError_operation() {
        return error_operation;
    }

    public void setError_operation(String error_operation) {
        this.error_operation = error_operation;
    }

}
