package sample;

/**
 * Created by cf913 on 14/05/16.
 */
import javafx.scene.control.Alert;
import java.math.BigInteger;
import java.util.Random;

/**
 * XOR Secret Sharing Scheme
 */
public class XOR {

    ////////////////////////////////SPLIT/////////////////////////////////////////////
    /*********************************************************************************
     * Splits a secret message in n shares
     * @param message
     *          secret message
     * @param n
     *          number of shares required
     * @return String representation of a list of shares: share1\nshare2\n...sharen\n
     *********************************************************************************/
    public static String split(String message, int n) {

        final long start = System.nanoTime();

        String result = "";
        BigInteger bitrep = new BigInteger(message.getBytes());
        //BigInteger bitrep = new BigInteger(message);

        String[] list = newshares(bitrep, n);
        BigInteger px = computeXor(bitrep, list);
        list[n-1] = String.valueOf(px);

        for (String s: list) {
            result = result.concat(s+"\n");
        }

        System.out.println("RuntimeSplit:" + (System.nanoTime() - start));
        return result;
    }

    /*************************************************************
     * Generates a list of random Bigintegers of size secret
     * @param bitrep
     *          BigInteger representation of the secret message
     * @param shares
     *          number of shares required
     * @return list of keys with last elem (list[shares-1]) empty
     *************************************************************/
    public static String[] newshares(BigInteger bitrep, int shares) {
        String[] list = new String[shares];

        for (int i = 0; i < list.length-1; i++) {

            BigInteger b = getRandomBigIntegerSameBitLength(bitrep);
            list[i] = String.valueOf(b);

        }

        return list;
    }

    /****************************************************
     * Generates a random Biginteger of a given bitsize
     * @param max
     *          target bitsize
     * @return random BigInteger of size max
     ****************************************************/
    private static BigInteger getRandomBigIntegerSameBitLength(BigInteger max) {

        Random rand = new Random();
        BigInteger upperLimit = max;
        BigInteger result;

        do {
            result = new BigInteger(upperLimit.bitLength(), rand);
        } while(result.bitLength() != max.bitLength());

        return result;
    }

    /*********************************************************************
     * Computes share n:
     * Performs the XORing of the secret and every element of a list of
     * random numbers of size secret.
     * @param bitrep
     *          BigInteger representation of the secret message
     * @param list
     *          list of keys where last elem is empty
     * @return share n
     *********************************************************************/
    public static BigInteger computeXor(BigInteger bitrep, String[] list) {

        BigInteger result = bitrep;

        for (int i = 0; i < list.length-1; i++) {
            result = result.xor(new BigInteger(list[i]));
        }

        return result;
    }

    ///////////////////////////////////COMBINE//////////////////////////////////
    /**************************************************
     * Combines n shares to recover the secret message
     * @param n
     *          number of shares to combine
     * @param shares
     *          shares to combine
     * @return secret message
     **************************************************/
    public static String combine(int n, String shares) {

        final long start = System.nanoTime();

        String[] list = shares.split("\\n");
        if (list.length != n) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Conflicting number of shares");
            alert.showAndWait();
            return "";
        }

        BigInteger result = computeXor(list);

        System.out.println("RuntimeComb:" + (System.nanoTime() - start));
        return new String(result.toByteArray());

    }

    /****************************************************
     * Computes the XOR of a list of keys
     * @param list
     *          list of keys
     * @return BigInteger representation of the secret
     ****************************************************/
    public static BigInteger computeXor(String[] list) {

        BigInteger result = new BigInteger(list[0]);

        for (int i = 1; i < list.length; i++) {
            result = result.xor(new BigInteger(list[i]));
        }

        return result;
    }

}
