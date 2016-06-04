package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import picture.Picture;

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



    @FXML private Button sss;
    @FXML private Button xor;
    @FXML private Button ab;
    @FXML private Button pss;
    @FXML private Button splitbutton;
    @FXML private Button splitbuttonxor;
    @FXML private Button splitbuttonab;
    @FXML private Button splitbuttonpss;
    @FXML private Button backtomain;
    @FXML private Button gotofilebutton;
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

    @FXML private TextArea secretbox;
    @FXML private TextArea outputbox;

    @FXML private ImageView pssview;
    @FXML private ImageView outputpssview;
    @FXML private ImageView plop;


    @FXML private TextField sharebox;
    @FXML private TextField secretboxpss;
    @FXML private TextField threshbox;

    @FXML private TextField shareboxnumcombine;
    @FXML private TextArea shareboxcombine;
    @FXML private TextField keyboxcombine;
    @FXML private TextArea outputboxcombine;


    @FXML private CheckBox tik1;
    @FXML private CheckBox xortic;
    @FXML private CheckBox sstic;


    @FXML
    public void goTo(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
        if(event.getSource()==sss){
            stage=(Stage) sss.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("sss.fxml"));
        } else if(event.getSource()==xor){
            stage=(Stage) xor.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("xor.fxml"));
        } else if(event.getSource()==ab){
            stage=(Stage) ab.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ab.fxml"));
        } else if(event.getSource()==pss){
            stage=(Stage) pss.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pss.fxml"));

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
    @FXML
    public void dothesplit(ActionEvent e) {


        ////////////////////////////////////////////////////////////
        //invalid input handling
        if (secretbox.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No secret found");
            alert.showAndWait();

        } else if (sharebox.getText().isEmpty() || !isInt(sharebox.getText())
                || Integer.parseInt(sharebox.getText()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Enter a valid number of shares");
            alert.showAndWait();


        } else {
            ////////////////////////////////////////////////////////////
            ////////////////SS//////////////////////////////////////////

            if (e.getSource()==splitbutton || e.getSource()==splitbuttonab) {

                if (threshbox.getText().isEmpty() || !isInt(threshbox.getText())
                        || Integer.parseInt(threshbox.getText()) <= 0
                        || Integer.parseInt(threshbox.getText()) > Integer.parseInt(sharebox.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Enter a valid threshold =< number of shares");
                    alert.showAndWait();
                }

                String secret = secretbox.getText();
                int shares = Integer.parseInt(sharebox.getText());
                int thresh = Integer.parseInt(threshbox.getText());

                String[] s;

                if (e.getSource()==splitbutton) {
                    s = SSSSplit.split(secret, shares, thresh);
                    secretbox.setText("");
                    outputbox.setText("Shares:\n" + s[0] + "\nThreshold:\n" + s[2] + "\n\nKey:\n" + s[1]);
                    outputbox.setEditable(false);
                } else {
                    s = ABS.split(secret, shares, thresh);
                    secretbox.setText("");
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
                String s = XORSplit.split(secret, shares);
                secretbox.setText("");
                outputbox.setText("Shares:\n" + s);
                outputbox.setEditable(false);
            }
        }
    }

    @FXML
    public void dothesplitPSS(ActionEvent e) {
        if (secretboxpss.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No secret found");
            alert.showAndWait();
        }  else if (sharebox.getText().isEmpty() || !isInt(sharebox.getText())
            || Integer.parseInt(sharebox.getText()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Enter a valid number of shares");
            alert.showAndWait();
        } else {
            int numshares = Integer.parseInt(sharebox.getText());
            if (xortic.isSelected() && !sstic.isSelected()) {
                PSS.split(secretboxpss.getText(), numshares);
                outputbox.setText("Success \n" + numshares + " shares have been saved");
                outputbox.setEditable(false);
            } else if (!xortic.isSelected() && sstic.isSelected()) {
                PSS.splitSS(secretboxpss.getText(), numshares);
                outputbox.setText("Success \n" + numshares + " shares have been saved");
                outputbox.setEditable(false);
            } else {
                outputbox.setText("YOU HAVE FAILED, please untick one checkbox");
                outputbox.setEditable(false);
            }
        }

    }

    @FXML
    public void dothecombinePSS(ActionEvent e) {
        if (shareboxnumcombine.getText().isEmpty() || !isInt(shareboxnumcombine.getText())
                || Integer.parseInt(shareboxnumcombine.getText()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Enter a valid number of shares");
            alert.showAndWait();
        }
            int numshares = Integer.parseInt(shareboxnumcombine.getText());

        if (xortic.isSelected() && !sstic.isSelected()) {
            PSS.combine(shareboxcombine.getText(), numshares);
        } else if (!xortic.isSelected() && sstic.isSelected()) {
            PSS.combineSS(shareboxcombine.getText(), numshares);
        } else {
            return;
        }
        File file = new File("RESULT.png");
        Image image = new Image(file.toURI().toString());
        outputpssview.setImage(image);


    }

    @FXML
    public void gotofile(ActionEvent e) throws IOException {
//        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
//        fileChooser.getExtensionFilters().add(extFilter);
//        File file = fileChooser.showOpenDialog(Main.stage);
        String current = new File( "." ).getCanonicalPath();
        outputbox.setText(current);
        Desktop.getDesktop().open(new File(current));

    }



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

    @FXML
    public void loadImage(ActionEvent e) throws FileNotFoundException {
        String[] res;
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(Main.stage);
        secretboxpss.setText(file.getAbsolutePath());
        Image image = new Image(file.toURI().toString());
        pssview.setImage(image);

    }

    @FXML
    public void loadImages(ActionEvent e) throws FileNotFoundException {
        String[] res;
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

    //load multiple shares
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

    @FXML
    public void saveFile(ActionEvent e) {

            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);


        if (!tik1.isSelected()) {

            fileChooser.setInitialFileName("Shares.txt");
            File file = fileChooser.showSaveDialog(Main.stage);

            if (file != null) {
                SaveFile(outputbox.getText(), file);
            }
        } else {

            String out = outputbox.getText();
            int share = Integer.parseInt(sharebox.getText());

            String[] list = out.split("\\n");
            String shares = "";

            String key = list[share + 6];


            fileChooser.setInitialFileName("Share0.txt");
            File file = fileChooser.showSaveDialog(Main.stage);
            if (file != null) {
                SaveFile("Share 0:\n" + list[1] + "\nKey;\n" + key , file);
            }

            for (int i = 1; i < share; i++) {
                SaveFile("Share "+i+":\n" + list[i+1] + "\nKey; \n" + key , new File(file.getParent() + "/Share" + i + ".txt"));

            }


        }

    }


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


    ///////////////////////////////////////////////////////////////////////
    /////////////////////////RECONSTRUCTION////////////////////////////////

    @FXML
    public void dothecombine(ActionEvent e) {

        if (shareboxnumcombine.getText().isEmpty() || !isInt(shareboxnumcombine.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Share number not valid");
            alert.showAndWait();

        } else if (e.getSource() == combinebutton || e.getSource() == combinebuttonab) {


            if (keyboxcombine.getText().isEmpty() || !isInt(keyboxcombine.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Key not valid");
                alert.showAndWait();
                return;
            } else if (shareboxcombine.getText().isEmpty() || !isGoodShare(shareboxcombine.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Shares are not valid");
                alert.showAndWait();
            } else {

                int numshares = Integer.parseInt(shareboxnumcombine.getText());
                String shares = shareboxcombine.getText();


                String s;
                BigInteger key = new BigInteger(keyboxcombine.getText());
                if (e.getSource() == combinebutton) {
                    s = SSSReconstruct.reconstruct(numshares, shares, key);
                } else {
                    s = ABS.reconstruct(numshares, shares, key);
                }
                outputboxcombine.setText(s);
                outputboxcombine.setEditable(false);
            }


        } else if (e.getSource() == combinebuttonxor) {
            if (shareboxcombine.getText().isEmpty() || !isGoodShareXor(shareboxcombine.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Shares are not valid!!!!!");
                alert.showAndWait();
            } else {
                int numshares = Integer.parseInt(shareboxnumcombine.getText());
                String shares = shareboxcombine.getText();

                String s = XORSplit.combine(numshares, shares);
                outputboxcombine.setText(s);
                outputboxcombine.setEditable(false);

            }
        }
    }




    ///////////////////////////////////////////////////////////////////////
    /////////////////////////UTILS//////////////////////////////////////////
    @FXML
    public void clearInput(ActionEvent e) {

        if (e.getSource() == clearbuttonsplit) {
            secretbox.setText("");
            sharebox.setText("");
            threshbox.setText("");
        }

        if (e.getSource() == clearbuttoncombine) {
            shareboxcombine.setText("");
            keyboxcombine.setText("");
            shareboxnumcombine.setText("");
        }
        if (e.getSource() == clearbuttonsplitxor) {
            secretbox.setText("");
            sharebox.setText("");
        }

        if (e.getSource() == clearbuttoncombinexor) {
            shareboxcombine.setText("");
            shareboxnumcombine.setText("");
        }
    }

    @FXML
    public void quit(ActionEvent e) {
        exit();
    }


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

    private boolean isInt(String text)
    {
        return text.matches("[0-9]*");
    }

    private boolean isGoodShare(String text)
    {
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

    private boolean isGoodShareXor(String text)
    {
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
