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

    public void setFile(File file) {
        this.file = file;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
