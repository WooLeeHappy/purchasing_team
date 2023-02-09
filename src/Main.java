import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
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
        Scene scene = new Scene(layout, 300, 300);
        Button button = new Button();
        button.setText("엑셀파일 가져오기");
        button.setOnAction(new EventHandler<ActionEvent>() {
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
        layout.getChildren().add(button);
        stage.setScene(scene);
        stage.setTitle("상플");
        stage.show();
    }
    public void test() {
        System.out.println("잘 됬다");
    }
}