package Listeners;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ReadExcelFile {
    public static HashMap<String, String> fetchData() throws IOException {
        String file_path = System.getProperty("user.dir") + "\\src\\main\\resources\\ExcelData.xlsx";
        System.out.println(file_path);
        FileInputStream fis = new FileInputStream(file_path);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("Sheet1");
        HashMap<String,String> sheetData = new HashMap<>();
        int max_row = sheet.getLastRowNum();
        for(int i=1;i<=max_row;i++)
        {
            String test_data_key = sheet.getRow(i).getCell(0).getStringCellValue();
            Cell cell = sheet.getRow(i).getCell(1);
            String test_data_value = "";
            if(cell.getCellType() ==Cell.CELL_TYPE_STRING ){
                 test_data_value = sheet.getRow(i).getCell(1).getStringCellValue();
            } else if (cell.getCellType() ==Cell.CELL_TYPE_NUMERIC) {
                int test_data = (int)sheet.getRow(i).getCell(1).getNumericCellValue();
                test_data_value = String.valueOf(test_data);
            }
            else if (cell.getCellType() ==Cell.CELL_TYPE_BOOLEAN) {
                boolean test_type_data = sheet.getRow(i).getCell(1).getBooleanCellValue();
                test_data_value = String.valueOf(test_type_data);
            }
            else {
                test_data_value = String.valueOf(sheet.getRow(i).getCell(1));
            }
            sheetData.put(test_data_key,test_data_value);
        }
        System.out.println(sheetData);
        wb.close();
        return sheetData;
    }
}