package com.example.goal.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Classe de Testes da Classe do Usuario.
 * <p>
 * Está no AndroidTest por conta da utilização do Context na Instancia da Classe User
 */
@RunWith(AndroidJUnit4.class)
public class UserTest {

    // Variaveis Usadas nos Testes
    private Context context;
    private String[] valid_password;
    private User user;
    private String max_length;
    private int randomNumber;

    /**
     * Instancia as variaveis que serão utilizados nos Testes
     */
    @Before
    public void instanceItems() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
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
        String max_lengthEmail = "LKoupla_plouDs-ldjASDnfpsnen.okamDsHuiolasn_LuhjeiKandJau.comHashdo_Masfbujs_Kbvues@u-jdkJDNSNIKSisjte-LeugianunvLOl.com";

        // Emails de Teste disponibilizados por : https://pt.slideshare.net/LivrariaFaceEbook/emails-validos
        String[] valid_email = new String[5];
        valid_email[0] = "beatrice@hotmail.com";
        valid_email[1] = "celine@hotmail.com";
        valid_email[2] = "steve@hotmail.com";
        valid_email[3] = "henry@hotmail.com";
        valid_email[4] = "theo@hotmail.com";

        assertFalse(user.validationEmail(null));
        assertFalse(user.validationEmail(""));
        assertFalse(user.validationEmail("Lums " + 256));
        assertFalse(user.validationEmail("Loem"));
        assertFalse(user.validationEmail(max_length + "Knus"));
        assertFalse(user.validationEmail(String.valueOf(65232)));
        assertFalse(user.validationEmail(String.valueOf(0)));
        assertFalse(user.validationEmail(valid_email[randomNumber] + " "));
        assertFalse(user.validationEmail(valid_email[randomNumber] + " Aplos"));

        for (String item : valid_email) {
            assertTrue(user.validationEmail(item));
        }

