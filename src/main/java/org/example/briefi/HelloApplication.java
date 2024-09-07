package org.example.briefi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;


public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Image icon = new
                Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/logo.jpg")));

        FXMLLoader fxmlLoader = new
                FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SellApp");
        stage.setScene(scene);
        stage.getIcons().add(icon);
        //stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }


}