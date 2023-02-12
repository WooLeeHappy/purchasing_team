import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        StackPane layout = new StackPane();
        VBox vBox = new VBox();
        Scene page1 = new Scene(layout, 640, 480);
        Scene page2 = new Scene(vBox, 400, 200);
        Label label = new Label("먼저 변형할 엑셀 파일을 넣어주세요");
        Button getExcel = new Button("파일 가져오기");
        // 엑셀 파일 가져오는 부분
        vBox.getChildren().addAll(getExcel, label);
        getExcel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Excel File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    try {
                        FileInputStream fis = new FileInputStream(selectedFile);
                        XSSFWorkbook workbook = new XSSFWorkbook(fis);
                        XSSFSheet sheet = workbook.getSheetAt(0);

                        // loop through the rows
                        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                            XSSFRow row = sheet.getRow(rowNum);

                            // loop through the columns
                            for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
                                XSSFCell cell = row.getCell(colNum);
                                System.out.println(cell.toString());
                            }
                        }

                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //  엑셀 파일의 숫자 받아오는 부분
        TextField input = new TextField();
        Button submitButton = new Button();
        submitButton.setText("금액지정");
        layout.getChildren().add(submitButton);
        submitButton.setOnAction(event -> getLowMoney(input.getText()));

        //
        stage.setScene(page1);
        stage.setScene(page2);
        stage.setTitle("상플");
        stage.show();
    }

    private String getLowMoney(String text) {
        System.out.println(text);
        return text;
    }

    public void test() {
        System.out.println("잘 됬다");
    }
}