        assertTrue(user.validationEmail(min_lengthEmail));
        assertTrue(user.validationEmail(max_lengthEmail));
    }

    @Test
    public void validationNickname() {
        String min_lengthNickname = "Ilaus";
        String max_lengthNickname = "sn_LuhjeiKandJaqkmuing..Cui.Lopluns.Lope_sjte_Le.OlPlutyplun";
        String[] valid_nickname = new String[5];
        valid_nickname[0] = "Lor3m_Ipsul1n";
        valid_nickname[1] = "Hum.lof_Opla";
        valid_nickname[2] = "LokMo4is";
        valid_nickname[3] = "Niju.";
        valid_nickname[4] = "Lore4nv2_Lpoa.";

        assertFalse(user.validationNickname(null));
        assertFalse(user.validationNickname(""));
        assertFalse(user.validationNickname("Lufems "));
        assertFalse(user.validationNickname("Loem"));
        assertFalse(user.validationNickname(max_length + " Knus"));
        assertFalse(user.validationNickname(String.valueOf(0)));
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
        String[] valid_cpf = new String[]{"690.228.950-68", "405.604.270-50", "983.396.700-00",
                "616.563.890-29", "574.114.230-33"};

        assertFalse(user.validationCpf(null));
        assertFalse(user.validationCpf(""));
        assertFalse(user.validationCpf("Lums"));
        assertFalse(user.validationCpf("Loem "));
        assertFalse(user.validationCpf(String.valueOf(65232)));
        assertFalse(user.validationCpf(String.valueOf(0)));
        assertFalse(user.validationCpf("Lums" + 256));

        assertTrue(user.validationCpf(valid_cpf[0]));
        assertTrue(user.validationCpf(valid_cpf[1]));
        assertTrue(user.validationCpf(valid_cpf[2]));
        assertTrue(user.validationCpf(valid_cpf[3]));
        assertTrue(user.validationCpf(valid_cpf[4]));
    }

    @Test
    public void validationCnpj() {
        // Cpf disponibilizados por: https://www.4devs.com.br/gerador_de_cnpj
        String[] valid_cnpj = new String[]{"27.601.226/0001-02", "35.108.454/0001-65"};

        assertFalse(user.validationCnpj(null));
        assertFalse(user.validationCnpj(""));
        assertFalse(user.validationCnpj("Lums"));
        assertFalse(user.validationCnpj("Loem "));
        assertFalse(user.validationCnpj(String.valueOf(65232)));
        assertFalse(user.validationCnpj(String.valueOf(0)));
        assertFalse(user.validationCnpj("Lums" + 256));

        assertTrue(user.validationCnpj(valid_cnpj[0]));
        assertTrue(user.validationNumberCnpj(Executors.newSingleThreadExecutor(), valid_cnpj[0]));
        assertTrue(user.validationCnpj(valid_cnpj[1]));
        assertTrue(user.validationNumberCnpj(Executors.newSingleThreadExecutor(), valid_cnpj[1]));
    }

    @Test
    public void validationPassword() {

        String min_lengthPassword = "laos2";
        String max_lengthPassword = "aius8913rhi102r1120391rjasojfl1283thungd";

        assertFalse(user.validationPassword(null));
        assertFalse(user.validationPassword(""));
        assertFalse(user.validationPassword("Kjao"));
        assertFalse(user.validationPassword(String.valueOf(1234)));
        assertFalse(user.validationPassword("Kjao "));

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

        assertFalse(user.validationConfirmPassword(new User(context)));
        assertFalse(user.validationConfirmPassword(empty_password));
        assertFalse(user.validationConfirmPassword(error_length));
        assertFalse(user.validationConfirmPassword(error_lengthNumber));
        assertFalse(user.validationConfirmPassword(error_blankChar));
        assertFalse(user.validationConfirmPassword(no_match));

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
        String[] valid_numbers = new String[]{"(077)84100-9622", "(054)82759-0235", "(017)17630-9513",
                "(061)60537-0906", "(043)47721-8084"};

        assertFalse(user.validationBrazilianPhone(null));
        assertFalse(user.validationBrazilianPhone(""));
        assertFalse(user.validationBrazilianPhone("Kjao"));
        assertFalse(user.validationBrazilianPhone("Kjao12354"));
        assertFalse(user.validationBrazilianPhone("Kjao "));
        assertFalse(user.validationBrazilianPhone(String.valueOf(1234)));
        assertFalse(user.validationBrazilianPhone("123 153"));

        assertTrue(user.validationBrazilianPhone(valid_numbers[0]));
        assertTrue(user.validationBrazilianPhone(valid_numbers[1]));
        assertTrue(user.validationBrazilianPhone(valid_numbers[2]));
        assertTrue(user.validationBrazilianPhone(valid_numbers[3]));
        assertTrue(user.validationBrazilianPhone(valid_numbers[4]));
    }

    @Test
    public void validationDateBirth() {
        String[] date_valid = new String[]{"24/10/2008", "01/09/2008", "01/04/2004", "25/02/2003",
                "06/12/2006", "06/07/2004", "29/02/2008", "29/02/2004"};

        for (String item : date_valid) {
            assertTrue(user.validationDateBirth(item));
        }

        String[] date_invalid = new String[]{"20/05/2018", "16/05/2021", "08/02/2019", "", " ",
                "///", "00/00/0000", "00/00/", "00/", "32/11/2021", "15/13/2021", "25/10/1821",
                "25/10/1919", "25/10/1921", "as", "aa/aa/aaaa", "32/11/2000", "18/13/2000",
                "30/02/2000", "29/02/2001", "30/02/2002", "84/11/2000", "02/15/2000"
        };

        for (String item : date_invalid) {
            assertFalse(user.validationDateBirth(item));
            assertNotNull(user.getError_validation());
            assertNotEquals("", user.getError_validation());
        }
    }

}