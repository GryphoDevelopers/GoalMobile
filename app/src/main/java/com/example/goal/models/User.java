package com.example.goal.models;

import android.content.Context;

import com.example.goal.R;

public class User {

    // Constantes da Força da Senha
    private static final int LOW_STRENGTH = 1;
    private static final int OK_STRENGTH = 2;

    // Constantes Usadas nos Erros
    private final String INPUT_NULL;
    private final String INPUT_MIN_LENGTH;
    private final String INPUT_MAX_LENGTH;
    private final String INPUT_NOT_FORMAT;
    private final String INPUT_INVALID;

    private final Context context;
    private String name;
    private String email;
    private String nickname;
    private String cpf;
    private String password;
    private String confirmPassword;
    private String phone;
    private String cnpj;
    private boolean isSeller;
    private boolean checkedTermsUse;

    private String error_validation;

    public User(Context context) {
        this.context = context;

        // Obtem as Strings de Erro
        INPUT_NULL = context.getString(R.string.validation_empty);
        INPUT_MIN_LENGTH = context.getString(R.string.validation_min_length);
        INPUT_MAX_LENGTH = context.getString(R.string.validation_max_length);
        INPUT_NOT_FORMAT = context.getString(R.string.validation_format);
        INPUT_INVALID = context.getString(R.string.validation_not_disponible);
    }

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
            error_validation = String.format(INPUT_NOT_FORMAT, "Nome", "Letras");
            return false;
        } else return true;
    }

    public boolean validationEmail(String email) {
        if (email == null || email.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (email.length() < 15) {
            error_validation = String.format(INPUT_MIN_LENGTH, "Email", 15);
            return false;
        } else if (email.length() > 120) {
            error_validation = String.format(INPUT_MAX_LENGTH, "Email", 120);
            return false;
        } else if (!email.matches("^[A-Za-z0-9.@_]*")) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Email",
                    "Letras, Numeros, Underline, Pontos ou Arroba");
            return false;
        } else if (!validationEmailAPI(email)) {
            error_validation = String.format(INPUT_INVALID, "Email");
            return false;
        } else return true;
    }

    private boolean validationEmailAPI(String email) {
        // todo: implementar API de validação email
        return true;
    }

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
            error_validation = String.format(INPUT_NOT_FORMAT, "Nome de Usuario",
                    "Letras, Numeros, Underline ou Pontos");
            return false;
        } else return true;
    }

    public boolean validationCpf(String cpf) {
        if (cpf == null || cpf.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (cpf.length() != 11) {
            error_validation = String.format(INPUT_NOT_FORMAT, "CPF",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
            return false;
        } else if (!cpf.matches("^[0-9]*")) {
            error_validation = String.format(INPUT_NOT_FORMAT, "CPF",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
            return false;
        } else if (!validationNumberCpf(cpf)) {
            error_validation = String.format(INPUT_INVALID, "CPF");
            return false;
        } else return true;
    }

    private boolean validationNumberCpf(String cpf) {
        // todo: Codigo de Validação do CPF
        return true;
    }

    public boolean validationCnpj(String cnpj) {
        if (cnpj == null || cnpj.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (cnpj.length() != 14) {
            error_validation = String.format(INPUT_NOT_FORMAT, "CNPJ",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
            return false;
        } else if (!validationNumberCnpj(cnpj)) {
            error_validation = String.format(INPUT_INVALID, "CNPJ");
            return false;
        } else return true;
    }

    private boolean validationNumberCnpj(String cnpj) {
        // todo: Codigo de Validação do CNPJ
        return true;
    }

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
            error_validation = String.format(INPUT_NOT_FORMAT, "Senha",
                    "Caracteres (Sem espaços em Branco)");
            return false;
        } else if (strengthPassword(password) == LOW_STRENGTH) {
            //TODO : mensagem de erro da senha
            error_validation = context.getString(R.string.validation_password,
                    "todo: alterar aqui");
            return false;
        } else return true;
    }

    private int strengthPassword(String password) {
        // todo: implementar requisitos  para força da senha
        return OK_STRENGTH;
    }

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

    public boolean validationPhone(String phone) {
        if (phone == null || phone.equals("")) {
            error_validation = INPUT_NULL;
            return false;
        } else if (phone.length() != 11) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Telefone", "11 Digitos (DDD + Numero)");
            return false;
        } else if (!phone.matches("^[0-9]*")) {
            error_validation = String.format(INPUT_NOT_FORMAT, "Telefone",
                    "Numeros (Sem Hifen, Ponto, Virgula ou Espaços em Branco)");
            return false;
        } else return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCpf() {
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

    public String getConfirmPassword() {
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

    public boolean isCheckedTermsUse() {
        return checkedTermsUse;
    }

    public void setCheckedTermsUse(boolean checkedTermsUse) {
        this.checkedTermsUse = checkedTermsUse;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getError_validation() {
        return error_validation;
    }
}
