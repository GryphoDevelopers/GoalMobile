package com.example.goal.models;

import android.content.Context;

import com.example.goal.R;

import java.util.Arrays;
import java.util.List;

/**
 * Classe Address: Responsavel por obter e manipular os dados de localização do Usuario
 */
public class Address {

    // Constantes Usadas nos Erros
    private final String INPUT_NULL;
    private final String INPUT_MIN_LENGTH;
    private final String INPUT_MAX_LENGTH;
    private final String INPUT_NOT_FORMAT;
    private final String INPUT_INVALID;

    // Atributos da Classe
    private final Context context;
    private String country;
    private String state;
    private String city;
    private String address;
    private String district;
    private String complement;
    private int number;
    private String cep;

    private String error_validation;

    /**
     * Contrutor da Classe Addres
     *
     * @param context Context é usado para obter as Strings de Erro
     */
    public Address(Context context) {
        this.context = context;

        // Obtem as Strings de Erro
        INPUT_NULL = context.getString(R.string.validation_empty);
        INPUT_MIN_LENGTH = context.getString(R.string.validation_min_length);
        INPUT_MAX_LENGTH = context.getString(R.string.validation_max_length);
        INPUT_NOT_FORMAT = context.getString(R.string.validation_format_char);
        INPUT_INVALID = context.getString(R.string.validation_not_disponible);
    }

    /**
     * Valida o País Informado
     *
     * @param country País informado pelo Usuario
     * @return true/false
     */
    public boolean validationCountry(String country) {
        String[] countries = context.getResources().getStringArray(R.array.pays);

        if (country == null || country.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        }

        // Busca no Array se Existe a Opção Passada
        for (String itemCountry : countries) {
            if (itemCountry.equals(country)) return true;
        }

        error_validation = INPUT_NULL;
        return false;
    }

    /**
     * Valida o Estado passado pelo Usuario
     *
     * @param address Instancia da Classe Addres, onde será usado o Country e State
     * @return true/false
     */
    public boolean validationState(Address address) {
        List<String> states_list =
                Arrays.asList(context.getResources().getStringArray(R.array.state));

        String state = address.getState();

        if (state == null || state.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (address.getCountry().equals("Estrangeiro")) {
            // Validação do Estado Digitado pelo Usuario
            if (state.length() < 5) {
                error_validation = String.format(INPUT_MIN_LENGTH, "Estado", 5);
                return false;
            } else if (state.length() > 100) {
                error_validation = String.format(INPUT_MAX_LENGTH, "Estado", 100);
                return false;
            } else return true;
        } else if (!states_list.contains(state)) {
            error_validation = String.format(INPUT_INVALID, "Estado");
            return false;
        } else return true;
    }

    /**
     * Valida a cidade informada
     *
     * @param address Instancia da Classe Address, onde será usado o Country e City
     * @return true/false
     */
    public boolean validationCity(Address address) {
        String city = address.getCity();
        if (city == null || city.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (address.getCountry().equals("Estrangeiro")) {
            // Validação do Estado Digitado pelo Usuario
            if (city.length() < 5) {
                error_validation = String.format(INPUT_MIN_LENGTH, "Cidade", 5);
                return false;
            } else if (city.length() > 100) {
                error_validation = String.format(INPUT_MAX_LENGTH, "Cidade", 100);
                return false;
            } else return true;
        } else return true;
    }

    /**
     * Valida o Endereço Informado
     *
     * @param address String do Endereço informada
     * @return true/false
     */
    public boolean validationAddress(String address) {
        if (address == null || address.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (address.length() < 3) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Endereço", 3);
            return false;
        } else if (address.length() > 120) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Endereço", 200);
            return false;
        } else if (!address.matches("^[A-ZÀ-úà-úa-zçÇ\\s]*")) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Endereço", "Letras");
            return false;
        } else return true;
    }

    /**
     * Valida o Bairro Informado
     *
     * @param district Bairro que foi Informado
     * @return true/false
     */
    public boolean validationDistrict(String district) {
        if (district == null || district.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (district.length() < 3) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Bairro", 3);
            return false;
        } else if (district.length() > 120) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Bairro", 80);
            return false;
        } else if (!district.matches("^[A-ZÀ-úà-úa-zçÇ\\s]*")) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Bairro", "Letras");
            return false;
        } else return true;
    }

    /**
     * Valida o Numero do Endereço Informado
     *
     * @param number Numero Informado
     * @return true/false
     */
    public boolean validationNumber(int number) {
        if (number == 0) {
            error_validation = INPUT_NULL;
            return false;
        } else if (number < 0) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Numero", "Numeros Positivos");
            return false;
        } else if (number > 1000000) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Numero", "Numeros até 1000000");
            return false;
        } else return true;
    }

    /**
     * Validação do Complemento Informado. Esse é um dado Opcional
     *
     * @param complement Complemento (Apartamento, Bloco, Fundo, A, B, etc) do Endereço
     * @return true/false
     */
    public boolean validationComplement(String complement) {
        if (complement == null || complement.equals("")) {
            return true;
        } else {
            // Caso Preenchido, tem que ser validado
            if (complement.length() < 5) {
                error_validation = String.format(INPUT_MIN_LENGTH, "Complemento", 5);
                return false;
            } else if (complement.length() > 120) {
                error_validation = String.format(INPUT_MAX_LENGTH, "Complemento", 80);
                return false;
            } else if (!complement.matches("^[A-ZÀ-úà-úa-zçÇ,\\-\\s]*")) {
                error_validation = String.format(INPUT_NOT_FORMAT, "Complemento",
                        "Letras, Virgulas ou Hifen ");
                return false;
            } else return true;
        }
    }

    /**
     * Valida o CEP Informado
     *
     * @param cep CEP Informado pelo Usuario
     * @return true/false
     */
    public boolean validationCEP(String cep) {
        if (cep == null || cep.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (Integer.parseInt(cep) < 0) {
            error_validation = String.format(INPUT_NOT_FORMAT, "CEP", "Numeros Positivos");
            return false;
        } else if (cep.length() != 8 || Integer.parseInt(cep) > 100000000) {
            error_validation = String.format(INPUT_NOT_FORMAT, "CEP", "Numeros no Formato 'XXXXXXXX'");
            return false;
        } else return true;
    }

    /**
     * Verifica o CEP com o Endereço Informado. Esse é um metodo Independente da Validação do CEP
     *
     * @param address Instancia da Classe Address, que será usado para Comparar os Dados do CEP com
     *                o endereço Informado
     * @return true/false
     */
    public boolean checkCEP(Address address) {
        // Todo: implementar validação do CEP (API EXTERNA) com o Endereço Informado
        return true;
    }


    // Getters e Setters da Calsse
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

    // Obtem os Erros das Etapas de Validação
    public String getError_validation() {
        return error_validation;
    }

}