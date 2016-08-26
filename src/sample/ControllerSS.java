package sample;

/**
 * Created by cf913
 */
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.application.Platform.exit;

public class ControllerSS {


    // Button
    @FXML private Button sss;
    @FXML private Button xor;
    @FXML private Button ab;
    @FXML private Button pss;
    @FXML private Button splitbutton;
    @FXML private Button splitbuttonxor;
    @FXML private Button splitbuttonab;
    @FXML private Button splitbuttonpss;
    @FXML private Button backtomain;
    @FXML private Button nextbutton;
    @FXML private Button prevbutton;
    @FXML private Button copybuttonab;
    @FXML private Button loadsharesbutton;
    @FXML private Button loadmultsharesbutton;
    @FXML private Button combinebutton;
    @FXML private Button combinebuttonxor;
    @FXML private Button combinebuttonab;
    @FXML private Button clearbuttonsplit;
    @FXML private Button clearbuttoncombine;
    @FXML private Button clearbuttonsplitxor;
    @FXML private Button clearbuttoncombinexor;
    @FXML private Button new1;

    // TextArea
    @FXML private TextArea secretbox;
    @FXML private TextArea outputbox;
    @FXML private TextArea shareboxcombine;
    @FXML private TextArea outputboxcombine;

    // ImageView
    @FXML private ImageView pssview;
    @FXML private ImageView outputpssview;

    // TextField
    @FXML private TextField sharebox;
    @FXML private TextField secretboxpss;
    @FXML private TextField threshbox;
    @FXML private TextField shareboxnumcombine;
    @FXML private TextField keyboxcombine;
    @FXML private TextField primebox;

    // CheckBox
    @FXML private CheckBox tik1;
    @FXML private CheckBox xortic;
    @FXML private CheckBox sstic;
    @FXML private CheckBox useKey;
    @FXML private CheckBox comparesecret;

    //ProgressBar
    @FXML private ProgressBar bar;

    //Label
    @FXML private Label labelprime;


    /***********************************************************************
     * Controls the behavior of scheme selection in the Main Menu window
     * Opens the window corresponding to the selected scheme
     * @param event
     *          button in the Main Menu window
     * @throws IOException
     ***********************************************************************/
    @FXML
    public void goTo(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;

        // selection
        if(event.getSource()==sss){
            stage= (Stage) sss.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("sss.fxml"));
        } else if(event.getSource()==xor){
            stage= (Stage) xor.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("xor.fxml"));
        } else if(event.getSource()==ab){
            stage= (Stage) ab.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ab.fxml"));
        } else if(event.getSource()==pss) {
            stage= (Stage) pss.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pss.fxml"));
        } else if(event.getSource()==prevbutton) {
            stage= (Stage) prevbutton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        } else if(event.getSource()==nextbutton) {
            stage= (Stage) nextbutton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("mainmenu2.fxml"));
        } else if(event.getSource()==new1) {
            stage= (Stage) new1.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("new1.fxml"));
            ////TO ADD newscheme///////////////
