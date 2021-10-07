package com.example.goal.models;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

// todo: arrumar context dos testes
public class UserTest {

    // Variaveis Usadas nos Testes
    private Context context;
    private String[] valid_password;
    private User user;
    private String max_length;
    private int randomNumber;

    @Before
    public void instanceItems() {
        user = new User(context);
        randomNumber = new Random().nextInt(4);

        max_length = "Lokams Huiolasn Luhjei Kand Jaqk Muing Hashdo" +
                " Masfbujs Kbvues Cuisjte Leugianunv";

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

        assertNotEquals(true, user.validationName(null));
        assertNotEquals(true, user.validationName(""));
        assertNotEquals(true, user.validationName("Mi"));
        assertNotEquals(true, user.validationName(max_length + " Knus"));
        assertNotEquals(true, user.validationName(String.valueOf(65232)));
        assertNotEquals(true, user.validationName(String.valueOf(0)));

        for (String item : valid_names) {
            assertTrue(user.validationName(item));
        }
        assertTrue(user.validationName(min_lengthName));
        assertTrue(user.validationName(max_length));

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


        assertNotEquals(true, user.validationEmail(null));
        assertNotEquals(true, user.validationEmail(""));
        assertNotEquals(true, user.validationEmail("Lums " + 256));
        assertNotEquals(true, user.validationEmail("Loem"));
        assertNotEquals(true, user.validationEmail(max_length + "Knus"));
        assertNotEquals(true, user.validationEmail(String.valueOf(65232)));
        assertNotEquals(true, user.validationEmail(String.valueOf(0)));
        assertNotEquals(true, user.validationEmail(valid_email[randomNumber] + " "));
        assertNotEquals(true, user.validationEmail(valid_email[randomNumber] + " Aplos"));

        for (String item : valid_email) {
            assertTrue(user.validationEmail(item));
        }

        assertTrue(user.validationEmail(min_lengthEmail));
        assertTrue(user.validationEmail(max_lengthEmail));
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

        assertNotEquals(true, user.validationNickname(null));
        assertNotEquals(true, user.validationNickname(""));
        assertNotEquals(true, user.validationNickname("Lufems "));
        assertNotEquals(true, user.validationNickname("Loem"));
        assertNotEquals(true, user.validationNickname(max_length + " Knus"));
        assertNotEquals(true, user.validationNickname(String.valueOf(0)));
        /* todo: alterar = Implementar regex de pelo menos 5 Letras no Nickname
        assertNotEquals(true, user.validationNickname(String.valueOf(65232)));
        assertNotEquals(true, user.validationNickname("Lums" + 256));
        */

        for (String item : valid_nickname) {
            assertTrue(user.validationNickname(item));
        }

        assertTrue(user.validationNickname(max_lengthNickname));
        assertTrue(user.validationNickname(min_lengthNickname));
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

        assertNotEquals(true, user.validationCpf(null));
        assertNotEquals(true, user.validationCpf(""));
        assertNotEquals(true, user.validationCpf("Lums"));
        assertNotEquals(true, user.validationCpf("Loem "));
        assertNotEquals(true, user.validationCpf(String.valueOf(65232)));
        assertNotEquals(true, user.validationCpf(String.valueOf(0)));
        assertNotEquals(true, user.validationCpf("Lums" + 256));

        for (String item : valid_cpf) {
            assertTrue(user.validationCpf(item));
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

        assertNotEquals(true, user.validationCnpj(null));
        assertNotEquals(true, user.validationCnpj(""));
        assertNotEquals(true, user.validationCnpj("Lums"));
        assertNotEquals(true, user.validationCnpj("Loem "));
        assertNotEquals(true, user.validationCnpj(String.valueOf(65232)));
        assertNotEquals(true, user.validationCnpj(String.valueOf(0)));
        assertNotEquals(true, user.validationCnpj("Lums" + 256));

        for (String item : valid_cnpj) {
            assertTrue(user.validationCnpj(item));
        }
    }

    @Test
    public void validationPassword() {

        String min_lengthPassword = "laos2";
        String max_lengthPassword = "aius8913rhi102r1120391rjasojfl1283thungd";

        assertNotEquals(true, user.validationPassword(null));
        assertNotEquals(true, user.validationPassword(""));
        assertNotEquals(true, user.validationPassword("Kjao"));
        assertNotEquals(true, user.validationPassword(String.valueOf(1234)));
        assertNotEquals(true, user.validationPassword("Kjao "));

        for (String item : valid_password) {
            assertTrue(user.validationPassword(item));
        }

        assertTrue(user.validationPassword(min_lengthPassword));
        assertTrue(user.validationPassword(max_lengthPassword));
    }

    @Test
    public void validationConfirmPassword() {

        User empty_password = new User(context);
        empty_password.setPassword("");
        empty_password.setConfirmPassword("");

        User error_length = new User(context);
        error_length.setPassword("Kjao");
        error_length.setConfirmPassword("Kjao");

        User error_lengthNumber = new User(context);
        error_lengthNumber.setPassword(String.valueOf(1234));
        error_lengthNumber.setConfirmPassword(String.valueOf(1234));

        User error_blankChar = new User(context);
        error_blankChar.setPassword("Kjao ");
        error_blankChar.setConfirmPassword("Kjao ");

        User min_length = new User(context);
        min_length.setPassword("laos2");
        min_length.setConfirmPassword("laos2");

        User max_length = new User(context);
        max_length.setPassword("aius8913rhi102r1120391rjasojfl1283thungd");
        max_length.setConfirmPassword("aius8913rhi102r1120391rjasojfl1283thungd");

        User no_match = new User(context);
        no_match.setPassword("Password123");
        no_match.setConfirmPassword("Password12");

        assertNotEquals(true, user.validationConfirmPassword(new User(context)));
        assertNotEquals(true, user.validationConfirmPassword(empty_password));
        assertNotEquals(true, user.validationConfirmPassword(error_length));
        assertNotEquals(true, user.validationConfirmPassword(error_lengthNumber));
        assertNotEquals(true, user.validationConfirmPassword(error_blankChar));
        assertNotEquals(true, user.validationConfirmPassword(no_match));

        for (String item : valid_password) {
            User userItem = new User(context);
            userItem.setPassword(item);
            userItem.setConfirmPassword(item);

            assertTrue(user.validationConfirmPassword(userItem));
        }

        assertTrue(user.validationConfirmPassword(min_length));
        assertTrue(user.validationConfirmPassword(max_length));
    }

    @Test
    public void validationPhone() {

        String[] valid_numbers = new String[5];
        valid_numbers[0] = "77841009622";
        valid_numbers[1] = "54827590235";
        valid_numbers[2] = "17176309513";
        valid_numbers[3] = "61605370906";
        valid_numbers[4] = "43477218084";

        assertNotEquals(true, user.validationPhone(null));
        assertNotEquals(true, user.validationPhone(""));
        assertNotEquals(true, user.validationPhone("Kjao"));
        assertNotEquals(true, user.validationPhone("Kjao12354"));
        assertNotEquals(true, user.validationPhone("Kjao "));
        assertNotEquals(true, user.validationPhone(String.valueOf(1234)));
        assertNotEquals(true, user.validationPhone("123 153"));

        for (String item : valid_numbers) {
            assertTrue(user.validationPhone(item));
        }
    }
}