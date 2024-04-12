package com.poc.exceltojavapojolist.adapter.income.restapi.mapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
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
      int RowNum=0;
      BufferedReader br = new BufferedReader(new InputStreamReader(file));
      while ((row = br.readLine()) != null) {
        String[] str = row.split(",");
        RowNum++;
        XSSFRow currentRow=sheet.createRow(RowNum);
        for(int i=0;i<str.length;i++){
          currentRow.createCell(i).setCellValue(str[i]);
        }
      }
    } catch (Exception ex) {
      throw new CustomApplicationException(
          "Error reading csv file.", ex.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(), ex);
    }
    return workBook;
  }

  /**
   * Alternative way to create the list without excel library.
   *
   * With the csvFileToExcelWorkbook(InputStream file) method, we get XSSFWorkbook and then we can reuse all the logic
   * in theExcelMapper (validate data,error handling,etc)
   *
   * Provably all validations can be done here, but I need to support both file extensions and it will easy to
   * maintain in one single place.
   *
   */

  public List<LeadDto> fromCsvWithHeader(InputStream file) {
    try {
      CsvSchema schema = CsvSchema.emptySchema().withHeader();
      MappingIterator<LeadDto> genericIterator = new CsvMapper()
          .readerWithSchemaFor(LeadDto.class).with(schema).readValues(file);
      return genericIterator.readAll();
    } catch (Exception e) {
      log.error("Error converting file to leads", e);
      throw new CustomApplicationException(
          "Error converting file to leads",e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
    }
  }
}
