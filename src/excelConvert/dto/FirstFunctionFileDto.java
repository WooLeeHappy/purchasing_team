package excelConvert.dto;


import java.io.File;

public class FirstFunctionFileDto {

    private File file;
    private String check;
    private int amount;

    public FirstFunctionFileDto(File file, String check, int amount) {
        this.file = file;
        this.check = check;
        this.amount = amount;
    }

    public File getFile() {
        return file;
    }
    public String getCheck() {
        return check;
    }
    public int getAmount() {
        return amount;
    }
}