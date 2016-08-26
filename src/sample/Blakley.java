package sample;

import java.util.Random;

/**
 * Created by cf913
 *
 * works for small number but without using prime modulus as i
 * haven't figured out how it works
 * TODO: Finish Blakleys Secret sharing scheme
 * Add support for BigInteger
 * Link to GUI
 * Add prime modulus
 *
 */
public class Blakley {

    public static void main(String args[]) {
        int secret = Integer.parseInt(args[0]);
        int shares = Integer.parseInt(args[1]);
        int threshold = Integer.parseInt(args[2]);
        //System.out.println(secret + "\n" + shares + "\n" + threshold);

        String[] res = split(secret, shares, threshold);
        String newshares = res[0];
        //System.out.println(newshares);
        //String[] list = newshares.split("\\n");
        //newshares = new String(list[0] + "\n" + list[1] + "\n" + list[2] + "\n" + list[3]);
        System.out.println(newshares);

        String secretpoint = reconstruct(newshares, 2, threshold);
        System.out.println(secretpoint);

    }

    public static String reconstruct(String shares, int n, int t) {
        String[] list = shares.split("\\n");
//        if (list.length > t) {
//            for (int i = 0; i < t; i++) {
//
//            }
//        }
        double[][] matrix = new double[t][t+1];
        for (int i = 0; i <t; i++) {
            String nobracket = list[i].replaceAll("[\\[\\](){}]", "");
            String[] s = nobracket.split(",");
            for (int j = 0; j < matrix[0].length; j++) {

                if (j == matrix[0].length - 2) {
                    matrix[i][j] = -1;
                    matrix[i][j+1] = Integer.parseInt(s[j]);
                    break;
                } else {
                    matrix[i][j] = Integer.parseInt(s[j]);
                }
            }

        }

//        for (int i = 0; i < t; i++) {
//            System.out.print("|\t");
//            for (int j = 0; j < t+1; j++) {
//                System.out.print(matrix[i][j] + "\t");
//            }
//            System.out.println("|");
//        }
//        Equations.solve(matrix);
//        for (int i = 0; i < t; i++) {
//            System.out.print("|\t");
//            for (int j = 0; j < t+1; j++) {
//                System.out.print(matrix[i][j] + "\t");
//            }
//            System.out.println("|");
//        }
        double[] solution = new double[t];
        new simeq(t, matrix, solution);
//        for (double d : solution) {
//            System.out.print(d + ",");
//        }
        return String.valueOf(Math.round(solution[0]));
    }

    public static String[] split(int secret, int n, int t){
        String[] results = new String[3];
        String result = "";
        int prime = 251;

        Random rand = new Random();
        Integer[] point = new Integer[t];
        point[0] = secret;

        for (int i = 1; i < point.length; i++) {
            point[i] = rand.nextInt(prime);
        }

        Integer[] coef = new Integer[t-1];
        //System.out.println("(" + point[0] + "," + point[1] + "," + point[2] + ")");
        for (int i = 0; i < n; i++) {
            for (int j= 0; j < coef.length; j++) {
                coef[j] = rand.nextInt(prime);
            }

            int c = point[point.length-1];
            for (int k = 0; k < coef.length; k++) {
                //System.out.println("k: " + k + " coef[k]: " + coef[k] + " point[k]: " + point[k]);
                c -= (coef[k] * point[k]);// % prime;
            }
            result = result.concat("(");
            for (int l = 0; l < coef.length; l++) {
                result = result.concat(String.valueOf(coef[l])+ ",");
            }
            result = result.concat(-c +")\n");

        }
        System.out.print(result);
        results[0] = result;
        results[1] = String.valueOf(t);
        results[2] = String.valueOf(prime);
        return results;
    }


}
