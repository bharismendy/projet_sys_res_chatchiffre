package Security;

import java.math.BigInteger;

/**
 * Provide Maths functions
 */
public class MathsFunctions {

    /**
     * Tell if a and b are prime between them
     * @param a
     * @param b
     * @return
     */
    public static boolean arePrime(BigInteger a, BigInteger b) {
        BigInteger gcd = a.gcd(b);
        return gcd.equals(new BigInteger("1"));
    }

}
