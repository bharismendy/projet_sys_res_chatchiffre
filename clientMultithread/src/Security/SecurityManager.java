package Security;

import java.math.BigInteger;

/**
 * Provide functions to encryption & decryption
 */
public class SecurityManager {

    public static String delimiter = "#";

    /**
     * Encrypt a String in an ASCII String
     * @param message
     * @return String
     */
    public static String encrypt(String message, String publicKey) {
        // We get the values of the key
        String[] keyValues = publicKey.split(":");
        BigInteger n = new BigInteger(keyValues[0]);
        BigInteger e = new BigInteger(keyValues[1]);
        String s = "";
        char[] letters = message.toCharArray();
        for (char letter: letters) {
            s += (new BigInteger(String.valueOf((int)letter)).modPow(e,n)) + delimiter;
        }
        return s;
    }

    /**
     * Decrypt an ASCII String in a String
     * @param message
     * @return String
     */
    public static String decrypt(String message, String privateKey) {
        // We get the values of the key
        String[] keyValues = privateKey.split(":");
        BigInteger n = new BigInteger(keyValues[0]);
        BigInteger u = new BigInteger(keyValues[1]);
        String[] letters = message.trim().split(delimiter);
        String s = "";
        for (String letter: letters) {
            s += (char)(new BigInteger(letter.trim()).modPow(u,n).intValue());
        }
        return s;
    }

}
