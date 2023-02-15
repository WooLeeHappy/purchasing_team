package excelConvert.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class ReadExcelService {
    public void readExcel(Sheet sheet) {
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
