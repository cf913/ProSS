package sample;

/**
 * Created by cf913
 */

import javafx.scene.control.Alert;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Shamir's Secret Sharing Scheme
 *
 */
public class SSS {

    ////////////////////////////////////SPLIT/////////////////////////////////////////
    /********************************************************************************
     * Splits the secret into n shares
     * @param message
     *          the secret message
     * @param n
     *          number of shares
     * @param t
     *          threshold: minimum number of shares needed to reconstruct the secret
     * @return a list [shares, key, t] where shares is the new set of shares,
     *          key is the prime generated to compute the modulo, and t the threshold
     ********************************************************************************/
    public static String[] split(String message, int n, int t, String key) {

        final long start = System.nanoTime();
        String msg = message;
        BigInteger secret    = new BigInteger(msg.getBytes());
        String[] result = new String[3];
        /**
         * Prime > Secret
         */

        System.out.println("The key: " + key);
        BigInteger prime;
        if (!key.isEmpty()) {
            prime = new BigInteger(key);
        } else {
            prime = newPrime(secret.bitLength());
            if ((secret.compareTo(prime) >= 0)) {
                prime = newPrime((secret.multiply(BigInteger.valueOf(2))).bitLength());
            }
        }

        int degree = t - 1;
        BigInteger[] coefs;
        coefs = newPolynomial(degree, secret);

        String newShares;
        newShares = split_shares(coefs, n, prime);


        result[0] = newShares;
        result[1] = prime.toString();
        result[2] = String.valueOf(t);
        /*
         * Uncomment this out for detailed output
        System.out.println("secret: " + secret);
        System.out.println("prime: " + prime);
        System.out.println("degree: " + degree);
        System.out.print("coefs: [");
        for (int i = 0; i < coefs.length; i++)
        {
            System.out.print(coefs[i]);
            if (i == (coefs.length - 1)) {
                System.out.println("]");
            } else {
                System.out.print(", ");
            }
        }
        System.out.print("newShares: \n" + newShares);
        */
        System.out.println("Runtime:" + (System.nanoTime() - start));
        return result;

    }

    /***********************************************************************
     * Computes shares by calculating the polynomial for given coefficients
     * by calculating
     *
     * f(n) = coef[0] + coef[1]*n + coef[2]*n^2 +...+coef[degree]*n^degree mod prime
     *
     * for all n
     * @param coef
     *          polynomial coefficients
     * @param n
     *          number of shares
     * @param prime
     *          key for modulo
     * @return output-friendly String of shares: share1\nshare2\n...sharen\n
     ***********************************************************************/
    private static String split_shares(BigInteger[] coef, int n, BigInteger prime) {

        String newShares = "";
        for (int j = 1; j <= n; j++) {

            BigInteger k = BigInteger.valueOf(j);
            BigInteger v = coef[0];

            for (int i = 1; i < coef.length; i ++) {
                v = (v.add(k.pow(i).mod(prime).multiply(coef[i])).mod(prime)).mod(prime);
            }

            newShares = newShares.concat(new Pair<>(k,v).toString() + "\n");

        }
        return newShares;
    }

    /****************************************************
     * Generates random coefficients < secret
     * @param degree
     *          degree of the generated polynomial (t-1)
     * @param secret
     *          BigInteger representation of the secret message,
     *          to be set as coef[0]
     * @return list of coefficients
     ****************************************************/
    public static BigInteger[] newPolynomial(int degree, BigInteger secret) {

        BigInteger[] coef = new BigInteger[degree+1];

        coef[0] = new BigInteger(String.valueOf(secret));

        for (int i = 1; i <= degree; i++) {
            coef[i] = getRandomBigInteger(secret.subtract(BigInteger.ONE));
        }
        return coef;
    }

    /***********************************************
     * Generates a random prime of a given bitsize
     * @param bit
     *          target bitsize
     * @return prime of size bit
     ***********************************************/
    public static BigInteger newPrime(int bit) {
        return BigInteger.probablePrime(bit, new Random());
    }


