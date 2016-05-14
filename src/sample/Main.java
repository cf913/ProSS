package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        primaryStage.setTitle("ProceSS");
        primaryStage.setScene(new Scene(root, 610, 360));
        primaryStage.show();
    }

    public Stage getStage() {
        return this.stage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
