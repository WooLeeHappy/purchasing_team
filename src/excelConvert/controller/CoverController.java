package excelConvert.controller;

import excelConvert.AppMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class CoverController {
    @FXML
    private Button exit;

    @FXML
    private Button start;

    public void initialize() {
        exit.setOnAction(Event -> {
                Platform.exit();
        });
        start.setOnAction(Event -> {
            try {
                Parent main = FXMLLoader.load(getClass().getClassLoader().getResource("mainpage.fxml"));

                Scene scene = new Scene(main);
                Stage secondScene = (Stage) start.getScene().getWindow();
                secondScene.setScene(scene);
                secondScene.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


}
