package com.example.goal.models;

import static com.example.goal.managers.SearchInternet.GET;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.managers.SearchInternet;
import com.example.goal.views.widgets.MaskInputPersonalized;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Classe User: Classe Usada para manipular as informações dos Usuario (Clientes e Vendedores)
 */
public class User {
    // Constantes Usadas na manipulação da Data
    public static final String TIME_ZONE = "America/Sao_Paulo";
    public static final Locale LOCALE_BR = new Locale("pt", "BR");
    public static final String PATTERN_DATE = "dd/MM/yyyy";
    // Constantes da Força da Senha
    private static final int LOW_STRENGTH = 1;
    private static final int OK_STRENGTH = 2;
    // Constantes de Possiveis Exception usadads nos Logs
    private final String NAME_CLASS = "User";
    private final String EXECUTION_EXCEPTION = "Exception Execution";
    private final String INTERRUPTED_EXCEPTION = "Exception Interrupted";
    private final String EXCEPTION_GLOBAL = "Exception General";
    // Constantes Usadas nos Erros
    private final String INPUT_NULL;
    private final String INPUT_MIN_LENGTH;
    private final String INPUT_MAX_LENGTH;
    private final String INPUT_NOT_CHARS_ACCEPT;
    private final String INPUT_NOT_FORMAT;
    private final String EMAIL_DISPOSABLE;
    private final String CNPJ_INVALID;
    private final String MESSAGE_EXCEPTION;
    private final String INVALID_DDD;
    private final String INPUT_INVALID_AGE;
    private final String INPUT_INVALID;
    // Variavies Usadas já inicializadas
    private final Context context;
    private String error_validation = "";
    private Date date_birth = null;
    private String name = "";
    //todo: atlerar e utilizar a variavel de sobrenome
    private String last_name = "TEste";
    private String email = "";
    private String nickname = "";
    private String cpf = "";
    private String password = "";
    private String confirmPassword = "";
    private String phone = "";
    private String cnpj = "";
    // todo: alterar o tipo String para GUID (Id_user)
    private String id_user = "";
    private boolean isSeller = false;

    /**
     * Construtor da Classe User
     *
     * @param context Context é utilizado para obter Strings pré-definidas de Erro
     */
    public User(Context context) {
        this.context = context;

        // Obtem as Strings de Erro
        INPUT_NULL = context.getString(R.string.validation_empty);
        INPUT_MIN_LENGTH = context.getString(R.string.validation_min_length);
        INPUT_MAX_LENGTH = context.getString(R.string.validation_max_length);
        INPUT_NOT_CHARS_ACCEPT = context.getString(R.string.validation_format_char);
        INPUT_NOT_FORMAT = context.getString(R.string.validation_format);
        EMAIL_DISPOSABLE = Html.fromHtml(context.getString(R.string.validation_disposable_email)).toString();
        CNPJ_INVALID = Html.fromHtml(context.getString(R.string.validation_invalid_cnpj)).toString();
        MESSAGE_EXCEPTION = context.getString(R.string.error_exception);
        INVALID_DDD = Html.fromHtml(context.getString(R.string.validation_invalid_ddd)).toString();
        INPUT_INVALID_AGE = context.getString(R.string.validation_age);
        INPUT_INVALID = context.getString(R.string.validation_unavailable);
    }

    /**
     * Compara as Informações entre dois Usuarios. Essa comparação, será armazenada no novo Usuario
     * que conterá as novas informações em comparação com o Antigo Usuario
     */
    public static User compareUser(User oldUser, User newUser) {

        // Verifica para cada Propriedade do Usuario se foi preenchida
        if (newUser.getId_user().equals("")) newUser.setId_user(oldUser.getId_user());
        if (newUser.getName().equals("")) newUser.setName(oldUser.getName());
        if (newUser.getNickname().equals("")) newUser.setNickname(oldUser.getNickname());
        if (newUser.getEmail().equals("")) newUser.setEmail(oldUser.getEmail());
        if (newUser.getString_dateBirth().equals(""))
            newUser.setDate_birth(oldUser.getString_dateBirth());
        if (newUser.getUnmaskPhone().equals("")) newUser.setPhone(oldUser.getUnmaskPhone());
        if (newUser.getUnmaskCnpj().equals("")) newUser.setCnpj(oldUser.getUnmaskCnpj());
        if (newUser.getUnmaskCpf().equals("")) newUser.setCpf(oldUser.getUnmaskCpf());

        return newUser;
    }

