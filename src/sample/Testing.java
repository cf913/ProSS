package sample;

import java.math.BigInteger;

import static sample.SSSSplit.getRandomBigInteger;

/**
 * Created by cf913 on 13/05/16.
 */
public class Testing {

    public static void main(String[] args) {
        int degree = 11;
        BigInteger[] coefs;
        coefs = newPolynomial(degree, new BigInteger("123456789"));
        for (int i = 0; i < coefs.length; i ++) {
            System.out.println(i + ": " + coefs[i]);
        }
    }

    private static BigInteger[] newPolynomial(int degree, BigInteger secret) {

        BigInteger[] coef = new BigInteger[degree+1];  //coef[0] + plus coefs of degree d

        for (int i = 1; i <= degree; i++) {
            coef[i] = getRandomBigInteger(secret.subtract(BigInteger.ONE));
        }

        coef[0] = new BigInteger(String.valueOf(secret));

        return coef;
    }
}
