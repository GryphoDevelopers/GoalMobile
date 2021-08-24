package com.example.goal.models;

import android.content.Context;

import com.example.goal.R;

import java.util.Arrays;
import java.util.List;

public class Address {

    // Constantes Usadas nos Erros
    public static final String OK = "Campo Validado";
    private static final String INPUT_NULL = "Campo Obrigatorio";
    private static final String INPUT_MIN_LENGHT = "%1$s deve ter no Minimo %2$s Caracteres";
    private static final String INPUT_MAX_LENGHT = "%1$s deve ter no Maximo %2$s Caracteres";
    private static final String INPUT_NOT_FORMAT = "%1$s deve ter conter apenas %2$s";

    // Atrobitos da Classe
    private Context context;
    private String country;
    private String state;
    private String city;
    private String address;
    private String district;
    private String complement;
    private int number;
    private String cep;

    public Address() {
    }

    public Address(Context context) {
        this.context = context;
    }

    public String validationCountry(String country) {
        List<String> countries_list =
                Arrays.asList(context.getResources().getStringArray(R.array.pays));

        if (country == null || country.equals("")) {
            return INPUT_NULL;
        } else if (countries_list.contains(country)) {
            return "País Invalido. Escolha um País Valido";
        } else return OK;
    }

    public String validationState(Address address) {
        List<String> states_list =
                Arrays.asList(context.getResources().getStringArray(R.array.state));

        String state = address.getState();

        if (state == null || state.equals("")) {
            return INPUT_NULL;
        } else if (address.getCountry().equals("Estrangeiro")) {
            // Validação do Estado Digitado pelo Usuario
            if (state.length() < 5) {
                return String.format(INPUT_MIN_LENGHT, "Estado", 5);
            } else if (state.length() > 100) {
                return String.format(INPUT_MAX_LENGHT, "Estado", 100);
            } else return OK;
        } else if (states_list.contains(state)) {
            return "Estado Invalido. Escolha um Estado Valido";
        } else return OK;
    }

    public String validationCity(Address address) {
        List<String> states_list =
                Arrays.asList(context.getResources().getStringArray(R.array.state));

        String city = address.getState();

        if (city == null || city.equals("")) {
            return INPUT_NULL;
        } else if (address.getCountry().equals("Estrangeiro")) {
            // Validação do Estado Digitado pelo Usuario
            if (city.length() < 5) {
                return String.format(INPUT_MIN_LENGHT, "Cidade", 5);
            } else if (city.length() > 100) {
                return String.format(INPUT_MAX_LENGHT, "Cidade", 100);
            } else return OK;
        } else return OK;
    }


    public String validationAddress(String address) {

        if (address == null || address.equals("")) {
            return INPUT_NULL;
        } else if (address.length() < 3) {
            return String.format(INPUT_MIN_LENGHT, "Endereço", 3);
        } else if (address.length() > 120) {
            return String.format(INPUT_MAX_LENGHT, "Endereço", 200);
        } else if (address.matches("^[A-ZÀ-úà-úa-zçÇ\\s]*")) {
            return String.format(INPUT_NOT_FORMAT, "Endereço", "Letras");
        } else return OK;
    }

    // validação do Bairro
    public String validationDistrict(String district) {

        if (district == null || district.equals("")) {
            return INPUT_NULL;
        } else if (district.length() < 3) {
            return String.format(INPUT_MIN_LENGHT, "Bairro", 3);
        } else if (district.length() > 120) {
            return String.format(INPUT_MAX_LENGHT, "Bairro", 80);
        } else if (district.matches("^[A-ZÀ-úà-úa-zçÇ\\s]*")) {
            return String.format(INPUT_NOT_FORMAT, "Bairro", "Letras");
        } else return OK;
    }

    public String validationNumber(int number) {
        if (number == 0) {
            return INPUT_NULL;
        } else if (number < 0) {
            return String.format(INPUT_NOT_FORMAT, "Numero", "Numeros Positivos");
        } else if (number > 1000000) {
            return String.format(INPUT_NOT_FORMAT, "Numero", "Numeros até 1000000");
        } else return OK;
    }

    // Validação do Complemento ---> Dado Opicional
    public String validationComplement(String district) {

        if (district == null || district.equals("")) {
            return OK;
        } else {
            if (district.length() < 5) {
                return String.format(INPUT_MIN_LENGHT, "Complemento", 5);
            } else if (district.length() > 120) {
                return String.format(INPUT_MAX_LENGHT, "Complemento", 80);
            } else if (district.matches("^[A-ZÀ-úà-úa-zçÇ,\\-\\s]*")) {
                return String.format(INPUT_NOT_FORMAT, "Complemento", "Letras, Virgulas ou Hifen ");
            } else return OK;
        }
    }

    public String validationCEP(Address address) {

        String cep = address.getCep();

        if (cep == null || cep.equals("")) {
            return INPUT_NULL;
        } else if (Integer.parseInt(cep) < 0) {
            return String.format(INPUT_NOT_FORMAT, "CEP", "Numeros Positivos");
        } else if (cep.length() != 8 || Integer.parseInt(cep) > 100000000) {
            return String.format(INPUT_NOT_FORMAT, "CEP", "Numeros no Formato 'xxxxx-xxx'");
        } else if (!checkCEP(address)) {
            return "CEP Invalido";
        } else return OK;
    }


    // Todo: implementar validação do CEP (API EXTERNA) com o Endereço Informado
    private boolean checkCEP(Address address) {
        return true;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
