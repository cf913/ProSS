package sample;

/**
 * Created by cf913
 */
import javafx.scene.control.Alert;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Asmuth-Bloom Secret Sharing Scheme
 *
 */
public class ABS {

    //////////////////////////////////////SPLIT///////////////////////////////
    /************************************************************************
     * Splits a secret message into n shares such as t shares are needed
     * to reconstruct the secret.
     * @param message
     *          secret message
     * @param n
     *          number of shares to compute
     * @param t
     *          threshold
     * @return a list [shares, t, key] where shares is the new set of shares,
     *          t the threshold,
     *          key is the first element (m[0]) of a list of random co-prime
     *          need to recover the secret
     *
     * Uncomment all System.out.println for detailed output
     ************************************************************************/
    public static String[] split(String message, int n, int t) {

        final long start = System.nanoTime();
        String[] results = new String[3];

        String msg = message;
        BigInteger secret    = new BigInteger(msg.getBytes());
        //System.out.println("secret: " + secret);

        BigInteger[] m = new BigInteger[n + 1];


        // Computes list of ms such as m[0]<...<m[n]
        // and m[n-t+2]*...*m[n] < m[1]*...*m[t]
        BigInteger left;
        BigInteger right;
        m[0] = SSS.newPrime(secret.bitLength()+1);
        do {
            for (int i = 1; i < m.length; i++) {
                do {
                    m[i] = SSS.newPrime(secret.bitLength() + 1);
                } while (inList(m[i],m, i));
            }
            left = m[0];
            right = BigInteger.ONE;

            for (int i = n-t+2; i <= n; i++) {
                left = left.multiply(m[i]);
            }

            for (int i = 1; i <= t; i++) {
                right = right.multiply(m[i]);
            }
        } while (left.compareTo(right) >= 0);

        /*
        System.out.print("m: [");
        for (int i = 0; i < m.length; i++)
        {
            System.out.print(m[i]);
            if (i == (m.length - 1)) {
                System.out.println("]");
            } else {
                System.out.print(", ");
            }
        }
        */
        BigInteger key = m[0];

        BigInteger[] temp = m;
        BigInteger alpha = getAlpha(m, secret,t);
        //System.out.println("alpha: " + alpha);
        BigInteger bloom = (secret.add(m[0].multiply(alpha)));
        //System.out.println("bloom: " + bloom);

        String newshares = computShares(temp, bloom, n);
        //System.out.println("newshares: " + newshares);
        results[0] = newshares;
        results[1] = String.valueOf(t);
        results[2] = String.valueOf(key);

        System.out.println("Runtime:" + (System.nanoTime() - start));
        return results;
    }

    /********************************************************
     * Checks if b is an element of list m up to index index
     * @param b
     *          element to check for duplicate
     * @param m
     *          list of co-prime numbers
     * @param index
     *          index of m up to which searching is permitted
     * @return false iff b is not in m
     ********************************************************/
    private static boolean inList(BigInteger b, BigInteger[] m, int index) {
        boolean res = false;
        for (int i = 0; i < index; i ++) {
            if (b.compareTo(m[i]) == 0) {
                res = true;
            }
        }
        return res;
    }


    /***************************************************************************
     * Computes shares (Si, mi) where 1 <= i <= threshold and Si = bloom mod mi
     * @param m
     *          list of co-prime numbers
     * @param bloom
     *          bloom = secret + alpha*m[0]
     * @param num
     *          number of shares
     * @return String representation of a list of shares: share1\nshare2\n...sharen\n
     ***************************************************************************/
    private static String computShares(BigInteger[] m, BigInteger bloom, int num) {

        String results = "";
        for (int i = 1; i <= num; i++) {

            BigInteger mi = m[i];
            BigInteger si = bloom.mod(m[i]);
            results = results.concat(new Pair<>(si,mi).toString() + "\n");

        }
        return results;
    }

