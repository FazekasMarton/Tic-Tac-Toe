package org.example.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Game controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Tik Tak Toe");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        this.controller = fxmlLoader.getController();
    }

    @Override
    public void stop() {
        controller.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}