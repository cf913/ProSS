package sample;


import java.math.BigInteger;
import java.util.Random;

/**
 * Created by cf913 on 25/05/16.
 */
public class PicSplit {


    // random prime number length
    private static int keylength = 128;


















    public static BigInteger newPrime(int bit) {
        BigInteger i = BigInteger.probablePrime(keylength, new Random());
        return i;
    }
}
