package excelConvert.service;

import excelConvert.dto.FirstFunctionFileDto;
import javafx.scene.control.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainpageFirstFunctionService {
    ReadExcelService readExcelService = new ReadExcelService();
    AlertService alertService = new AlertService();
    public void checkOption(File file, ToggleGroup upDown, TextField amountDefalut, Label fileName) {
        String fileNameText = fileName.getText();
        if ("엑셀파일 이름이 나타납니다".equals(fileNameText)) {
            alertService.showAlert("변환 실패", "엑셀 파일을 업로드해주세요!");
            return;
        }
        RadioButton selectedRadioButton = (RadioButton) upDown.getSelectedToggle();
        if (selectedRadioButton == null) {
            alertService.showAlert("변환 실패", "체크박스를 선택해주세요!");
            return;
        }
        String amountDefaultText = amountDefalut.getText();
        if (amountDefaultText == null || amountDefaultText.isEmpty()) {
            alertService.showAlert("변환 실패", "금액을 입력해주세요!");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountDefaultText);
        } catch (NumberFormatException e) {
            alertService.showAlert("변환 실패", "올바른 금액을 입력해주세요!");
            return;
        }

        String selectedText = selectedRadioButton.getText();
        String check = "Defalut";
        if (selectedText.equals("Asc")) {
            check = "up";
        } else if (selectedText.equals("Desc")) {
            check = "down";
        }
        System.out.println("여기까진옴?");

        converting(new FirstFunctionFileDto(file, check, amount));
    }


    public void converting(FirstFunctionFileDto fileDto) {
        File file = fileDto.getFile();
        String check = fileDto.getCheck();
        int amount = fileDto.getAmount();
//        File fileDe = deDuplication(file);



        switch (check) {
            case "up":
                // 엑셀 파일 열기
                try {
                    FileInputStream excelFile = new FileInputStream(file);
                    XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    int rows = sheet.getLastRowNum();
                    int cols = sheet.getRow(1).getLastCellNum();




                    readExcelService.readExcel(sheet);
                    excelFile.close();
                    System.out.println("파일오픈 및 인풋스트림 닫기 성공");
                    System.out.println("체크 컨버팅 up");
                    System.out.println(amount);


                } catch (FileNotFoundException e) {
                    // 파일이 존재하지 않는 경우
                    alertService.showAlert("파일 업로드 실패", "선택한 파일이 존재하지 않습니다.");
                } catch (IOException e) {
                    // 파일 읽기 중에 오류가 발생한 경우
                    alertService.showAlert("파일 업로드 실패", "선택한 파일을 읽지 못했습니다");
                }
                break;
            case "down":
                System.out.println("여긴되고");
                System.out.println(file.getName());
                System.out.println("체크 컨버팅 down");
                System.out.println(amount);
                break;

        }
    }

    private File deDuplication(File file) {

    }

}
