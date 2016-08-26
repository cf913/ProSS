package picture;

/**
 * Created by cf913
 */
import sample.SSS;
import sample.XOR;
import java.math.BigInteger;

/**
 * COLOUR XOR Image Secret Sharing
 *
 * In this scheme, XOR secret sharing is performed over each pixel of the secret image.
 * Hence, the secret is always a colour, a number ranging from 0 to 255.
 *
 * This scheme is compatible with color images.
 * There is no loss of information after reconstruction
 */
public class ProcessXOR {

    /*********************************************
     * Splits a Picture in n shadow images
     * @param p
     *          Secret Picture
     * @param n
     *          Number of shadow images required
     * @return list[n] of shadow images
     *********************************************/
    /////////////////////////SPLIT//////////////////////////////////////
    public static Picture[] ProcessImage(Picture p, int n) {
        Picture[] list = new Picture[n];

        for (int k = 0; k < n; k++) {
            list[k] = Utils.createPicture(p.getWidth(), p.getHeight());
        }

        for (int i = 0; i < p.getHeight(); i++) {
            for (int j = 0; j < p.getWidth(); j++) {

                Color c = p.getPixel(j, i);

                Integer[] cols = new Integer[3];
                cols[0] = c.getRed();
                cols[1] = c.getGreen();
                cols[2] = c.getBlue();

                String[] s = new String[3];

                for (int l = 0; l < cols.length; l++) {
                    s[l] = split(cols[l], n);
                }

                String[] sharesR;
                String[] sharesG;
                String[] sharesB;

                for (int k = 0; k < list.length; k++) {

                    sharesR = s[0].split("\\n");
                    sharesG = s[1].split("\\n");
                    sharesB = s[2].split("\\n");

                    c.setRed(Integer.parseInt(sharesR[k]));
                    c.setGreen(Integer.parseInt(sharesG[k]));
                    c.setBlue(Integer.parseInt(sharesB[k]));

                    list[k].setPixel(j, i, c);

                }
            }
        }
        return list;
    }

    /********************************************
     * Splits a colour into n shares
     * @param message
     *          secret colour
     * @param n
     *          number of shares required
     * @return String representation of shares
     ********************************************/
    private static String split(int message, int n) {

        String result = "";
        BigInteger bitrep = (new BigInteger(String.valueOf(message)));

        String[] list = newshares(n);
        BigInteger px = XOR.computeXor(bitrep, list);
        list[n - 1] = String.valueOf(px);

        for (String s : list) {
            result = result.concat(s + "\n");
        }

        return result;
    }

    /************************************************************
     * Generates n random numbers between ranging from 1 to 255
     * @param n
     *          number of random numbers needed
     * @return list of random BigIntegers
     ************************************************************/
    private static String[] newshares(int n) {

        String[] list = new String[n];
        for (int i = 0; i < list.length-1; i++) {

            BigInteger b = SSS.getRandomBigInteger(new BigInteger("256"));
            list[i] = String.valueOf(b);

        }

        return list;
    }

    //////////////////////////COMBINE///////////////////////////////////////////////
    /*********************************************************
     * Combines shadow images into the original secret Image
     * @param pics
     *          list of shadow images
     * @return secret picture
     *********************************************************/
    public static Picture combinePics(Picture[] pics) {
        Picture res = Utils.createPicture(pics[0].getWidth(),pics[0].getHeight());

        for (int i = 0; i < pics[0].getHeight(); i++) {
            for (int j = 0; j < pics[0].getWidth(); j++) {

                String sharesR = "";
                String sharesG = "";
                String sharesB = "";

                for (int k = 0; k < pics.length; k++) {

                    Color c = pics[k].getPixel(j, i);

                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();

                    sharesR = sharesR.concat(String.valueOf(r) + "\n");
                    sharesG = sharesG.concat(String.valueOf(g) + "\n");
                    sharesB = sharesB.concat(String.valueOf(b) + "\n");

                }

                String resultR = combineVSS(sharesR);
                String resultG = combineVSS(sharesG);
                String resultB = combineVSS(sharesB);

                Color plop = res.getPixel(j,i);

                plop.setRed(Integer.parseInt(resultR));
                plop.setGreen(Integer.parseInt(resultG));
                plop.setBlue(Integer.parseInt(resultB));

                res.setPixel(j,i,plop);
            }
        }
        return res;
    }

    /*****************************************
     * Performs reconstruction xor operations
     * @param shares
     *          keys to xor
     * @return secret colour
     *****************************************/
    private static String combineVSS(String shares) {

        String[] list = shares.split("\\n");
        BigInteger result = XOR.computeXor(list);

        return String.valueOf(result);
    }

}