    /**************************************************
     * Generates a random BigInteger between 0 and max
     * @param max
     *          range maximum value
     * @return 0 < BigInteger result < max
     **************************************************/
    public static BigInteger getRandomBigInteger(BigInteger max) {

        Random rand = new Random();
        BigInteger upperLimit = max;
        BigInteger result;

        do {
            result = new BigInteger(upperLimit.bitLength(), rand);
        } while((result.compareTo(upperLimit) != 0) && result.equals(BigInteger.ZERO));

        return result;
    }

    /////////////////////////////////COMBINE///////////////////////////////////////
    /*********************************************************
     * Combines a set of given share into the secret message
     * @param numshares
     *          number of shares to combine
     * @param shares
     *          String of shares to combine
     * @param prime
     *          Key for modulo
     * @return String representation of the secret message
     *********************************************************/
    public static String reconstruct(int numshares, String shares, BigInteger prime) {

        final long start = System.nanoTime();
        String secret;

        ArrayList<Pair<Integer, BigInteger>> newShares;
        newShares = to_list_of_pairs(shares);

        if (numshares != newShares.size()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Conflicting number of shares");
            alert.showAndWait();
            return "";
        }

        BigInteger secretByte = reconstruct_shares(newShares,prime);
        secret = new String(secretByte.toByteArray());
        //System.out.println("secretByte: " + secretByte);
        //System.out.println("secret: " + secret);
        System.out.println("Runtime:" + (System.nanoTime() - start));
        return secret;
    }

    /*******************************************************
     * Converts a String of shares into a list of Pair<k,v>
     * @param shares
     *          String of shares:
     *          share1\nshare2\n...sharen\n
     * @return array of shares
     *          [(1,f(1)),(2,f(2)),...,(n,f(n))]
     *******************************************************/
    public static ArrayList<Pair<Integer, BigInteger>> to_list_of_pairs(String shares) {

        ArrayList<Pair<Integer, BigInteger>> newShares = new ArrayList<>();
        String[] list = shares.split("\\n");

        for (int i = 0; i < list.length; i++) {
            String nobracket = list[i].replaceAll("[\\[\\](){}]","");
            String[] s = nobracket.split(",");
            int k = Integer.parseInt(s[0]);
            BigInteger v = new BigInteger(String.valueOf(s[1]));
            newShares.add(new Pair<>(k,v));
        }

        return newShares;
    }

    /**************************************************************
     * Performs Lagrange Interpolation to recover coef[0] = secret
     * @param newShares
     *          shares to combine
     * @param prime
     *          prime to compute modulo
     * @return BigInteger representation of the secret message
     *
     * Uncomment all System.out.println for detailed output
     **************************************************************/
    public static BigInteger reconstruct_shares(List<Pair<Integer, BigInteger>> newShares, BigInteger prime) {

        BigInteger s = BigInteger.ZERO;

        for (int i = 0; i < newShares.size(); i++) {
            BigInteger num = BigInteger.ONE;
            BigInteger denum = BigInteger.ONE;
            for (int j = 0; j < newShares.size(); j++) {
                //System.out.println("(" + newShares.get(j).getL() + "," + newShares.get(j).getR() + ")");

                if (i != j) {
                    num = num.multiply(BigInteger.valueOf(-newShares.get(j).getL())).mod(prime);
                    denum = denum.multiply(BigInteger.valueOf(newShares.get(i).getL()-newShares.get(j).getL())).mod(prime);

                    //System.out.println("num[" + i + "][" + j + "]: " + num);
                    //System.out.println("denum[" + i + "][" + j + "]: " + denum);

                }
            }
            BigInteger value = newShares.get(i).getR();
            s = s.add(value.multiply(num).multiply(denum.modInverse(prime))).mod(prime);
            //System.out.println("value[" + i + "]: " + value);
            //System.out.println("s[" + i + "]: " + s);
        }

        return s;
    }

}

