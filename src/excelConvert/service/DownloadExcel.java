package excelConvert.service;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DownloadExcel {
    private String downloadExcel(File file, Stage primaryStage) {
// FileChooser 객체 생성
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("변환한 파일을 저장합니다");

        // 파일 저장 위치를 지정합니다. 이 코드에서는 현재 디렉토리를 사용합니다.
        File currentDir = new File(".");
        fileChooser.setInitialDirectory(currentDir);

        // 파일 필터를 지정합니다. 이 코드에서는 엑셀 파일(xlsx)만 선택 가능합니다.
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // 사용자에게 파일 선택 대화상자를 표시합니다.
        File selectedFile = fileChooser.showSaveDialog(primaryStage);

        if (selectedFile != null) {
            // 선택한 파일이 존재하는 경우, 파일 경로를 반환합니다.
            String filePath = selectedFile.getPath();
            System.out.println("File path: " + filePath);

            // 수정된 파일을 선택한 경로에 저장하거나 반환하는 로직을 작성합니다.

            // 저장된 파일 경로를 반환합니다.
            return file.getPath();
        } else {
            System.out.println("File selection canceled.");
            // 사용자가 파일 선택 대화상자를 취소한 경우, null 값을 반환합니다.
            return null;
        }
    }
}
