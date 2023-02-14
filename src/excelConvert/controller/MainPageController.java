package excelConvert.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private Button fileUpload1;
    @FXML
    private Label fileName;

    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileUpload1.setOnAction(this::uploadFile);
        convert.setOnAction(this::convert);

    }

    // 아래 두 메서드는 드래그 파일해서 엑셀가져오기 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @FXML
    private void handleDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles() && isExcelFile(db.getFiles().get(0))) {
            System.out.println("드래그 시도");
            event.acceptTransferModes(TransferMode.COPY);
            System.out.println("드래그 성공");
        } else {
            event.consume();
        }
    }
    @FXML
    private void handleDragDropped(DragEvent event) {
        System.out.println("드롭다운 시도");
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles() && isExcelFile(db.getFiles().get(0))) {
            file = db.getFiles().get(0);
            fileName.setText(file.getName());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
        System.out.println("드롭다운 성공");
    }



    // 버튼 파일 업로드 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @FXML
    public void uploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            fileName.setText(file.getName());
            System.out.println("파일업로드 성공");
        } else {
            System.out.println("파일업로드 실패");
        }
    }

    // 파일 체크     ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private boolean isExcelFile(File file) {
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0 && lastIndex < fileName.length() - 1) {
            String fileExtension = fileName.substring(lastIndex + 1).toLowerCase();
            if (fileExtension.equals("xls") || fileExtension.equals("xlsx")) {
                System.out.println("엑셀파일 체크 성공");
                return true;
            }
        }
        return false;
    }

    // 토글 선택 관련 컨트롤   ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @FXML private ToggleGroup upDown;
    @FXML private Button convert;
    @FXML private TextField amount;

    @FXML
    public void convert(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) upDown.getSelectedToggle();
        if(!(fileName.getText().equals("엑셀파일 이름이 나타납니다"))) {
            if(selectedRadioButton != null) {
                if(amount.getText() != null) {
                    try {
                        String amountStr = amount.getText();
                        int amount = Integer.parseInt(amountStr);
                        String selectedText = selectedRadioButton.getText();
                        String check;
                        if(selectedText.equals("이상")) {
                            check = "up";
                        } else {
                            check = "down";
                        }
                        converting(amount, check);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "금액을 입력해주세요!", ButtonType.OK);
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("변환 실패");
                    alert.setContentText("금액을 설정해주세요!");
                    alert.showAndWait();
                }

            }  else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("변환 실패");
                alert.setContentText("체크박스 설정해주세요!");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("변환 실패");
            alert.setContentText("엑셀 파일을 업로드해주세요!");
            alert.showAndWait();
        }
    }


    //  엑셀 파일 변환작업
    public void converting(int amount ,String check) {

        switch (check) {
            case "up":
                try {
                    FileInputStream excelFile = new FileInputStream(file);
                    XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    int rowNo = 0;
                    int cellIndex = 0;


                    readExcel(sheet);


                    excelFile.close();
                    System.out.println("파일오픈 및 인풋스트림 닫기 성공");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("체크 컨버팅 up");
                System.out.println(amount);
                break;





            case "down":
                System.out.println("체크 컨버팅 down");
                System.out.println(amount);
                break;
        }
    }

    // 엑셀 읽기 메서드
    public void readExcel(Sheet sheet) throws IOException {
        for (Row row : sheet) {
            // 각각의 행에 존재하는 모든 열(cell)을 순회한다.
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                // cell의 타입을 하고, 값을 가져온다.
                switch (cell.getCellType()) {
                    case NUMERIC ->
                        //getNumericCellValue 메서드는 기본으로 double형 반환
                            System.out.print((int) cell.getNumericCellValue() + "\t");
                    case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                }
            }
            System.out.println("");
        }
    }
}