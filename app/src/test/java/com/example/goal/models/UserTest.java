package com.example.goal.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {

    private User user;
    private String max_length;
    private int randomNumber;
    String[] valid_password;

    @Before
    public void instanceItems() {
        user = new User();

        max_length = "Lokams Huiolasn Luhjei Kand Jaqk Muing Hashdo" +
                " Masfbujs Kbvues Cuisjte Leugianunv";

        randomNumber = new Random().nextInt(4);

        valid_password = new String[5];
        valid_password[0] = "LopesPçka2";
        valid_password[1] = "Pas291Ola";
        valid_password[2] = "Pluongj7491";
        valid_password[3] = "*&194u19q812r";
        valid_password[4] = "123456789011";
    }

    @Test
    public void validationName() {

        String min_lengthName = "Lor";
        String[] valid_names = new String[5];
        valid_names[0] = "Lorem Ipsulin";
        valid_names[1] = "Kilá Koplé";
        valid_names[2] = "Hum Loçã Opla";
        valid_names[3] = "Lok Moais";
        valid_names[4] = "Niju";

        assertNotEquals(User.OK, user.validationName(null));
        assertNotEquals(User.OK, user.validationName(""));
        assertNotEquals(User.OK, user.validationName("Mi"));
        assertNotEquals(User.OK, user.validationName(max_length + " Knus"));
        assertNotEquals(User.OK, user.validationName(String.valueOf(65232)));
        assertNotEquals(User.OK, user.validationName(String.valueOf(0)));

        for (String item : valid_names) {
            assertEquals(User.OK, user.validationName(item));
        }
        assertEquals(User.OK, user.validationName(min_lengthName));
        assertEquals(User.OK, user.validationName(max_length));

    }

    @Test
    public void validationEmail() {
        String min_lengthEmail = "Lopsen@Loes.com";
        String max_lengthEmail = "Lokams.Huiolasn_LuhjeiKandJauing." +
                "Hashdo_Masfbujs_Kbvues@uisjte_Leugianunv.Ol.com";

        // Emails de Teste disponibilizados por : https://pt.slideshare.net/LivrariaFaceEbook/emails-validos
        String[] valid_email = new String[5];
        valid_email[0] = "beatrice@hotmail.com";
        valid_email[1] = "celine@hotmail.com";
        valid_email[2] = "steve@hotmail.com";
        valid_email[3] = "henry@hotmail.com";
        valid_email[4] = "theo@hotmail.com";


        assertNotEquals(User.OK, user.validationEmail(null));
        assertNotEquals(User.OK, user.validationEmail(""));
        assertNotEquals(User.OK, user.validationEmail("Lums " + 256));
        assertNotEquals(User.OK, user.validationEmail("Loem"));
        assertNotEquals(User.OK, user.validationEmail(max_length + "Knus"));
        assertNotEquals(User.OK, user.validationEmail(String.valueOf(65232)));
        assertNotEquals(User.OK, user.validationEmail(String.valueOf(0)));
        assertNotEquals(User.OK, user.validationEmail(valid_email[randomNumber]+" "));
        assertNotEquals(User.OK, user.validationEmail(valid_email[randomNumber]+" Aplos"));

        for (String item : valid_email) {
            assertEquals(User.OK, user.validationEmail(item));
        }

        assertEquals(User.OK, user.validationEmail(min_lengthEmail));
        assertEquals(User.OK, user.validationEmail(max_lengthEmail));
    }

    @Test
    public void validationNickname() {

        String min_lengthNickname = "Ilaus";
        String max_lengthNickname = "Lokams.Huiolasn_LuhjeiKandJaqkmuing" +
                ".Hashdo_Masfbujs_Kbvues.Cuisjte_Leugianunv.Ol";
        String[] valid_nickname = new String[5];
        valid_nickname[0] = "Lor3m_Ipsul1n";
        valid_nickname[1] = "Hum.lof_Opla";
        valid_nickname[2] = "LokMo4is";
        valid_nickname[3] = "Niju.";
        valid_nickname[4] = "Lore4nv2_Lpoa.";

        assertNotEquals(User.OK, user.validationNickname(null));
        assertNotEquals(User.OK, user.validationNickname(""));
        assertNotEquals(User.OK, user.validationNickname("Lufems "));
        assertNotEquals(User.OK, user.validationNickname("Loem"));
        assertNotEquals(User.OK, user.validationNickname(max_length + " Knus"));
        assertNotEquals(User.OK, user.validationNickname(String.valueOf(0)));
        /* todo: alterar = Implementar regex de pelo menos 5 Letras no Nickname
        assertNotEquals(User.OK, user.validationNickname(String.valueOf(65232)));
        assertNotEquals(User.OK, user.validationNickname("Lums" + 256));
        */

        for (String item : valid_nickname) {
            assertEquals(User.OK, user.validationNickname(item));
        }

        assertEquals(User.OK, user.validationNickname(max_lengthNickname));
        assertEquals(User.OK, user.validationNickname(min_lengthNickname));
    }

    @Test
    public void validationCpf() {

        // Cpf disponibilizados por: https://www.4devs.com.br/gerador_de_cpf
        String[] valid_cpf = new String[5];
        valid_cpf[0] = "69022895068";
        valid_cpf[1] = "40560427050";
        valid_cpf[2] = "98339670000";
        valid_cpf[3] = "61656389029";
        valid_cpf[4] = "57411423033";

        assertNotEquals(User.OK, user.validationCpf(null));
        assertNotEquals(User.OK, user.validationCpf(""));
        assertNotEquals(User.OK, user.validationCpf("Lums"));
        assertNotEquals(User.OK, user.validationCpf("Loem "));
        assertNotEquals(User.OK, user.validationCpf(String.valueOf(65232)));
        assertNotEquals(User.OK, user.validationCpf(String.valueOf(0)));
        assertNotEquals(User.OK, user.validationCpf("Lums" + 256));

        for (String item : valid_cpf) {
            assertEquals(User.OK, user.validationCpf(item));
        }
    }

    @Test
    public void validationCnpj() {
        // Cpf disponibilizados por: https://www.4devs.com.br/gerador_de_cnpj
        String[] valid_cnpj = new String[5];
        valid_cnpj[0] = "63590501000195";
        valid_cnpj[1] = "70932647000137";
        valid_cnpj[2] = "35108454000165";
        valid_cnpj[3] = "10267871000120";
        valid_cnpj[4] = "01021298000189";

        assertNotEquals(User.OK, user.validationCnpj(null));
        assertNotEquals(User.OK, user.validationCnpj(""));
        assertNotEquals(User.OK, user.validationCnpj("Lums"));
        assertNotEquals(User.OK, user.validationCnpj("Loem "));
        assertNotEquals(User.OK, user.validationCnpj(String.valueOf(65232)));
        assertNotEquals(User.OK, user.validationCnpj(String.valueOf(0)));
        assertNotEquals(User.OK, user.validationCnpj("Lums" + 256));

        for (String item : valid_cnpj) {
            assertEquals(User.OK, user.validationCnpj(item));
        }
    }

    @Test
    public void validationPassword() {

        String min_lengthPassword = "laos2";
        String max_lengthPassword = "aius8913rhi102r1120391rjasojfl1283thungd";

        assertNotEquals(User.OK, user.validationPassword(null));
        assertNotEquals(User.OK, user.validationPassword(""));
        assertNotEquals(User.OK, user.validationPassword("Kjao"));
        assertNotEquals(User.OK, user.validationPassword(String.valueOf(1234)));
        assertNotEquals(User.OK, user.validationPassword("Kjao "));

        for (String item : valid_password) {
            assertEquals(User.OK, user.validationPassword(item));
        }

        assertEquals(User.OK, user.validationPassword(min_lengthPassword));
        assertEquals(User.OK, user.validationPassword(max_lengthPassword));
    }

    @Test
    public void validationConfirmPassword() {

        User empty_password = new User();
        empty_password.setPassword("");
        empty_password.setConfirmPassword("");

        User error_length = new User();
        error_length.setPassword("Kjao");
        error_length.setConfirmPassword("Kjao");

        User error_lengthNumber = new User();
        error_lengthNumber.setPassword(String.valueOf(1234));
        error_lengthNumber.setConfirmPassword(String.valueOf(1234));

        User error_blankChar = new User();
        error_blankChar.setPassword("Kjao ");
        error_blankChar.setConfirmPassword("Kjao ");

        User min_length = new User();
        min_length.setPassword("laos2");
        min_length.setConfirmPassword("laos2");

        User max_length = new User();
        max_length.setPassword("aius8913rhi102r1120391rjasojfl1283thungd");
        max_length.setConfirmPassword("aius8913rhi102r1120391rjasojfl1283thungd");

        User no_match = new User();
        no_match.setPassword("Password123");
        no_match.setConfirmPassword("Password12");

        assertNotEquals(User.OK, user.validationConfirmPassword(new User()));
        assertNotEquals(User.OK, user.validationConfirmPassword(empty_password));
        assertNotEquals(User.OK, user.validationConfirmPassword(error_length));
        assertNotEquals(User.OK, user.validationConfirmPassword(error_lengthNumber));
        assertNotEquals(User.OK, user.validationConfirmPassword(error_blankChar));
        assertNotEquals(User.OK, user.validationConfirmPassword(no_match));

        for (String item : valid_password) {
            User userItem = new User();
            userItem.setPassword(item);
            userItem.setConfirmPassword(item);

            assertEquals(User.OK, user.validationConfirmPassword(userItem));
        }

        assertEquals(User.OK, user.validationConfirmPassword(min_length));
        assertEquals(User.OK, user.validationConfirmPassword(max_length));
    }

    @Test
    public void validationPhone() {

        String[] valid_numbers = new String[5];
        valid_numbers[0] = "77841009622";
        valid_numbers[1] = "54827590235";
        valid_numbers[2] = "17176309513";
        valid_numbers[3] = "61605370906";
        valid_numbers[4] = "43477218084";

        assertNotEquals(User.OK, user.validationPhone(null));
        assertNotEquals(User.OK, user.validationPhone(""));
        assertNotEquals(User.OK, user.validationPhone("Kjao"));
        assertNotEquals(User.OK, user.validationPhone("Kjao12354"));
        assertNotEquals(User.OK, user.validationPhone("Kjao "));
        assertNotEquals(User.OK, user.validationPhone(String.valueOf(1234)));
        assertNotEquals(User.OK, user.validationPhone("123 153"));

        for (String item : valid_numbers) {
            assertEquals(User.OK, user.validationPhone(item));
        }
    }
}