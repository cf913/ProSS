package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /***********************************************
     makes stage assignable.
     Used when opening FileChooser Dialogs
     ***********************************************/
    public static Stage stage;


    /***********************************************
     start(Stage primaryStage) is the launch function.
     It opens the application on the MainMenu page.
     ***********************************************/
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        primaryStage.setTitle("ProSS");
        primaryStage.setScene(new Scene(root, 600, 413));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
