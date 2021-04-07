package Utilities;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * <h1>Die Java-Klasse zum Hashen der Passwörter.</h1>
 * <p>
 * Diese Klasse generiert die Hashwerte zu gegebenen Passwörterun.</p>
 */
@Stateless
@LocalBean
public class Hasher {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 512;
    private static final String SALT = "58g--fbgdf%§!bjds8b,AB.AF08?21nl"; //Zufällig

    /**
     * Diese Methode gibt den berechneten Hashwert aus einem gegeben Passwort.
     * Das wird dazu genutzt, um Passworteingaben des Frontends zu überprüfen.
     *
     * @param password Das Passwort
     * @return Der Hashwert
     */
    public String checkPassword(String password) {
        return Hex.encodeHexString(hashPassword(password.toCharArray()));
    }

    /**
     * Diese Methode hasht ein Passwort.
     *
     * @param password Das Passwort
     * @return Der Hashwert
     */
    private byte[] hashPassword(final char[] password) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, SALT.getBytes(), ITERATIONS, KEY_LENGTH);
            SecretKey key = secretKeyFactory.generateSecret(spec);
            return key.getEncoded();
        } catch (Exception e) {
            return null;
        }
    }
}
