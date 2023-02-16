package excelConvert.controller;

import excelConvert.AppMain;
import excelConvert.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainPageController implements Initializable {
    AppMain appMain = new AppMain();
    MainpageFirstFunctionService mainpageFirstFunctionService = new MainpageFirstFunctionService();
    MainpageUploadService mainpageUploadService = new MainpageUploadService();
    ReadExcelService readExcelService = new ReadExcelService();
    DownloadExcel downloadExcel = new DownloadExcel();
    AlertService alertService = new AlertService();

    @FXML
    private Button fileUpload1;
    @FXML
    private Label fileName;

    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileUpload1.setOnAction(this::uploadFile);
        convert.setOnAction(this::checkOption);
        download.setOnAction(this::download);
    }

    private void download(ActionEvent event) {
        if(mainpageFirstFunctionService.getWorkbook() != null) {
            downloadExcel.downloadExcel(mainpageFirstFunctionService.getWorkbook(), appMain.getPrimaryStage());
        } else {
            AlertService.showAlert("파일 업로드 실패", "선택한 파일이 존재하지 않습니다.");
        }
    }

    // 아래 두 개는 드래그 파일해서 엑셀가져오기 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @FXML
    private void handleDragOver(DragEvent event) {
        mainpageUploadService.handleDragOver(event);
    }
    @FXML
    private void handleDragDropped(DragEvent event) {
        file = mainpageUploadService.handleDragDropped(event, fileName);
    }

    // 버튼 파일 업로드 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @FXML
    public void uploadFile(ActionEvent event) {
        file = mainpageUploadService.uploadFile(event, fileName);
    }

    // 토글 선택 관련 컨트롤   ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @FXML private ToggleGroup upDown;
    @FXML private Button convert;
    @FXML private TextField amount;
    @FXML private Button download;

    @FXML
    public void checkOption(ActionEvent event) {
        mainpageFirstFunctionService.checkOption(file, upDown, amount, fileName);
        System.out.println("여기까진 보냈음");
    }
    // 엑셀 읽기 메서드
    public void readExcel(Sheet sheet) throws IOException {
        readExcelService.readExcel(sheet);
    }
    // 엑셀 다운로드
}