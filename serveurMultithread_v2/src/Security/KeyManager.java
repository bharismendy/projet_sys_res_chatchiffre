package Security;

import java.math.BigInteger;
import java.util.Random;

/**
 * Provide an object with a public and a private key
 */
public class KeyManager {

    private BigInteger n;
    private BigInteger m;
    private BigInteger e;
    private BigInteger u;

    /**
     * Constructor
     */
    public KeyManager() {
        generatePublicKey();
        generatePrivateKey();
    }

    /**
     * Generate values to make a public key
     */
    private void generatePublicKey() {
        BigInteger p;
        BigInteger q;
        do {
            // On génère un entier premier BigInteger p
            p = BigInteger.probablePrime(999, new Random());
        } while(!p.isProbablePrime(1));
        do {
            // On génère un entier premier (celui après p) BigInteger q
            q = p.nextProbablePrime();
        } while(!q.isProbablePrime(1));
        // On set n et m en fonction de p et q
        n = p.multiply(q);
        m = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));

        // On génére un entier BigInteger e
        e = BigInteger.probablePrime(99, new Random());
        // Tant que e et m ne sont pas premier entre eux
        while(!MathsFunctions.arePrime(m,e)) {
            // On prend l'entier premier qui suit
            e = e.nextProbablePrime();
        }
    }

    /**
     * Generate values to make a private key
     */
    private void generatePrivateKey() {
        u = e.modInverse(m);
    }

    /**
     * Get the public key according to the values computed
     * @return String
     */
    public String getPublicKey() {
        return n+":"+e;
    }

    /**
     * Get the private key according to the values computed
     * @return String
     */
    public String getPrivateKey() {
        return n+":"+u;
    }

}
