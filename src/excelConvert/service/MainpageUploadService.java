package excelConvert.service;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;

public class MainpageUploadService {
    private File file;


    // 드래그
    public void handleDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles() && isExcelFile(db.getFiles().get(0))) {
            System.out.println("드래그 시도");
            event.acceptTransferModes(TransferMode.COPY);
            System.out.println("드래그 성공");
        } else {
            event.consume();
        }
    }
    // 드롭다운 업로드
    public File handleDragDropped(DragEvent event, Label fileName) {
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
        return file;
    }

    // 버튼 파일 업로드
    public File uploadFile(ActionEvent event, Label fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            fileName.setText(file.getName());
            System.out.println("파일업로드 성공");
            return file;
        } else {
            System.out.println("업로드 실패");
            throw new IllegalArgumentException();
        }
    }

    // 엑셀 파일인지 확인하는 메서드
    public boolean isExcelFile(File file) {
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
}
