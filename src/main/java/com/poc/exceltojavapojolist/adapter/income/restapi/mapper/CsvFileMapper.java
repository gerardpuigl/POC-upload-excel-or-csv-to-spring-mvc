package com.poc.exceltojavapojolist.adapter.income.restapi.mapper;

import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvFileMapper {

  public XSSFWorkbook csvFileToExcelWorkbook(InputStream file) {
    XSSFWorkbook workBook = new XSSFWorkbook();
    XSSFSheet sheet = workBook.createSheet("sheet1");

    try {
      String row;
      int RowNum = 0;
      BufferedReader br = new BufferedReader(new InputStreamReader(file));

      while ((row = br.readLine()) != null) {
        String[] str = row.split(",");
        XSSFRow currentRow = sheet.createRow(RowNum);
        for (int i = 0; i < str.length; i++) {
          currentRow.createCell(i).setCellValue(str[i]);
        }
        RowNum++;
      }
    } catch (Exception ex) {
      throw new CustomApplicationException(
          "Error reading csv file.", ex.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(), ex);
    }
    return workBook;
  }
}
