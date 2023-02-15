package excelConvert.service;

import excelConvert.dto.FirstFunctionFileDto;
import javafx.scene.control.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
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
        String check = "down";
        if ("이상".equals(selectedText)) {
            check = "up";
        }

        converting(new FirstFunctionFileDto(file, check, amount));
    }
    // 텍스트필드에 있는 숫자를 int값으로 변경해주는 작업
    private int strToInt(TextField amount) {
        String amountStr = amount.getText();
        return Integer.parseInt(amountStr);
    }

    public void converting(FirstFunctionFileDto fileDto) {
        File file = fileDto.getFile();
        String check = fileDto.getCheck();
        int amount = fileDto.getAmount();

        switch (check) {
            case "up":
                try {
                    FileInputStream excelFile = new FileInputStream(file);
                    XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    readExcelService.readExcel(sheet);


                    excelFile.close();
                    System.out.println("파일오픈 및 인풋스트림 닫기 성공");
                } catch (IOException e) {
                    e.printStackTrace();
                }



                System.out.println("체크 컨버팅 up");
                System.out.println(amount);
                break;

            case "down":
                System.out.println("여긴되고");
                System.out.println(file.getName());
                System.out.println("체크 컨버팅 down");
                System.out.println(amount);
                break;
        }
    }
}
