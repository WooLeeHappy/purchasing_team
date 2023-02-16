package excelConvert.service;

import excelConvert.dto.FirstFunctionFileDto;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

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
    private XSSFWorkbook convertWorkbook = null;
    public XSSFWorkbook getWorkbook() {
        return convertWorkbook;
    }

    private void deDuplication(File file) {
        convertWorkbook = new XSSFWorkbook();
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
            // 아예 빈 행을 shift
            int lastRowNum = sheet.getLastRowNum();
            for (int i = lastRowNum; i >= 0; i--) {
                if (sheet.getRow(i) == null || sheet.getRow(i).getPhysicalNumberOfCells() == 0) {
                    sheet.shiftRows(i + 1, lastRowNum, -1);
                }
            }


            File convertFile = new File("converted_temp.xlsx");
            convertWorkbook = new XSSFWorkbook(); // 새 엑셀 생성
            FileOutputStream fileoutputstream = new FileOutputStream(convertFile); // 추출
            XSSFSheet convertSheet = convertWorkbook.createSheet("convertSheet");

            int sourceRowCount = sheet.getLastRowNum();
            XSSFDataFormat fmt = convertWorkbook.createDataFormat();

            XSSFCellStyle borderStyle = convertWorkbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            for (Row row : convertSheet) {
                for (Cell cell : row) {
                    cell.setCellStyle(borderStyle);
                }
            }

            for (int i = 0; i <= sourceRowCount; i++) {
                XSSFRow sourceRow = sheet.getRow(i);
                if (sourceRow != null) {
                    XSSFRow newRow = convertSheet.createRow(i);

                    int sourceCellCount = sourceRow.getLastCellNum();
                    for (int j = 0; j < sourceCellCount; j++) {
                        XSSFCell sourceCell = sourceRow.getCell(j);
                        if (sourceCell != null) {
                            XSSFCell newCell = newRow.createCell(j);
                            XSSFCellStyle style = sourceCell.getCellStyle();
                            XSSFCellStyle newStyle = convertWorkbook.createCellStyle();
                            newStyle.cloneStyleFrom(style);
                            double value;

                            switch (sourceCell.getCellType()) {
                                case NUMERIC:
                                    value = sourceCell.getNumericCellValue();
                                    newStyle.cloneStyleFrom(sourceCell.getCellStyle());
                                    if (j == 6) { // G열일 때
                                        newStyle.setDataFormat(fmt.getFormat("#,##0.00"));
                                    } else { // G열 이외의 열일 때
                                        newStyle.setDataFormat(fmt.getFormat("#,##0"));
                                    }
                                    newCell.setCellValue(value);
                                    newCell.setCellStyle(newStyle);

                                    break;
                                case BOOLEAN:
                                    newCell.setCellValue(sourceCell.getBooleanCellValue());
                                    break;
                                case FORMULA:
                                    newCell.setCellFormula(sourceCell.getCellFormula());
                                    break;
                                default:
                                    newCell.setCellValue(sourceCell.getStringCellValue());
                                    break;
                            }

                            newCell.setCellStyle(newStyle);
                            convertSheet.setColumnWidth(j, sheet.getColumnWidth(j));
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
            alertService.showInformationAlert("파일 변환 성공", "파일을 다운로드 할 수 있습니다.");

        } catch (FileNotFoundException e) {
            // 파일이 존재하지 않는 경우
            alertService.showAlert("파일 업로드 실패", "선택한 파일이 존재하지 않습니다.");
        } catch (IOException e) {
            // 파일 읽기 중에 오류가 발생한 경우
            alertService.showAlert("파일 업로드 실패", "선택한 파일을 읽지 못했습니다");
        }
    }

}
