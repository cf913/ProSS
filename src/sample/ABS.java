package sample;


import java.math.BigInteger;

/**
 * Created by cf913 on 25/05/16.
 */
public class ABS {

    private static BigInteger key;
    public static void main(String[] args) {

        String[] list = split("Hello", 4, 4);

    }

    public static String[] split(String message, int shares, int threshold) {

        String[] results = new String[3];

        String msg = message;
        BigInteger secret    = new BigInteger(msg.getBytes());
        System.out.println("Secret: " + secret);

        BigInteger[] m = new BigInteger[shares + 1];


        // list of ms
        BigInteger left;
        BigInteger right;
        int p = 0;
        m[0] = SSSSplit.newPrime(secret.bitLength()+1);
        do {
            for (int i = 1; i < m.length; i++) {
                do {
                    m[i] = SSSSplit.newPrime(secret.bitLength() + 1);
                } while (inList(m[i],m, i));
            }
            left = m[0];
            right = BigInteger.ONE;

            for (int i = shares-threshold+2; i <= shares; i++) {
                left = left.multiply(m[i]);
            }

            for (int i = 1; i <= threshold; i++) {
                right = right.multiply(m[i]);
            }
            p++;
        } while (left.compareTo(right) >= 0);
        System.out.println("rounds" + p + "\n" + left + "\n" + right);




        key = m[0];

        BigInteger[] temp = m;
        BigInteger alpha = getAlpha(m, secret,threshold);
        BigInteger bloom = (secret.add(m[0].multiply(alpha)));

        System.out.println("bloom: " + bloom);
        String newshares = computShares(temp, bloom, shares);
        results[0] = newshares;
        results[1] = String.valueOf(threshold);
        results[2] = String.valueOf(key);


        ///////////////////////////////////////
//        BigInteger bigm = BigInteger.ONE;
//        for (int i = 1; i <= shares; i++) {
//            bigm = bigm.multiply(m[i]);
//        }
//        System.out.println("bigm: " + bigm);
//
//        String[] list = newshares.split("\\n");
//
//        BigInteger res = BigInteger.ZERO;
//        for (int i = 0; i < list.length; i++) {
//            String nobracket = list[i].replaceAll("[\\[\\](){}]", "");
//            String[] s = nobracket.split(",");
//            BigInteger a = new BigInteger(s[1]);
//            BigInteger b = bigm.divide(new BigInteger(s[1]));
//            BigInteger[] eu = euclid(a,b);
//            res = res.add((new BigInteger(s[0])).multiply(eu[1].multiply(b)));
//        }
//        System.out.println("res: " + res);
//        System.out.println("secret: " + (new BigInteger(String.valueOf(res))).mod(bigm).mod(key));

        ///////////////////////////////////////
        return results;
    }

    public static String reconstruct(int num, String shares, BigInteger key) {

        String result;
        String[] list = shares.split("\\n");

        String nobracket;
        String[] s;
        BigInteger bigm = BigInteger.ONE;
        for (int i = 0; i < list.length; i++) {
            nobracket = list[i].replaceAll("[\\[\\](){}]", "");
            s = nobracket.split(",");
            bigm = bigm.multiply(new BigInteger(s[1]));
        }

        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < list.length; i++) {
            nobracket = list[i].replaceAll("[\\[\\](){}]", "");
            s = nobracket.split(",");
            BigInteger a = new BigInteger(s[1]);
            BigInteger b = bigm.divide(new BigInteger(s[1]));
            BigInteger[] eu = euclid(a,b);
            res = res.add((new BigInteger(s[0])).multiply(eu[1].multiply(b)));
        }
        result = new String(res.mod(bigm).mod(key).toByteArray());
        System.out.println("Secret: " + res.mod(bigm).mod(key));
        return result;
    }

    private static boolean inList(BigInteger b, BigInteger[] m, int index) {
        boolean res = false;
        for (int i = 0; i < index; i ++) {
            if (b.compareTo(m[i]) == 0) {
                res = true;
            }
        }
        return res;
    }


    private static String computShares(BigInteger[] m, BigInteger bloom, int num) {

        String results = "";
        for (int i = 1; i <= num; i++) {

            BigInteger mi = m[i];
            BigInteger si = bloom.mod(m[i]);
            results = results.concat("(" + si + "," + mi + ")\n");
            System.out.println("(" + si + "," + mi + ")");

        }

        return results;
    }

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
        return list  ;

    }

    public static BigInteger getAlpha(BigInteger[] m, BigInteger secret, int thresh) {

        BigInteger alpha;
        BigInteger temp = BigInteger.ONE;

        for (int i = 1; i <= thresh; i++) {
            temp = temp.multiply(m[i]);
        }
        System.out.println("bitlength: " + temp.bitLength());
        alpha = SSSSplit.newPrime(temp.bitLength());
        int i = 0;

        //loop runs 3 times in average
        while ((secret.add(m[0].multiply(alpha))).compareTo(temp) >= 0) {
            //alpha should satisfy this condition
            alpha = SSSSplit.newPrime(alpha.bitLength()-i);
            i++;
        }
        System.out.println("i: " + i);

        System.out.println("S+al: " + (secret.add(m[0].multiply(alpha))));
        System.out.println("temp: " + temp);
        System.out.println("alph: " + alpha);


        return alpha;
    }
}
