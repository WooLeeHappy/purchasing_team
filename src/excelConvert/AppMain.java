package excelConvert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AppMain extends Application {
    private Stage primaryStage;
    public void changeScene(Scene scene) throws Exception {
        System.out.println("여기까지옴");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));  // 현재클래스 위치에서 root.fxml 파일 로드(같은위치)

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmlFile/mainpage.fxml"));
        Parent root = loader.load();            // 위 방식과 동일하나 다만 이벤트처리할때 컨트롤러 객체 사용할 때 반드시 loader객체가 필요하다

        Scene scene = new Scene(root);
        primaryStage.setTitle("StJohnsApp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
