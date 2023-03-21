package org.utl.dsm.optica.dsm404optica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(root);


        primaryStage.setScene(scene);
        primaryStage.setTitle("Optica Qualite");
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