//        } else if(event.getSource()==newscheme) {
//            stage= (Stage) newscheme.getScene().getWindow();
//            root = FXMLLoader.load(getClass().getResource("newscheme.fxml"));

        } else {
            stage=(Stage) backtomain.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        }


        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }


    ///////////////////////////////////////////////////////////////////////
    /////////////////////////SPLIT/////////////////////////////////////////

    /*****************************************************************
     * Controls result of pressing SPLIT button for SSS, ABS and XOR
     * @param e
     *          split button
     *****************************************************************/
    @FXML
    public void dothesplit(ActionEvent e) {

        //invalid secret handling
        if (!validSecret()) {
            alertError("secret");
            return;

        //invalid number of shares handling
        } else if (!validNumberOfShares()) {
            alertError("numberofshares");
            return;
        } else {
            ////////////////////////////////////////////////////////////
            ////////////////SS AND ABS//////////////////////////////////

            if (e.getSource()==splitbutton || e.getSource()==splitbuttonab) {

                //invalid threshold value check
                if (!validThreshold()) {
                    alertError("threshold");
                    return;
                }


                String secret = secretbox.getText();
                int shares = Integer.parseInt(sharebox.getText());
                int thresh = Integer.parseInt(threshbox.getText());

                String[] s;
                //scheme selection (SSS or ABS)
                if (e.getSource()==splitbutton) {
                    String key = primebox.getText();

                    if (!primebox.isDisabled() && !key.isEmpty() && !validPrime(secret, key)) {
                        alertError("prime");
                        return;
                    }
                    if (primebox.isDisabled()) {
                        s = SSS.split(secret, shares, thresh, "");
                    } else {
                        s = SSS.split(secret, shares, thresh, key);
                    }
                    outputbox.setText("Shares:\n" + s[0] + "\nThreshold:\n" + s[2] + "\n\nKey:\n" + s[1]);
                    outputbox.setEditable(false);

                } else {

                    s = ABS.split(secret, shares, thresh);
                    outputbox.setText("Shares:\n" + s[0] + "\nThreshold:\n" + s[1]+ "\n\nKey:\n" + s[2]);
                    outputbox.setEditable(false);

                }

            ////////////////////////////////////////////////////////////////
            ////////////////////XOR/////////////////////////////////////
            } else if (e.getSource()==splitbuttonxor) {

                String secret = secretbox.getText();
                System.out.println("secret " + secret);
                int shares = Integer.parseInt(sharebox.getText());
                System.out.println("shares " + shares);
                String s = XOR.split(secret, shares);
                outputbox.setText("Shares:\n" + s);
                outputbox.setEditable(false);
            }
        }
    }

    @FXML
    public void dothesplitnew1(ActionEvent e) {
        //invalid secret handling
        if (!validSecret()) {
            alertError("secret");

            //invalid number of shares handling
        } else if (!validNumberOfShares()) {
            alertError("numberofshares");

        } else if (!validThreshold()) {
                    alertError("threshold");

        } else {

            int secret = Integer.parseInt(secretbox.getText());
            int shares = Integer.parseInt(sharebox.getText());
            int thresh = Integer.parseInt(threshbox.getText());

            String[] s;
            s = Blakley.split(secret, shares, thresh);
            outputbox.setText("Shares:\n" + s[0] + "\nThreshold:\n" + s[1]+ "\n\nKey:\n" + s[2]);
            outputbox.setEditable(false);

            // uncomment above and remove below once new scheme is implemented
//            outputbox.setText("Create new Splitting method and link it to\n" +
//                    "this Split button in ControllerSS.dothesplitnew1():\n" +
//                    "Call <yournewclass>.split(message, n, t)");
//            outputbox.setEditable(false);
        }
    }

    /*******************************************************
     * Controls result of pressing SPLIT button for PSS
     * @param e
     *          split button
     *******************************************************/
    @FXML
    public void dothesplitPSS(ActionEvent e) {

        //invalid secret handling
        if (!validSecretPSS()) {
            alertError("secret");
            return;

        //invalid number of shares handling
        }  else if (!validNumberOfShares()) {
            alertError("numberofshares");
            return;

        } else {

            // checkbox verification and scheme selection
            int numshares = Integer.parseInt(sharebox.getText());
            if (xortic.isSelected() && !sstic.isSelected()) {

                PSS.splitXOR(secretboxpss.getText(), numshares);
                outputbox.setText("Success \n" + numshares + " shares have been saved");
                outputbox.setEditable(false);
//           /////////////////////////////////////////////////
            } else if (!xortic.isSelected() && sstic.isSelected()) {

                PSS.splitSS(secretboxpss.getText(), numshares, Integer.parseInt(threshbox.getText()));
                outputbox.setText("Success \n" + numshares + " shares have been saved");
                outputbox.setEditable(false);

            } else {

                outputbox.setText("YOU HAVE FAILED, please select a scheme");
                outputbox.setEditable(false);

            }
        }
    }

    /**********************************************************
     * Controls result of clicking the Combine button for PSS
     * @param e
     *          PSS combine button
     **********************************************************/
    @FXML
    public void dothecombinePSS(ActionEvent e) {

        //check if number of shares is valid
        if (!validNumberOfShares()) {

            alertError("numberofshares");
            return;
        }
        int numshares = Integer.parseInt(shareboxnumcombine.getText());

        // checkbox verification and scheme selection
        if (xortic.isSelected() && !sstic.isSelected()) {
            PSS.combineXOR(shareboxcombine.getText(), numshares);
        } else if (!xortic.isSelected() && sstic.isSelected()) {
            PSS.combineSS(shareboxcombine.getText(), numshares);
        } else {
            return;
        }

        File file = new File("RESULT.png");
        Image image = new Image(file.toURI().toString());
        outputpssview.setImage(image);

    }


    ///////////////////////////////////////////////////////////////////////
    /////////////////////////RECONSTRUCTION////////////////////////////////

    @FXML
    public void dothecombinenew1(ActionEvent e) {
        if (!validNumberOfShares()) {

            alertError("numberofshares");
            return;

        } else if (!validKey()) {

            alertError("key");
            return;

                //check validity of shares
//        } else if (!validShares()) {
//
//            alertError("shares");
//            return;

        } else {

            int numshares = Integer.parseInt(shareboxnumcombine.getText());
            String shares = shareboxcombine.getText();
            String s;
            int key = Integer.parseInt(keyboxcombine.getText());
            //BigInteger key = new BigInteger(keyboxcombine.getText());
            s = Blakley.reconstruct(shares, numshares, numshares);
            outputboxcombine.setText(s);
            outputboxcombine.setEditable(false);


            // uncomment above and remove below once new scheme is implemented
//            outputboxcombine.setText("Create new Reconstruction method and link it to\n" +
//                    "this combine button in ControllerSS.dothecombinenew1():\n" +
//                    "Call <yournewclass>.recontruct(message, n, t)");
//            outputboxcombine.setEditable(false);
        }
    }

    /****************************************************************
     * Controls behavior of the Combine button for SSS, ABS and XOR
     * @param e
     *
     ****************************************************************/
    @FXML
    public void dothecombine(ActionEvent e) {

        //check validity of number of shares
        if (!validNumberOfShares()) {

            alertError("numberofshares");
            return;

        //////////////////////SSS and ABS///////////////////////////////////////////
        } else if (e.getSource() == combinebutton || e.getSource() == combinebuttonab) {

            //check validity of key
            if (!validKey()) {

                alertError("key");
                return;

                //check validity of shares
            } else if (!validShares()) {

                alertError("shares");
                return;

            } else {

                int numshares = Integer.parseInt(shareboxnumcombine.getText());
                String shares = shareboxcombine.getText();

                String s;
                BigInteger key = new BigInteger(keyboxcombine.getText());

                //scheme selection
                if (e.getSource() == combinebutton) {
                    s = SSS.reconstruct(numshares, shares, key);
                } else {
                    s = ABS.reconstruct(numshares, shares, key);
                }

                outputboxcombine.setText(s);
                outputboxcombine.setEditable(false);
            }

        ///////////////////////////////XOR//////////////////////////////////////
        } else if (e.getSource() == combinebuttonxor) {

            if (!validSharesXOR()) {

                alertError("shares");
                return;

            } else {

                int numshares = Integer.parseInt(shareboxnumcombine.getText());
                String shares = shareboxcombine.getText();

                String s = XOR.combine(numshares, shares);
                outputboxcombine.setText(s);
                outputboxcombine.setEditable(false);

            }
        }
    }




    ///////////////////////////////////////////////////////////////////////
    /////////////////////////UTILS//////////////////////////////////////////

    /***********************************************************
     * Set of methods that check the validity of inputted values
     * secret, number of shares, threshold, key and more
     ***********************************************************/
    private boolean validSecret() {
        return !secretbox.getText().isEmpty();
    }
    private boolean validSecretPSS() {
        return !secretboxpss.getText().isEmpty();
    }
    private boolean validNumberOfShares() {
        return (!sharebox.getText().isEmpty() && isInt(sharebox.getText())
            && Integer.parseInt(sharebox.getText()) > 0);
    }
    private boolean validThreshold() {
        return (!threshbox.getText().isEmpty() && isInt(threshbox.getText())
                && Integer.parseInt(threshbox.getText()) > 0
                && Integer.parseInt(threshbox.getText()) <= Integer.parseInt(sharebox.getText()));
    }
    private boolean validKey() {
        return !keyboxcombine.getText().isEmpty() && isInt(keyboxcombine.getText());
    }
    private boolean validShares() {
        return !shareboxcombine.getText().isEmpty() && isGoodShare(shareboxcombine.getText());
    }
    private boolean validSharesXOR() {
        return !shareboxcombine.getText().isEmpty() || isGoodShareXor(shareboxcombine.getText());
    }
    private boolean validPrime(String secret, String key) {
        System.out.println("Secret: " + secret);
        System.out.println("Key: " + key);
        BigInteger s = new BigInteger(secret.getBytes());
        BigInteger k = new BigInteger(key);

        return (k.compareTo(s) > 0 && k.isProbablePrime(100));
    }

    /*****************
     * Error Alert
     *****************/
    private void alertError(String errortype) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");

        switch(errortype) {
            case "secret":
                alert.setContentText("No secret found");
                break;
            case "numberofshares":
                alert.setContentText("Enter a valid number of shares");
                break;
            case "threshold":
                alert.setContentText("Enter a valid threshold =< number of shares");
                break;
            case "shares":
                alert.setContentText("Shares are not valid!!!!!");
                break;
            case "key":
                alert.setContentText("Key not valid");
                break;
            case "prime":
                alert.setContentText("Key is not a prime number or\nis smaller than the secret");
        }

        alert.showAndWait();

    }


    /***********************************************
     * Opens directory where the images are saved
     * @param e
     *          Go To File button
     * @throws IOException
     ***********************************************/
    @FXML
    public void gotofile(ActionEvent e) throws IOException {

        String current = new File( "." ).getCanonicalPath();
        outputbox.setText(current);
        Desktop.getDesktop().open(new File(current));

    }


    /**************************************************************
     * Opens a Dialog to load a single file containing all shares
     * @param e
     *          Load File button
     * @throws FileNotFoundException
     **************************************************************/
    @FXML
    public void loadShares(ActionEvent e) throws FileNotFoundException {

        String[] res;
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(Main.stage);
        res = readfile(file);
        shareboxcombine.setText(res[0]);
        keyboxcombine.setText(res[1]);

    }

    /******************************************************************
     * Opens a Dialog to load a single image (.PNG)
     * @param e
     * @throws FileNotFoundException
     ******************************************************************/
    @FXML
    public void loadImage(ActionEvent e) throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(Main.stage);
        secretboxpss.setText(file.getAbsolutePath());
        Image image = new Image(file.toURI().toString());
        pssview.setImage(image);

    }

    /***********************************************
     * Opens a Dialog to load multiple images(.PNG)
     * @param e
     * @throws FileNotFoundException
     ***********************************************/
    @FXML
    public void loadImages(ActionEvent e) throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        List<File> file = fileChooser.showOpenMultipleDialog(Main.stage);

        String urls = "";
        for (File f :file) {
            urls = urls.concat(f.getAbsolutePath() + "\n");
        }

        shareboxcombine.setText(urls);

    }

    /*****************************************************
     * Reads a file and extract the Shares and the Key
     * @param file
     * @return list [Shares, key]
     * @throws FileNotFoundException
     *****************************************************/
    private String[] readfile(File file) throws FileNotFoundException {

        String[] res = new String[2];
        Scanner s = new Scanner(file).useDelimiter("\\n");
        String shares = "";
        String token;
        String key = "";

        while (s.hasNext()) {
            token = s.next();

            if (isGoodShare(token)) { // check if next token is an int
                shares = shares.concat(token + "\n");
            }

            if (isInt(token)) { // check if next token is an int
                key = token;
            }
        }

        shareboxcombine.setText(shares);
        res[0] = shares;
        res[1] = key;
        return res;

    }

    /**************************************************************
     * Opens a Dialog to load multiple files containing all shares
     * @param e
     *          Load File button
     * @throws FileNotFoundException
     **************************************************************/
    @FXML
    public void loadShares2(ActionEvent e) throws FileNotFoundException {

        String[] temp = new String[2];
        String[] res = new String[2];
        res[0] = "";
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        List<File> list = fileChooser.showOpenMultipleDialog(Main.stage);

        for (File file : list) {

            temp = readfile(file);
            res[0] = res[0].concat(temp[0]);

        }

        res[1] = temp[1];
        shareboxcombine.setText(res[0]);
        keyboxcombine.setText(res[1]);

    }

    /*******************************************************
     * Save a file containing shares, threshold and key
     * Save multiple file with one share and key each if
     * the CheckBox "One Share Per File" is selected.
     * Shares.txt
     * or
     * Share0.txt, Share1.txt, ..., Sharen.txt
     * @param e
     *******************************************************/
    @FXML
    public void saveFile(ActionEvent e) {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);


        //one file
        if (!tik1.isSelected()) {

            fileChooser.setInitialFileName("Shares.txt");
            File file = fileChooser.showSaveDialog(Main.stage);

            if (file != null) {
                SaveFile(outputbox.getText(), file);
            }

        //multiple files
        } else {

            String out = outputbox.getText();
            int share = Integer.parseInt(sharebox.getText());

            String[] list = out.split("\\n");

            String key = list[share + 6];


            fileChooser.setInitialFileName("Share0.txt");
            File file = fileChooser.showSaveDialog(Main.stage);
            System.out.println(file.getName());

            if (file != null) {
                SaveFile("Share 0:\n" + list[1] + "\nKey;\n" + key , file);
            }

            for (int i = 1; i < share; i++) {
                String filename = file.getName();
                filename = filename.substring(0, filename.length() - 4);
                SaveFile("Share "+i+":\n" + list[i+1] + "\nKey; \n" + key ,
                        new File(file.getParent() + "/" + filename + i + ".txt"));
            }


        }

    }

    /*******************************************
     * Used in saveFile. Save content to a file
     * @param content
     *          String to save
     * @param file
     *          Path to file
     *******************************************/
    private void SaveFile(String content, File file) {
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ControllerSS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**************************************************************
     * Clears the input boxes in each Secret sharing scheme window
     * @param e
     **************************************************************/
    @FXML
    public void clearInput(ActionEvent e) {

        //SSS and ABS split
        if (e.getSource() == clearbuttonsplit) {

            secretbox.setText("");
            sharebox.setText("");
            threshbox.setText("");

        }
        //SSS and ABS combine
        if (e.getSource() == clearbuttoncombine) {

            shareboxcombine.setText("");
            keyboxcombine.setText("");
            shareboxnumcombine.setText("");

        }

        //XOR split
        if (e.getSource() == clearbuttonsplitxor) {

            secretbox.setText("");
            sharebox.setText("");

        }

        //XOR ocmbine
        if (e.getSource() == clearbuttoncombinexor) {

            shareboxcombine.setText("");
            shareboxnumcombine.setText("");

        }
    }

    /*********************
     * Exits the program
     * @param e
     *********************/
    @FXML
    public void quit(ActionEvent e) {
        exit();
    }


    /**********************************************************
     * displays a preview of of the secret value in SSS
     * when the "show secret" checkbox is ticked
     * Used to compare the size fo the secret to a given key
     **********************************************************/
    @FXML
    public void showSecret() {

        if (!comparesecret.isSelected()) {
            labelprime.setText("");

        } else {
            labelprime.setText(String.valueOf(new BigInteger(secretbox.getText().getBytes())));
        }

    }

    /*************************************************************
     * Controls the behavior of the "Use own Key" checkbox in SSS.
     *************************************************************/
    @FXML
    public void useKey() {

        if (!useKey.isSelected()) {

            primebox.setDisable(true);
            comparesecret.setDisable(true);

        } else {
            primebox.setDisable(false);
            comparesecret.setDisable(false);
        }
    }

    /***********************************
     * Scheme selection control in PSS
     *
     ***********************************/
    @FXML
    public void untick(ActionEvent e) {

        if (e.getSource() == xortic) {

            sstic.setSelected(false);
            threshbox.setDisable(true);

        } else {

            xortic.setSelected(false);
            threshbox.setDisable(false);

        }
    }


    /*******************************************************************************************
     * Copies content of Splitting outputbox into each corresponding Recontruction input boxes
     * for (t,n) threshold schemes
     * @param e
     *******************************************************************************************/
    //FOR TESTING PURPOSES
    @FXML
    public void copyIt(ActionEvent e) {

        String out = outputbox.getText();
        int share = Integer.parseInt(sharebox.getText());

        String[] list = out.split("\\n");
        String shares = "";

        String thresh = list[share + 3];

        for (int i = 0; i < Integer.parseInt(thresh); i++) {
            shares = shares.concat(list[i + 1] + "\n");
        }

        String key = list[share + 6];
        keyboxcombine.setText(key);
        shareboxnumcombine.setText(thresh);
        shareboxcombine.setText(shares);
    }

    /*******************************************************************************************
     * Copies content of Splitting outputbox into each corresponding Recontruction input boxes
     * for XOR
     * @param e
     *******************************************************************************************/
    @FXML
    public void copyItXOR(ActionEvent e) {

        String out = outputbox.getText();
        String[] list = out.split("\\n");
        String shares = "";

        for (int i = 1; i < list.length; i++) {
            shares = shares.concat(list[i] + "\n");
        }

        shareboxnumcombine.setText(String.valueOf(list.length-1));
        shareboxcombine.setText(shares);

    }

    /********************************************************************
     * Checks if input is a valid number, contains only numbers 0-9
     * @param text
     *          input text
     * @return
     ********************************************************************/
    private boolean isInt(String text)
    {
        return text.matches("[0-9]*");
    }

    /*******************************************
     * Check the validity of shares
     * @param text
     *          String representation of shares
     * @return
     *******************************************/
    private boolean isGoodShare(String text) {

        boolean b = true;
        String[] split = text.split("\\n");

        for (String s : split) {

            if (!s.matches("(\\((\\d+)\\,(\\d+)\\))")) {

                System.out.println("Share is not valid");
                System.out.println(s);
                b = false;

            }

        }
        return b;
    }

    /************************************************
     * Checks if XOR shares are valid
     * @param text
     *          String representation of XOR shares
     * @return
     ************************************************/
    private boolean isGoodShareXor(String text) {

        boolean b = true;
        String[] split = text.split("\\n");

        for (String s : split) {

            if (!isInt(s)) {
                b = false;

            }
        }

        return b;
    }

}
