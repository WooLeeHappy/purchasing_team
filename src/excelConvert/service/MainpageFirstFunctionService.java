package excelConvert.service;

import excelConvert.dto.FirstFunctionFileDto;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

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
        deDuplication(file);



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
    private XSSFWorkbook convertWorkbook = new XSSFWorkbook();
    public XSSFWorkbook getWorkbook() {
        return convertWorkbook;
    }

    private void deDuplication(File file) {
        System.out.println("여긴?");
        try {
            System.out.println("try시작");
            FileInputStream excelFile = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getLastRowNum();
            // 중복되는값을 제거 첫번째
            for(int i = 2; i <= rows - 1; i++) {
                XSSFCell beforeCell = sheet.getRow(i-1).getCell(1);
                XSSFCell nowCell = sheet.getRow(i).getCell(1);
                if(beforeCell.getStringCellValue().equals(nowCell.getStringCellValue())) {
                    beforeCell.setCellValue("");
                }
            }

            // 소계값을 행에 올려주는 작업  G I K L 열의 값 index 6, 8, 10, 11
            for(int i = 2; i <= rows -1; i++) {
                XSSFCell nowCell = sheet.getRow(i).getCell(1);
                XSSFCell beforeCell_G = sheet.getRow(i-1).getCell(6);
                XSSFCell nowCell_G = sheet.getRow(i).getCell(6);
                XSSFCell beforeCell_I = sheet.getRow(i-1).getCell(8);
                XSSFCell nowCell_I = sheet.getRow(i).getCell(8);
                XSSFCell beforeCell_K = sheet.getRow(i-1).getCell(10);
                XSSFCell nowCell_K = sheet.getRow(i).getCell(10);
                XSSFCell beforeCell_L = sheet.getRow(i-1).getCell(11);
                XSSFCell nowCell_L = sheet.getRow(i).getCell(11);
                if(nowCell.getStringCellValue().equals("소계")) {
                    beforeCell_G.setCellValue((int) nowCell_G.getNumericCellValue());
                    beforeCell_I.setCellValue((int) nowCell_I.getNumericCellValue());
                    beforeCell_K.setCellValue((int) nowCell_K.getNumericCellValue());
                    beforeCell_L.setCellValue((int) nowCell_L.getNumericCellValue());
                }
            }

            // 마지막으로 소계와 빈값을 삭제하는 코드
            for(int i = 1; i <= rows -1; i++) {
                Row row = sheet.getRow(i);
                XSSFCell cell = sheet.getRow(i).getCell(1);

                if(cell.getStringCellValue().equals("") || cell.getStringCellValue().equals("소계")) {
                    if(row != null) {
                        sheet.removeRow(row);
                    }
                }
            }
            File convertFile = new File("converted_file.xlsx");
            convertWorkbook = new XSSFWorkbook(); // 새 엑셀 생성
            FileOutputStream fileoutputstream = new FileOutputStream(convertFile); // 추출
            XSSFSheet convertSheet = convertWorkbook.createSheet("convertSheet");

            // 원본 sheet의 데이터를 새로운 sheet에 복사한다
            int sourceRowCount = sheet.getLastRowNum();
            for (int i = 0; i <= sourceRowCount; i++) {
                XSSFRow sourceRow = sheet.getRow(i);
                if (sourceRow != null) {
                    // 복사할 row를 생성한다
                    XSSFRow newRow = convertSheet.createRow(i);

                    // 원본 row의 데이터를 복사하여 새로운 row에 넣는다
                    int sourceCellCount = sourceRow.getLastCellNum();
                    for (int j = 0; j < sourceCellCount; j++) {
                        XSSFCell sourceCell = sourceRow.getCell(j);
                        if (sourceCell != null) {
                            // 복사할 cell을 생성한다
                            XSSFCell newCell = newRow.createCell(j);

                            // 원본 cell의 데이터를 복사하여 새로운 cell에 넣는다
                            switch (sourceCell.getCellType()) {
                                case NUMERIC:
                                    newCell.setCellValue(sourceCell.getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    newCell.setCellValue(sourceCell.getBooleanCellValue());
                                    break;
                                case FORMULA:
                                    newCell.setCellFormula(sourceCell.getCellFormula());
                                    break;
                                // 다른 cell type에 대한 처리도 필요하다면 추가 구현이 필요
                                default:
                                    newCell.setCellValue(sourceCell.getStringCellValue());
                                    break;
                            }
                        }
                    }
                }
            }
            convertWorkbook.write(fileoutputstream);
            System.out.println("엑셀파일생성성공");

            workbook.close();
            excelFile.close();
            fileoutputstream.close();
            readExcelService.readExcel(convertWorkbook.getSheetAt(0));


            System.out.println("파일오픈 및 인풋스트림 닫기 성공");
            System.out.println("체크 컨버팅 up");

        } catch (FileNotFoundException e) {
            // 파일이 존재하지 않는 경우
            alertService.showAlert("파일 업로드 실패", "선택한 파일이 존재하지 않습니다.");
        } catch (IOException e) {
            // 파일 읽기 중에 오류가 발생한 경우
            alertService.showAlert("파일 업로드 실패", "선택한 파일을 읽지 못했습니다");
        }
    }

}
