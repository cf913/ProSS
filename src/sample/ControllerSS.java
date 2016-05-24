package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.application.Platform.exit;

public class ControllerSS {

    @FXML private Button sss;
    @FXML private Button xor;
    @FXML private Button splitbutton;
    @FXML private Button splitbuttonxor;
    @FXML private Button skip2;
    @FXML private Button savebutton;
    @FXML private Button exitbutton;
    @FXML private Button combinebutton;
    @FXML private Button combinebuttonxor;
    @FXML private Button clearbuttonsplit;
    @FXML private Button clearbuttoncombine;
    @FXML private Button clearbuttonsplitxor;
    @FXML private Button clearbuttoncombinexor;

    @FXML private TextArea secretbox;
    @FXML private TextArea outputbox;

    @FXML private TextField sharebox;
    @FXML private TextField threshbox;

    @FXML private TextField shareboxnumcombine;
    @FXML private TextArea shareboxcombine;
    @FXML private TextField keyboxcombine;
    @FXML private TextArea outputboxcombine;


    @FXML private CheckBox tik1;


    @FXML
    public void goTo(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
        if(event.getSource()==sss){
            //get reference to the button's stage
            stage=(Stage) sss.getScene().getWindow();
            //load up OTHER FXML document
            root = FXMLLoader.load(getClass().getResource("sss.fxml"));
        } else if(event.getSource()==xor){
            //get reference to the button's stage
            stage=(Stage) xor.getScene().getWindow();
            //load up OTHER FXML document
            root = FXMLLoader.load(getClass().getResource("xor.fxml"));
        } else {
            stage=(Stage) skip2.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        }
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
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

            if (e.getSource()==splitbutton) {

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
                //System.out.println("Secret: " + secret);
                int shares = Integer.parseInt(sharebox.getText());
                //System.out.println("Shares: " + shares);
                int thresh = Integer.parseInt(threshbox.getText());
                //System.out.println("Threshold: " + thresh);

                String[] s = SSSSplit.split(secret, shares, thresh);
                secretbox.setText("");
                outputbox.setText("Shares:\n" + s[0] + "\nThreshold:\n" + s[2] + "\n\nKey:\n" + s[1]);
                outputbox.setEditable(false);

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
    public void saveFile(ActionEvent e) {

            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);


        //TODO: implement save multiple files
        //if (!tik1.isSelected()) {

            fileChooser.setInitialFileName("Shares.txt");
            File file = fileChooser.showSaveDialog(Main.stage);

            if (file != null) {
                SaveFile(outputbox.getText(), file);
            }
        //}

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

        } else if (e.getSource() == combinebutton) {
            if (shareboxcombine.getText().isEmpty() || !isGoodShare(shareboxcombine.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Shares are not valid");
                alert.showAndWait();
            } else if (keyboxcombine.getText().isEmpty() || !isInt(keyboxcombine.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Key not valid");
                alert.showAndWait();
            } else {
                int numshares = Integer.parseInt(shareboxnumcombine.getText());
                String shares = shareboxcombine.getText();
                BigInteger key = new BigInteger(keyboxcombine.getText());

                String s = SSSReconstruct.reconstruct(numshares, shares, key);
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

        String thresh = list[share+3];
        String key = list[share+6];
        for (int i = 0; i < Integer.parseInt(thresh); i++ ){
            shares = shares.concat(list[i+1] + "\n");
        }

        shareboxnumcombine.setText(thresh);
        shareboxcombine.setText(shares);
        keyboxcombine.setText(key);


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