    /*********************************************************************
     * Generates a number alpha which satisfies the following condition:
     *  -> secret + alpha*m[0] < m[1]...m[t]
     * @param m
     *          List of random co-prime numbers
     * @param secret
     *          BigInteger representation of the secret message
     * @param t
     *          threshold
     * @return alpha
     *
     * Uncomment all System.out.println for detailed output
     *********************************************************************/
    public static BigInteger getAlpha(BigInteger[] m, BigInteger secret, int t) {

        BigInteger alpha;
        BigInteger temp = BigInteger.ONE;

        for (int i = 1; i <= t; i++) {
            temp = temp.multiply(m[i]);
        }
        alpha = SSS.newPrime(temp.bitLength());
        int i = 0;

        //loop runs up to 3 times in average
        while ((secret.add(m[0].multiply(alpha))).compareTo(temp) >= 0) {
            alpha = SSS.newPrime(alpha.bitLength()-i);
            i++;
        }
        //System.out.println("bitlength: " + temp.bitLength());
        //System.out.println("getAplha looped " + i + " times.");
        //System.out.println("S+alpha*m[0]: " + (secret.add(m[0].multiply(alpha))));
        //System.out.println("temp: " + temp);
        //System.out.println("alph: " + alpha);
        return alpha;
    }

    /////////////////////////////////////COMBINE/////////////////////////////
    /****************************************************************
     * Combines a set of given share into the secret message
     * @param num
     *          number of shares to combine
     * @param shares
     *          shares to combine
     * @param key
     *          m[0]
     * @return String representation of the recovered secret message
     *
     * Uncomment all System.out.println for detailed output
     ****************************************************************/
    public static String reconstruct(int num, String shares, BigInteger key) {

        final long start = System.nanoTime();
        String result;
        String[] list = shares.split("\\n");
        ArrayList<Pair<BigInteger, BigInteger>> newShares;
        newShares = to_list_of_pairs(shares);

        if (num != newShares.size()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Conflicting number of shares");
            alert.showAndWait();
            return "";
        }

        BigInteger bigm = BigInteger.ONE;

        for (int i = 0; i < newShares.size(); i++) {
            bigm = bigm.multiply(new BigInteger(String.valueOf(newShares.get(i).getR())));
        }

        BigInteger res = BigInteger.ZERO;
        Pair p;
        
        for (int i = 0; i < list.length; i++) {

            p = newShares.get(i);
            BigInteger a = new BigInteger(String.valueOf(p.getR()));
            BigInteger b = bigm.divide(new BigInteger(String.valueOf(p.getR())));

            BigInteger[] eu = euclid(a,b);
            res = res.add((new BigInteger(String.valueOf(p.getL()))).multiply(eu[1].multiply(b)));
        }

        result = new String(res.mod(bigm).mod(key).toByteArray());
        System.out.println("Runtime:" + (System.nanoTime() - start));
        return result;
    }

    /*******************************************************
     * Converts a String of shares into a list of Pair<k,v>
     * @param shares
     *          String of shares:
     *          share1\nshare2\n...sharen\n
     * @return array of shares
     *          [(1,f(1)),(2,f(2)),...,(n,f(n))]
     *******************************************************/
    public static ArrayList<Pair<BigInteger, BigInteger>> to_list_of_pairs(String shares) {

        ArrayList<Pair<BigInteger, BigInteger>> newShares = new ArrayList<>();
        String[] list = shares.split("\\n");

        for (int i = 0; i < list.length; i++) {
            String nobracket = list[i].replaceAll("[\\[\\](){}]","");
            String[] s = nobracket.split(",");
            BigInteger k = new BigInteger(String.valueOf(s[0]));
            BigInteger v = new BigInteger(String.valueOf(s[1]));
            newShares.add(new Pair<>(k,v));
        }

        return newShares;
    }

    /*****************************************
     * Performs Extended Euclid Algorithm:
     * Finds u,v such as a*u + b*v = 1
     * @param a
     * @param b
     * @return list [u,v]
     *****************************************/
    public static BigInteger[] euclid(BigInteger a, BigInteger b) {
        BigInteger r = a;
        BigInteger rprime = b;
        BigInteger u = BigInteger.ONE;
        BigInteger v = BigInteger.ZERO;
        BigInteger uprime = BigInteger.ZERO;
        BigInteger vprime = BigInteger.ONE;
        BigInteger q;
        BigInteger rs, us, vs;

        while (rprime.compareTo(BigInteger.ZERO) != 0) {
            q = r.divide(rprime);

            rs = r;
            us = u;
            vs = v;

            r = rprime;
            u = uprime;
            v = vprime;

            rprime = rs.subtract(q.multiply(rprime));
            uprime = us.subtract(q.multiply(uprime));
            vprime = vs.subtract(q.multiply(vprime));

        }

        BigInteger[] list = new BigInteger[2];
        list[0] = u;
        list[1] = v;
        return list;

    }

}
