package sample;

/**
 * Created by cf913 on 11/05/16.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;

public class MainSplit {

    // PRIME NUMBER HAS TO BE BIGGER THAN SECRET!!!
    private static final BigInteger prime = new BigInteger(String.valueOf("73092066093226693205369231494727664060618592077774520502905049396258760143111"));

    public static String[] split(String message, int shares, int threshold) {

        String msg = message;
        BigInteger secret    = new BigInteger(msg.getBytes());
        System.out.println("The secret is: " + secret);

        BigInteger prime = newPrime(secret.bitLength());


        // need polynomial of degree threshold - 1 to recover secret
        int degree = threshold - 1;

        BigInteger[] coefs;
        coefs = newPolynomial(degree, secret);

        // split secret into n shares
        String newShares;
        newShares = split_shares(coefs, shares, prime);


        //Print shares in console
        System.out.println(shares + " shares: ");
        /*System.out.println("[");
        for (Pair pair : newShares) {
            System.out.println("(" + pair.getL() + "," + pair.getR() + ")");
        }
        System.out.println("]");
        */
        System.out.println("Shares:\n" + newShares);
        System.out.println("key:" + prime);
        String[] result = new String[3];
        result[0] = newShares;
        result[1] = prime.toString();
        result[2] = String.valueOf(threshold);
        return result;

    }



    // split secret into shares given polynomials and shares
    //return a list of shares
    private static /*List<Pair<Integer,BigInteger>>*/ String split_shares(BigInteger[] coef, int shares, BigInteger prime) {

        //ArrayList<Pair<Integer, BigInteger>> newShares = new ArrayList<>();
        String newShares = "";
        for (int j = 1; j <= shares; j++) {

            int k = j;
            BigInteger v = coef[0];

            for (int i = 1; i < coef.length; i ++) {
                v = (v.add(new BigInteger(String.valueOf((int)pow(k, i))).mod(prime).multiply(coef[i])).mod(prime)).mod(prime);
            }

            //newShares.add(new Pair<>(k,v));
            newShares = newShares.concat("(" + k + "," + v + ")\n");

        }
        return newShares;
    }





    // gets list of secret + random coefficients smaller than secret
    //returns list of coefs including coef[0] the secret
    private static BigInteger[] newPolynomial(int degree, BigInteger secret) {

        BigInteger[] coef = new BigInteger[degree+1];  //coef[0] + plus coefs of degree d

        for (int i = 0; i < degree; i++) {
            coef[i+1] = getRandomBigInteger(secret.subtract(BigInteger.ONE));
        }

        coef[0] = new BigInteger(String.valueOf(secret));

        return coef;
    }



    //generates random prime of a given bitsize
    public static BigInteger newPrime(int bit) {

        BigInteger i = BigInteger.probablePrime(bit, new Random());

        return i;
    }



    // generates a random BigInteger between 0 and max
    public static BigInteger getRandomBigInteger(BigInteger max) {

        Random rand = new Random();
        BigInteger upperLimit = max;
        BigInteger result;

        do {
            result = new BigInteger(upperLimit.bitLength(), rand);
        }while((result.compareTo(upperLimit) != 0) && result.equals(BigInteger.ZERO));

        return result;
    }

}

