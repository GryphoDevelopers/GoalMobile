package com.example.goal.models;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.SearchInternet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Classe Address: Responsavel por obter e manipular os dados de localização do Usuario
 */
public class Address {

    // Constantes de Possiveis Exception usadads nos Logs
    private final String NAME_CLASS = "Address";
    private final String EXECUTION_EXCEPTION = "Exception Execution";
    private final String INTERRUPTED_EXCEPTION = "Exception Interrupted";
    private final String EXCEPTION = "Exception";

    // Constantes Usadas nos Erros
    private final String INPUT_NULL;
    private final String INPUT_MIN_LENGTH;
    private final String INPUT_MAX_LENGTH;
    private final String INPUT_NOT_FORMAT;
    private final String INPUT_INVALID;
    private final String CEP_INVALID;
    private final String MESSAGE_EXCEPTION;

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
        INPUT_INVALID = context.getString(R.string.validation_unavailable);
        CEP_INVALID = Html.fromHtml(context.getString(R.string.validation_invalid_cep)).toString();
        MESSAGE_EXCEPTION = context.getString(R.string.error_exception);
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
        } else if (!address.matches("^[0-9A-ZÀ-úà-úa-zçÇ\\s]*")) {
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
            } else if (!complement.matches("^[0-9A-ZÀ-úà-úa-zçÇ,\\-\\s]*")) {
                error_validation = String.format(INPUT_NOT_FORMAT, "Complemento",
                        "Letras, Virgulas ou Hifen ");
                return false;
            } else return true;
        }
    }

    /**
     * Valida o CEP Informado (Tamanho e Formato)
     *
     * @param cep CEP Informado pelo Usuario
     * @return true/false
     */
    public boolean validationCEP(String cep) {
        try {
            int cep_convert = Integer.parseInt(cep);

            if (cep.equals("") || cep_convert == 0) {
                error_validation = INPUT_NULL;
                return false;
            } else if (Integer.parseInt(cep) < 0) {
                error_validation = String.format(INPUT_NOT_FORMAT, "CEP", "Numeros Positivos");
                return false;
            } else if (cep.length() != 8) {
                error_validation = String.format(INPUT_INVALID, "CEP");
                return false;
            } else if (cep_convert >= 100000000) {
                error_validation = String.format(INPUT_NOT_FORMAT, "CEP", "8 Numeros");
                return false;
            } else return true;
        } catch (Exception ex) {
            Log.e(EXCEPTION, NAME_CLASS + " - Houve um erro na Conversão do CEP");
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica o CEP com o Endereço Informado. Esse é um metodo Independente da Validação do CEP.
     * Ele precisa do Endereço (Avenida/Rua), Bairro e Estado
     *
     * @param address Instancia da Classe Address, que será usado para Comparar os Dados do CEP com
     *                o endereço Informado
     * @return true/false
     */
    public boolean checkCEP(Address address) {
        try {
            // URI de Pesquisa
            Uri build_uri = Uri.parse(SearchInternet.API_BRAZIL_CEP)
                    .buildUpon().appendPath(address.getCep()).build();

            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                String json_cep = searchInternet.SearchInAPI(build_uri.toString(), "GET");
                if (json_cep == null) error_validation = searchInternet.getError_search();
                return json_cep;
            });

            // Executa as Tarefas Assincornas
            List<Future<String>> futureTasksList = executorService.invokeAll(callable);
            String json_cep = futureTasksList.get(0).get();

            if (json_cep != null) {
                // Obtem o JSON da API
                SerializationInfos serializationInfos = new SerializationInfos(context);
                String[] infos_cep = serializationInfos.jsonToArray(json_cep,
                        new String[]{"street", "neighborhood", "state"});

                // Obtem uma String[] serializada
                if (infos_cep != null) {
                    // Deixa em Letras minusculas a Serialização
                    for (int i = 0; i < infos_cep.length; i++) {
                        infos_cep[i] = infos_cep[i].toLowerCase();
                    }
                    // Valida os Dados
                    if (infos_cep[0].equals(address.getAddress().toLowerCase()) &&
                            infos_cep[1].equals(address.getDistrict().toLowerCase()) &&
                            infos_cep[2].equals(address.getState().toLowerCase())) {
                        return true;
                    } else error_validation = String.format(CEP_INVALID, infos_cep);
                } else error_validation = serializationInfos.getError_operation();
            }

        } catch (InterruptedException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(INTERRUPTED_EXCEPTION, NAME_CLASS + " - Tarefa Assincrona Interrompida");
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(EXECUTION_EXCEPTION, NAME_CLASS + " - Falha na Execução da Tarefa Assincrona");
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Retorna a UF do Estado a partir de uma Lista (array) onde os estados e UF estão na mesma posição
     *
     * @param state UF do Estado esfecificada
     * @return String/null
     */
    public String getUF(String state) {
        try {
            List<String> array_state = Arrays.asList(context.getResources().getStringArray(R.array.state));
            int positionOfArray = array_state.indexOf(state);
            return context.getResources().getStringArray(R.array.uf)[positionOfArray];
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(EXCEPTION, NAME_CLASS + " - Houve um erro ao Obter a UF");
            error_validation = String.format(INPUT_INVALID, "Estado");
            return null;
        }
    }

    /**
     * Obtem uma Lista (em Ordem Alfabetica) de Municipios a partir do Estado esppecificado
     *
     * @param uf Unidade Federativa do Estado das Cidades que serão obtidas
     * @return String[]|null
     */
    public String[] getCities(String uf) {
        try {
            if (uf == null || uf.equals("") || uf.length() != 2) {
                error_validation = String.format(INPUT_INVALID, "Estado/UF");
                return null;
            }

            // URI de Pesquisa
            Uri build_uri_cities = Uri.parse(SearchInternet.API_BRAZIL_CITY)
                    .buildUpon().appendPath(uf).build();

            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            // Configura a Tarefa Assincrona que Retorna uma String (JSON)
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                SearchInternet searchInternet = new SearchInternet(context);
                String json_cities = searchInternet.SearchInAPI(build_uri_cities.toString(), "GET");
                if (json_cities == null) error_validation = searchInternet.getError_search();
                return json_cities;
            });

            // Executa as Tarefas Assincornas
            List<Future<String>> futureTasksList = executorService.invokeAll(callable);
            String json_cities = futureTasksList.get(0).get();

            // Valida o JSON da API
            if (json_cities != null) {
                SerializationInfos serializationInfos = new SerializationInfos(context);
                String[] array_cities = serializationInfos.serializationCities(json_cities);

                // Obtem o array serializado e Ordena por ordem alfabetica
                if (array_cities != null) {
                    Arrays.sort(array_cities);
                    return array_cities;
                } else error_validation = serializationInfos.getError_operation();
            }

        } catch (InterruptedException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(INTERRUPTED_EXCEPTION, NAME_CLASS + " - Tarefa Assincrona Interrompida");
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(EXECUTION_EXCEPTION, NAME_CLASS + " - Falha na Execução da Tarefa Assincrona");
            ex.printStackTrace();
        }
        return null;
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