    /**
     * Valida o Nome Baseando nos Parametros:
     * <p>
     * Tamanho de 3 a 80 Letras, com acentuação e espaços em branco
     *
     * @param name Nome Informado
     * @return true/false
     */
    public boolean validationName(String name) {
        if (name == null || name.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (name.length() < 3) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Nome", 3);
            return false;
        } else if (name.length() > 80) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Nome", 80);
            return false;
        } else if (!name.matches("^[A-ZÀ-úà-úa-zçÇ\\s]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Nome", "Letras");
            return false;
        } else return true;
    }

    /**
     * Valida o email informado nos seguintes parametros:
     * <p>
     * Tamanho de 15 à 120 Letras, numeros, pontos, underlines e @, mas sem espaços em branco
     *
     * @param email Email Informado
     * @return true/false
     */
    public boolean validationEmail(String email) {
        if (email == null || email.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (email.length() < 10) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Email", 10);
            return false;
        } else if (email.length() > 120) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Email", 120);
            return false;
        } else if (!email.matches("[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?")) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Email", "email@*****.XXX");
            return false;
        } else return true;
    }

    /**
     * Valida o email a partir de uma API. Esse é um metodo Independente da Validação do Email
     *
     * @param email Email utilizado na Validação
     * @return true/false
     */
    public boolean validationEmailAPI(String email) {
        try {
            // URI de Pesquisa
            Uri build_uri = Uri.parse(SearchInternet.API_EMAIL_DISPOSABLE)
                    .buildUpon().appendPath(email).build();

            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                String json_email = searchInternet.SearchInAPI(build_uri.toString(), GET, null);

                if (json_email == null) error_validation = searchInternet.getError_search();
                return json_email;
            });

            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_email = futureTasksList.get(0).get();

            if (json_email != null) {
                SerializationInfos serializationInfos = new SerializationInfos(context);
                String[] infos_email = serializationInfos.jsonStringToArray(json_email, new String[]{"disposable"});

                if (infos_email != null) {
                    if (infos_email[0].equals("false")) return true;
                    error_validation = EMAIL_DISPOSABLE;
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
        // Alguma etapa não foi bem sucedida
        return false;
    }

    /**
     * Valida o Nickname (Nome de Usario) informado nos seguintes parametros:
     * <p>
     * Tamanho de 5 à 60 Letras, podendo ter numeros, pontos, underlines, mas sem espaço em branco
     *
     * @param nickname Nickname (Nome de Usuario) Informaado
     * @return true/false
     */
    public boolean validationNickname(String nickname) {
        if (nickname == null || nickname.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (nickname.length() < 5) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Nome de Usuario", 5);
            return false;
        } else if (nickname.length() > 60) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Nome de Usuario", 60);
            return false;
        } else if (!nickname.matches("^[A-Za-z0-9._]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Nome de Usuario",
                    "Letras, Numeros, Underline ou Pontos");
            return false;
        } else return true;
    }

    /**
     * Valida o CPF informado. Somente é permitido numeros e esse CPF é validado pela API BRASIL
     *
     * @param cpf CPF informado
     * @return true/false
     */
    public boolean validationCpf(String cpf) {
        if (cpf != null && !cpf.equals("")) {
            if (cpf.length() != 14) {
                error_validation = String.format(INPUT_NOT_FORMAT, "CPF", "000.000.000-00");
                return false;
            }

            String cfp_unmask = MaskInputPersonalized.remove_mask(cpf, MaskInputPersonalized.DEFAULT_REGEX);
            if (cfp_unmask != null && !cfp_unmask.equals("")) {
                if (cfp_unmask.length() != 11) {
                    error_validation = String.format(INPUT_MIN_LENGTH, "CPF", "11 Numeros");
                    return false;
                } else if (!cfp_unmask.matches("^[0-9]*")) {
                    error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "CPF",
                            "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
                    return false;
                } else return true;
            }
        }

        // CPF com ou Sem Formatção são Invalidos
        error_validation = INPUT_NULL;
        return false;
    }

    /**
     * Valida o CPF em alguma API Externa. Este é um metodo Independente da Validação do CPF
     *
     * @param cpf CPF Informado
     * @return true/false
     */
    private boolean validationNumberCpf(String cpf) {
        // todo: Codigo de Validação do CPF
        return true;
    }

    /**
     * Valida apenas os Numeros e Tamanho do CNPJ
     *
     * @param cnpj CNPJ informado pelo Usuario
     * @return true/false
     */
    public boolean validationCnpj(String cnpj) {
        if (cnpj != null && !cnpj.equals("")) {
            if (cnpj.length() != 18) {
                error_validation = String.format(INPUT_NOT_FORMAT, "CNPJ", "00.000.000/000X-YY");
                return false;
            }

            String cnpj_unmask = MaskInputPersonalized.remove_mask(cnpj, MaskInputPersonalized.DEFAULT_REGEX);
            if (cnpj_unmask != null && !cnpj_unmask.equals("")) {
                if (cnpj_unmask.length() != 14) {
                    error_validation = String.format(INPUT_MIN_LENGTH, "CNPJ", "14 Numeros");
                    return false;
                } else if (!cnpj_unmask.matches("^[0-9]*")) {
                    error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "CNPJ",
                            "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
                    return false;
                } else return true;
            }
        }

        // CNPJ com ou sem Mascara é invalido
        error_validation = INPUT_NULL;
        return false;
    }

    /**
     * Valida o CNPJ na API BRASIL, Verificando se possui um cadastro na Receita Federal e tambem se
     * está ativo o CNPJ. Este é um metodo Independente da Validação do CNPJ
     *
     * @param cnpj CNPJ informado pelo Usuario
     * @return true/false
     */
    public boolean validationNumberCnpj(String cnpj) {
        try {
            String unmask_cnpj = "";
            if (cnpj != null && !cnpj.equals("")) {
                unmask_cnpj = MaskInputPersonalized.remove_mask(cnpj, MaskInputPersonalized.DEFAULT_REGEX);
            }

            if (unmask_cnpj == null || unmask_cnpj.equals("")) {
                // CNPJ com ou sem Mascara é invalido
                error_validation = INPUT_NULL;
                return false;
            }

            // URI de Pesquisa
            Uri build_uri = Uri.parse(SearchInternet.API_BRAZIL_CNPJ)
                    .buildUpon().appendPath(unmask_cnpj).build();

            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String>> callable = new HashSet<>();
            callable.add(() -> {
                SearchInternet searchInternet = new SearchInternet(context);
                String json_cnpj = searchInternet.SearchInAPI(build_uri.toString(), GET, null);

                if (json_cnpj == null) error_validation = searchInternet.getError_search();
                return json_cnpj;
            });

            List<Future<String>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String json_cnpj = futureTasksList.get(0).get();

            if (json_cnpj != null) {
                SerializationInfos serializationInfos = new SerializationInfos(context);
                String[] cnpj_reciver = serializationInfos.jsonStringToArray(json_cnpj,
                        new String[]{"cnpj", "descricao_situacao_cadastral"});

                if (cnpj_reciver != null) {
                    if (cnpj_reciver[0].equals(unmask_cnpj) && cnpj_reciver[1].equals("Ativa")) {
                        return true;
                    } else error_validation = CNPJ_INVALID;
                } else error_validation = serializationInfos.getError_operation();

            } else if (error_validation.equals(context.getString(R.string.error_generic))) {
                error_validation = CNPJ_INVALID;
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
        // Alguma etapa não foi bem sucedida
        return false;
    }

    /**
     * Valida a senha nos seguintes parametros
     * <p>
     * Tamanho de 5 à 40 Caracteres, porem sem espaços em branco. Tambem há uma validação na Força
     * da Senha inserida
     *
     * @param password Senha informada pelo Usuario
     * @return true/false
     */
    public boolean validationPassword(String password) {
        if (password == null || password.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (password.length() < 5) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Senha", 5);
            return false;
        } else if (password.length() > 40) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Senha", 40);
            return false;
        } else if (!password.matches("^[\\S]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Senha",
                    "Caracteres (Sem espaços em Branco)");
            return false;
        } else if (strengthPassword(password) == LOW_STRENGTH) {
            //TODO : mensagem de erro da senha
            error_validation = context.getString(R.string.validation_password,
                    "todo: alterar aqui");
            return false;
        } else return true;
    }

    /**
     * Retorna o da Força Valor da Senha Inserida
     *
     * @param password Senha informada
     * @return int (1 para fraca e 2 para forte)
     */
    private int strengthPassword(String password) {
        // todo: implementar requisitos  para força da senha
        return OK_STRENGTH;
    }

    /**
     * Valida a confirmação da senha informada. Passa pela mesma validação da senha anterior e
     * confere se as senhas são compativeis
     *
     * @param user Instancia da Classe Usuario que compara se as 2 senhas estão dentro dos
     *             parametros e são iguais
     * @return true/false
     */
    public boolean validationConfirmPassword(User user) {
        String confirmPassword = user.getConfirmPassword();
        String password = user.getPassword();

        if (confirmPassword == null || confirmPassword.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (validationPassword(confirmPassword)) {
            if (password.equals(confirmPassword)) return true;

            // As senhas são diferentes
            error_validation = context.getString(R.string.validation_equal_password);
            return false;
        } else return false;
        // No ultimo else, a mensagem de erro foi definido na "validationPassword()"
    }

    /**
     * Valida apenas a Formação e os Numero de Telefone do Usuario Brasileiro.
     *
     * @param phone Telefone Brasileiro Informado
     * @return true/false
     */
    public boolean validationBrazilianPhone(String phone) {
        if (phone != null && !phone.equals("")) {
            if (phone.length() != 15) {
                error_validation = String.format(INPUT_NOT_FORMAT, "Telefone", "(0XX) 90000-0000");
                return false;
            }

            String unmask_phone = MaskInputPersonalized.remove_mask(phone, MaskInputPersonalized.DEFAULT_REGEX);
            if (unmask_phone != null && !unmask_phone.equals("")) {
                if (unmask_phone.length() != 12) {
                    error_validation = String.format(INPUT_MIN_LENGTH,
                            "Telefone", "12 Digitos (DDD + Numero)");
                    return false;
                } else if (!unmask_phone.matches("^[0-9]*")) {
                    error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Telefone",
                            "Numeros (Sem Hifen, Ponto, Virgula, Sinais ou Espaços em Branco)");
                    return false;
                }

                // Verifica se o DDD está na lista dos DDDs Validos
                String ddd_phone = unmask_phone.substring(0, 3);
                String[] ddd_valid = ddd_valid();
                for (String item : ddd_valid) {
                    if (item.equals(ddd_phone)) return true;
                }

                error_validation = INVALID_DDD;
                return false;
            }
        }

        // Telefone Com ou Sem Formatação é invalido
        error_validation = INPUT_NULL;
        return false;
    }

    /**
     * Valida apenas a Formação e os Numero de Telefone do Usuario Estrangeiro.
     *
     * @param internation_phone Telefone do Usuario Estrangeiro
     * @return true/false
     */
    public boolean validationInternationPhone(String internation_phone) {
        if (internation_phone == null || internation_phone.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (!internation_phone.matches("^[0-9+]*")) {
            error_validation = String.format(INPUT_NOT_CHARS_ACCEPT, "Telefone",
                    "Numeros (Sem Hifen, Ponto, Virgula ou Espaços em Branco)");
            return false;
        } else return true;
    }

    /**
     * Valida a Idade do Usuario.
     * <p>
     * Formato: DD/MM/AAAA. Idade Minima = 13 e Idade Maxima = 100. Valida Ano Bissexto,
     * Meses/Dias Incorretos
     *
     * @param date_birth Data de Nascimento do Usuario
     * @return true/false
     */
    public boolean validationDateBirth(String date_birth) {
        try {
            if (date_birth == null || date_birth.equals("")) {
                error_validation = INPUT_NULL;
                return false;
            } else if (!date_birth.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
                error_validation = String.format(INPUT_NOT_FORMAT, "Data", "DD/MM/AAAA");
                return false;
            }


            // Validação na Regra da Data (Numero de Dias/Meses)
            String date_normalized = date_birth.replace("/", "");
            int day = Integer.parseInt(date_normalized.substring(0, 2));
            int month = Integer.parseInt(date_normalized.substring(2, 4));
            if (day <= 0 || day > 31 || month <= 0 || month > 12) {
                error_validation = String.format(INPUT_INVALID, "Data");
                return false;
            } else if (day == 31) {
                // Validação se o Mês permite dia 31
                List<Integer> month_more_day = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));
                if (!month_more_day.contains(month)) {
                    error_validation = String.format(INPUT_INVALID, "Data");
                    return false;
                }
            }

            if (month == 2) {
                int year = Integer.parseInt(date_normalized.substring(4, 8));
                if (day > 29) {
                    error_validation = String.format(INPUT_INVALID, "Data");
                    return false;
                } else if (day == 29 && year % 4 != 0) {
                    error_validation = String.format(INPUT_INVALID, "Data");
                    return false;
                }
            }

            // Alternativas para Diferentes Versões
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate localDate = LocalDate.parse(date_birth, DateTimeFormatter.ofPattern(PATTERN_DATE));
                LocalDate localDate_now = LocalDate.now(ZoneId.of(TIME_ZONE));

                if (localDate != null && localDate_now != null) {
                    Period period = Period.between(localDate, localDate_now);
                    if (period.getYears() >= 100) {
                        error_validation = String.format(INPUT_INVALID_AGE, "Maxima", "100");
                        return false;
                    }
                    if (period.getYears() < 13) {
                        // Usuario com Idade menor que 13 Anos
                        error_validation = String.format(INPUT_INVALID_AGE, "Minima", "13");
                        return false;
                    } else return true;
                }
            } else {
                // Formata a Data Recebida e a Data Atual do Brasil
                setDate_birth(date_birth);
                Date date_birth_formatted = getDate_dateBirth();
                Date date_now_formatted = getDate_now();

                if (date_birth_formatted != null && date_now_formatted != null) {
                    // Instancia e Coloca o Valor da Data
                    Calendar calendar_dateBirth = Calendar.getInstance(LOCALE_BR);
                    Calendar calendar_dateNow = Calendar.getInstance(LOCALE_BR);
                    calendar_dateBirth.setTime(date_birth_formatted);
                    calendar_dateNow.setTime(date_now_formatted);

                    // Subtração da Data de Nascimento com o Ano Atual
                    int years = calendar_dateNow.get(Calendar.YEAR) - calendar_dateBirth.get(Calendar.YEAR);

                    if (years > 13 && years < 100) return true;
                    else if (years >= 100) {
                        error_validation = String.format(INPUT_INVALID_AGE, "Maxima", "100");
                        return false;
                    } else if (years == 13) {
                        int day_of_year = calendar_dateNow.get(Calendar.DAY_OF_YEAR) - calendar_dateBirth
                                .get(Calendar.DAY_OF_YEAR);
                        if (day_of_year >= 0) return true;
                    }

                    // Usuario com Idade menor que 13 Anos
                    error_validation = String.format(INPUT_INVALID_AGE, "Minima", "13");
                    return false;
                }
            }

            error_validation = String.format(INPUT_INVALID, "Data");
            return false;
        } catch (Exception ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(EXCEPTION_GLOBAL, NAME_CLASS + " - Falha na Manipulação da Data");
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * String Array com os DDDs Brasileiros Validos
     *
     * @return String[]
     */
    public String[] ddd_valid() {
        return new String[]{
                "011", "012", "013", "014", "015", "016", "017", "018", "019", "021", "022",
                "024", "027", "028", "031", "032", "033", "034", "035", "037", "038", "041",
                "042", "043", "044", "045", "046", "047", "048", "049", "051", "053", "054",
                "055", "061", "062", "063", "064", "065", "066", "067", "068", "069", "071",
                "073", "074", "075", "077", "079", "081", "082", "083", "084", "085", "086",
                "087", "088", "089", "091", "092", "093", "094", "095", "096", "097", "098", "099"
        };
    }

    // Getters e Setters da Classe User
    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Retorna o CPF no formato XXX.XXX.XXX-XX
     *
     * @return {@link String}
     */
    public String getUnmaskCpf() {
        return MaskInputPersonalized.remove_mask(cpf, MaskInputPersonalized.DEFAULT_REGEX);
    }

    /**
     * Retorna o CPF sem Mascara (XXXXXXXXXXX)
     *
     * @return {@link String}
     */
    public String getMaskedCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    private String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna o Telefone + DDD no formato (0XX)XXXXX-XXXX
     *
     * @return {@link String}
     */
    public String getMaskedPhone() {
        return phone;
    }

    /**
     * Retorna o Telefone no formato XXXXXXXXX
     *
     * @return {@link String}
     */
    public String getUnmaskPhone() {
        return MaskInputPersonalized.remove_mask(phone, MaskInputPersonalized.DEFAULT_REGEX);
    }

    /**
     * Retorna apenas o DDD do Usuario
     *
     * @return {@link String}
     */
    public String getDDD() {
        return validationBrazilianPhone(phone) && getUnmaskCpf() != null
                ? getUnmaskPhone().substring(1, 3) : "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtem o CNPJ no seguinte formato: XX.XXX.XXX/XXXX-XX
     *
     * @return {@link String}
     */
    public String getMaskedCnpj() {
        return cnpj;
    }

    /**
     * Retorna o CNPJ sem Mascara (XXXXXXXXXXXXXX)
     *
     * @return {@link String}
     */
    public String getUnmaskCnpj() {
        return MaskInputPersonalized.remove_mask(cnpj, MaskInputPersonalized.DEFAULT_REGEX);
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getError_validation() {
        return error_validation;
    }

    /**
     * Retorna a Data de Nascimento em uma String no Formato dd/MM/aaaa
     *
     * @return {@link String}|""
     */
    public String getString_dateBirth() {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE, LOCALE_BR);
        if (date_birth == null) return "";
        else return dateFormat.format(date_birth);
    }

    /**
     * Retorna a Data de Nascimento no Formato {@link Date}
     *
     * @return {@link Date}
     */
    private Date getDate_dateBirth() {
        return date_birth;
    }

    /**
     * Define a Data de Nascimento do Usuario a partir de uma Data Passada no Formato DIA/MES/ANO.
     * <p>
     * A Data pode será Definida com a Formatação (Fuso Horario BR) ou Nula (Erro na Formatação da Data)
     *
     * @param date_birth Data que será convertida de String para Date
     */
    public void setDate_birth(String date_birth) {
        try {
            // Configura o Formato e Fuso Horario da Data
            SimpleDateFormat dateFormatDefault = new SimpleDateFormat(PATTERN_DATE, LOCALE_BR);
            dateFormatDefault.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

            // Formata a Data Recebida
            this.date_birth = dateFormatDefault.parse(date_birth);
        } catch (Exception ex) {
            this.date_birth = null;
            error_validation = MESSAGE_EXCEPTION;
            Log.e(EXCEPTION_GLOBAL, NAME_CLASS + " - Falha na Conversão da Data");
            ex.printStackTrace();
        }
    }

    /**
     * Obtem a Data Atual, configurada no Fuso Horario Brasileiro-SP.
     * <p>
     * * Esse metodo foi desenvolvido para aparelhos coma versão menor que o Android "O".
     *
     * @return Date/null
     */
    public Date getDate_now() {
        try {
            // Formata a Data Atual com a Localização e Fuso Horario Brasileiro
            SimpleDateFormat dateFormatDefault = new SimpleDateFormat(PATTERN_DATE, LOCALE_BR);
            dateFormatDefault.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
            return dateFormatDefault.parse(dateFormatDefault.format(new Date()));
        } catch (Exception ex) {
            error_validation = MESSAGE_EXCEPTION;
            Log.e(EXCEPTION_GLOBAL, NAME_CLASS + " - Falha na Conversão da Data");
            ex.printStackTrace();
            return null;
        }
    }

}
