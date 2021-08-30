package com.example.goal.models;

public class User {

    public static final String OK = "Campo Validado";
    private static final String INPUT_NULL = "Campo Obrigatorio";
    private static final String INPUT_MIN_LENGHT = "%1$s deve ter no Minimo %2$s Caracteres";
    private static final String INPUT_MAX_LENGHT = "%1$s deve ter no Maximo %2$s Caracteres";
    private static final String INPUT_NOT_FORMAT = "%1$s deve ter conter apenas %2$s";
    private static final int LOW_STRENGHT = 1;
    private static final int OK_STRENGHT = 2;

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

    public User() {
    }

    public String validationName(String name) {
        if (name == null || name.equals("")) {
            return INPUT_NULL;
        } else if (name.length() < 3) {
            return String.format(INPUT_MIN_LENGHT, "Nome", 3);
        } else if (name.length() > 80) {
            return String.format(INPUT_MAX_LENGHT, "Nome", 80);
        } else if (!name.matches("^[A-ZÀ-úà-úa-zçÇ\\s]*")) {
            return String.format(INPUT_NOT_FORMAT, "Nome", "Letras");
        } else return OK;
    }

    public String validationEmail(String email) {
        if (email == null || email.equals("")) {
            return INPUT_NULL;
        } else if (email.length() < 15) {
            return String.format(INPUT_MIN_LENGHT, "Email", 15);
        } else if (email.length() > 80) {
            return String.format(INPUT_MAX_LENGHT, "Email", 80);
        } else if (!email.matches("^[A-Za-z0-9.@_]*")) {
            return String.format(INPUT_NOT_FORMAT, "Email",
                    "Letras, Numeros, Underline, Pontos ou Arroba");
        } else if (!validationEmailAPI(email)) {
            return "Endereço de E-mail não Valido";
        } else return OK;
    }

    private boolean validationEmailAPI(String email) {
        // todo: implementar API de validação email
        return true;
    }

    public String validationNickname(String nickname) {
        if (nickname == null || nickname.equals("")) {
            return INPUT_NULL;
        } else if (nickname.length() < 5) {
            return String.format(INPUT_MIN_LENGHT, "Nome de Usuario", 5);
        } else if (nickname.length() > 80) {
            return String.format(INPUT_MAX_LENGHT, "Nome de Usuario", 80);
        } else if (!nickname.matches("^[A-Za-z0-9._]*")) {
            return String.format(INPUT_NOT_FORMAT, "Nome de Usuario", "Letras, Numeros, Underline ou Pontos");
        } else return OK;
    }

    public String validationCpf(String cpf) {
        if (cpf == null || cpf.equals("")) {
            return INPUT_NULL;
        } else if (cpf.length() != 11) {
            return String.format(INPUT_NOT_FORMAT,
                    "CPF", "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
        } else if (!cpf.matches("^[0-9]*")) {
            return String.format(INPUT_NOT_FORMAT, "CPF",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
        } else if (!validationNumberCpf(cpf)) {
            return "CPF Invalido";
        } else return OK;
    }

    private boolean validationNumberCpf(String cpf) {
        // todo: Codigo de Validação do CPF
        return true;
    }

    public String validationCnpj(String cnpj) {
        if (cnpj == null || cnpj.equals("")) {
            return INPUT_NULL;
        } else if (cnpj.length() != 14) {
            return String.format(INPUT_NOT_FORMAT, "CPF",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
        } else if (!validationNumberCnpj(cnpj)) {
            return "CNPJ Invalido";
        } else return OK;
    }

    private boolean validationNumberCnpj(String cnpj) {
        // todo: Codigo de Validação do CNPJ
        return true;
    }

    public String validationPassword(String password) {
        if (password == null || password.equals("")) {
            return INPUT_NULL;
        } else if (password.length() < 5) {
            return String.format(INPUT_MIN_LENGHT, "Senha", 5);
        } else if (password.length() > 40) {
            return String.format(INPUT_MIN_LENGHT, "Senha", 40);
        } else if (!password.matches("^[\\S]*")) {
            return String.format(INPUT_NOT_FORMAT,
                    "Senha", "Caracteres (Sem espaços em Branco)");
        } else if (strengthPassword(password) == LOW_STRENGHT) {
            //TODO : mensagem de erro da senha
            return "A senha deve conter....";
        } else return OK;
    }

    public String validationConfirmPassword(User user) {

        String confirmPassword = user.getConfirmPassword();
        String password = user.getPassword();

        if (confirmPassword == null || confirmPassword.equals("")) {
            return INPUT_NULL;
        }

        String validationPassword = validationPassword(confirmPassword);
        if (!validationPassword.equals(OK)) {
            return validationPassword;
        } else if (!password.equals(confirmPassword)) {
            return "As Senhas não são Iguais";
        } else return OK;
    }


    private int strengthPassword(String password) {
        // todo: implementar requisitos e mensagens de erro para força da senha
        return OK_STRENGHT;
    }

    public String validationPhone(String phone) {
        if (phone == null || phone.equals("")) {
            return INPUT_NULL;
        } else if (phone.length() != 11) {
            return String.format(INPUT_NOT_FORMAT, "Telefone", "11 Digitos (DDD + Numero)");
        } else if (!phone.matches("^[0-9]*")) {
            return String.format(INPUT_NOT_FORMAT, "Telefone",
                    "Numeros (Sem Hifen/Ponto/Virgula/Espaços em Branco)");
        } else return OK;
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
}
