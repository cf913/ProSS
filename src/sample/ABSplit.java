package sample;


import java.math.BigInteger;

/**
 * Created by cf913 on 25/05/16.
 */
public class ABSplit {

    public static void main(String[] args) {
        split("Hello", 10, 2);
    }

    public static String[] split(String message, int shares, int threshold) {

        String[] results = new String[2];
        String msg = message;
        BigInteger secret    = new BigInteger(msg.getBytes());

        BigInteger[] m = new BigInteger[shares + 1];


        // list of ms
        for (int i = 0; i < m.length; i++) {
            m[i] = SSSSplit.newPrime(secret.bitLength() + i);
        }
        for (int i = 0; i < m.length; i++) {
            System.out.println(i + " = " + m[i]);
        }


        return results;
    }

    public static Integer[] euclid(int a, int b) {
        int r = a;
        int rprime = b;
        int u = 1;
        int v = 0;
        int uprime = 0;
        int vprime = 1;
        int q;
        int rs, us, vs;

        while (rprime != 0) {
            q = r/rprime;

            rs = r;
            us = u;
            vs = v;

            r = rprime;
            u = uprime;
            v = vprime;

            rprime = rs - q*rprime;
            uprime = us - q*uprime;
            vprime = vs - q*vprime;

        }

        System.out.println("r = " + r);
        System.out.println("u = " + u);
        System.out.println("v = " + v);

        Integer[] list = new Integer[2];
        list[0] = u;
        list[1] = v;
        return list  ;

    }
}
