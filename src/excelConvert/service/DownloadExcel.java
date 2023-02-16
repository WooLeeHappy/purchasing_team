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
        fileChooser.setTitle("변환한 엑셀 저장하기");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        // 사용자가 파일을 선택한 경우
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            // 파일이 이미 존재하는 경우, 덮어쓸 것인지 확인
            if (selectedFile.exists()) {
                Optional<ButtonType> result = AlertService.showConfirmationAlert("확인",
                        "이미 파일이 존재합니다. 덮어씌울까요?");
                if (result.get() != ButtonType.OK) {
                    return; // 저장 취소
                }
            }

            // 파일 저장
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(selectedFile);
                workbook.write(outputStream);
                System.out.println("파일저장성공");
                File temp_file = new File("converted_temp.xlsx");
                if (temp_file.delete()) {
                    System.out.println("파일이 삭제되었습니다.");
                } else {
                    System.out.println("파일 삭제에 실패했습니다.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
