package excelConvert.controller;

import excelConvert.AppMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CoverController implements Initializable {
    @FXML private Button exit;
    @FXML private Button start;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exit.setOnAction(this::exitAction);
        start.setOnAction(event -> startAction(event));
    }

    public void exitAction(ActionEvent event) {
        Platform.exit();
    }
    public void startAction(ActionEvent event) {
//        Parent root = FXMLLoader.load(getClass().getResource("../fxmlFile/mainpage.fxml"));
//        Scene scene = new Scene(root);
    }
}
