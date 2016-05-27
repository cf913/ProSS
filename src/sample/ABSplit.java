package sample;


import java.math.BigInteger;

/**
 * Created by cf913 on 25/05/16.
 */
public class ABSplit {

    public static void main(String[] args) {

        String[] list = split("Hello", 11, 11);
        System.out.println(list[0]);
        System.out.println(list[1]);

    }

    public static String[] split(String message, int shares, int threshold) {

        String[] results = new String[2];

        String msg = message;
        BigInteger secret    = new BigInteger(msg.getBytes());
        System.out.println("Secret: " + secret);

        BigInteger[] m = new BigInteger[shares + 1];


        // list of ms
        for (int i = 0; i < m.length; i++) {
            m[i] = SSSSplit.newPrime(secret.bitLength() + i);
        }
        //m[0] = new BigInteger("3");
        //m[1] = new BigInteger("11");
        //m[2] = new BigInteger("13");
        //m[3] = new BigInteger("17");
        //m[4] = new BigInteger("19");

        for (int i = 0; i < m.length; i++) {
            System.out.println(i + " = " + m[i]);
        }

        BigInteger[] temp = m;
        BigInteger alpha = new BigInteger("51");//getAlpha(m, secret,threshold);
        BigInteger bloom = (secret.add(m[0].multiply(alpha)));

        System.out.println("bloom: " + bloom);
        String newshares = computShares(temp, bloom, threshold);
        results[0] = newshares;
        results[1] = String.valueOf(threshold);


        ///////////////////////////////////////
        BigInteger bigm = BigInteger.ONE;
        for (int i = 1; i <= threshold; i++) {
            bigm = bigm.multiply(m[i]);
        }
        System.out.println("bigm: " + bigm);

        String[] list = newshares.split("\\n");

        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < list.length; i++) {
            String nobracket = list[i].replaceAll("[\\[\\](){}]", "");
            String[] s = nobracket.split(",");
            BigInteger a = new BigInteger(s[1]);
            BigInteger b = bigm.divide(new BigInteger(s[1]));
            BigInteger[] eu = euclid(a,b);
            res = res.add((new BigInteger(s[0])).multiply(eu[1].multiply(b)));
        }
        System.out.println("res: " + res);
        System.out.println("secret: " + (new BigInteger(String.valueOf(res))).mod(bigm).mod(m[0]));
        ///////////////////////////////////////
        return results;
    }

    private static String computShares(BigInteger[] m, BigInteger bloom, int thresh) {

        String results = "";
        for (int i = 1; i <= thresh; i++) {

            BigInteger mi = m[i];
            BigInteger si = bloom.mod(m[i]);
            results = results.concat("(" + si + "," + mi + ")\n");


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
        //while ((secret.add(m[0].multiply(alpha))).compareTo(temp) >= 0) {
            //alpha should satisfy this condition
            alpha = SSSSplit.newPrime(temp.bitLength()-m[0].bitLength());
            //i++;
       // }

        System.out.println("S+al: " + (secret.add(m[0].multiply(alpha))));
        System.out.println("temp: " + temp);
        System.out.println("alph: " + alpha);


        return alpha;
    }
}
