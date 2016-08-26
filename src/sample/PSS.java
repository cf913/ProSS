package sample;

/**
 * Created by cf913
 */

import javafx.scene.control.Alert;
import picture.*;

import picture.ProcessSS;
import picture.Utils;

/**
 * This class handles Picture secret sharing.
 * Contains methods for splitting and combining
 * for each implemented PSS scheme.
 */
public class PSS {

    public static void splitXOR(String url, int shares) {
        final long start = System.nanoTime();
        Picture p = Utils.loadPicture(url);
        if (p.equals(null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Image does not exist");
            alert.showAndWait();
            return;
        }
        Picture[] list = ProcessXOR.ProcessImage(p, shares);
        for (int i = 0; i < list.length; i++) {
            Utils.savePicture(list[i], ("Share" + i + ".png"));
        }
        System.out.println("Runtime:" + (System.nanoTime() - start));
        System.out.print("Done!");
    }

    public static void splitSS(String url, int shares, int t) {
        final long start = System.nanoTime();
        Picture p = Utils.loadPicture(url);
        if (p.equals(null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Image does not exist");
            alert.showAndWait();
            return;
        }
        Picture[] list = ProcessSS.ProcessImageSS(p, shares, t);
        for (int i = 0; i < list.length; i++) {
            Utils.savePicture(list[i], ("Share" + i + ".png"));
        }
        System.out.print("Done!");
        System.out.println("Runtime:" + (System.nanoTime() - start));
    }

    public static void combineXOR(String urls, int numshares) {
        final long start = System.nanoTime();
        String[] shares = urls.split("\\n");
        if (numshares != shares.length) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Conflicting number of shares");
            alert.showAndWait();
        } else {
            Picture[] pics = new Picture[shares.length];

            for (int i = 0; i < pics.length; i++) {
                pics[i] = Utils.loadPicture(shares[i]);
            }

            System.out.println("pictures: " + pics.length);
            Picture res = ProcessXOR.combinePics(pics);
            Utils.savePicture(res, "RESULT.png");
        }
        System.out.println("Runtime:" + (System.nanoTime() - start));
    }


    public static void combineSS(String urls, int numshares) {
        final long start = System.nanoTime();
        String[] shares = urls.split("\\n");
        if (numshares != shares.length) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Conflicting number of shares");
            alert.showAndWait();
        } else {
            Picture[] pics = new Picture[shares.length];

            for (int i = 0; i < pics.length; i++) {
                pics[i] = Utils.loadPicture(shares[i]);
            }

            System.out.println("pictures: " + pics.length);
            Picture res = ProcessSS.combinePicsSS(pics);
            Utils.savePicture(res, "RESULT.png");
        }
        System.out.println("Runtime:" + (System.nanoTime() - start));
    }

    //public static void

}