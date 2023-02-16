package excelConvert.service;

import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class DownloadExcel {
    public static void downloadExcel(XSSFWorkbook workbook, Stage stage) {
        ReadExcelService readExcelService = new ReadExcelService();
        // 파일 선택 대화상자 생성
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        // 사용자가 파일을 선택한 경우
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            // 파일이 이미 존재하는 경우, 덮어쓸 것인지 확인
            System.out.println("덮씁확");
            if (selectedFile.exists()) {
                Optional<ButtonType> result = AlertService.showConfirmationAlert("Confirmation",
                        "The file already exists. Do you want to replace it?");
                if (result.get() != ButtonType.OK) {
                    return; // 저장 취소
                }
            }
            System.out.println("트라이직전");
            readExcelService.readExcel(workbook.getSheetAt(0));
            String filePath = "." + File.separator + "output.xlsx";


            // 파일 저장
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(filePath);
                workbook.write(outputStream);
                System.out.println("됐음됐다해!!");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        System.out.println("여긴 지나나?");
                        outputStream.close();
                        System.out.println("닫히기도 하구?");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
