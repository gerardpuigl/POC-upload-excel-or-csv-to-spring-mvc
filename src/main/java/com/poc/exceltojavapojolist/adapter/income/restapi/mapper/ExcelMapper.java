package com.poc.exceltojavapojolist.adapter.income.restapi.mapper;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto.LeadDtoBuilder;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExcelMapper {

  private final List<String> titleStructure = List.of("name","email");

  public List<LeadDto> excelFileToList(InputStream file) throws IOException {
      Workbook workbook = new XSSFWorkbook(file);
      Sheet sheet = workbook.getSheetAt(0);
      Iterator<Row> rowIterator = sheet.rowIterator();

      checkTitleRow(rowIterator.next());

      List<LeadDto> leadRequestList = new ArrayList<>();

      rowIterator.forEachRemaining(row -> leadRequestList.add(mapRowToLeadDto(row)));

      return leadRequestList;
  }

  private LeadDto mapRowToLeadDto(Row row) {
    LeadDtoBuilder leadRequest = LeadDto.builder();
    leadRequest.name(row.getCell(0).getStringCellValue());
    leadRequest.email(row.getCell(1).getStringCellValue());
    return leadRequest.build();
  }

  private void checkTitleRow(Row row) {
    Map<String,String> titleErrors = new HashMap<>();

    row.forEach(cell -> {
      String expectedColumnTitle = titleStructure.get(cell.getColumnIndex());
      if(!cell.getStringCellValue().equals(expectedColumnTitle)){
        titleErrors.put(
            "Error in row: %s column: '%d'".formatted(cell.getRowIndex(), cell.getColumnIndex()),
            "Expected: '%s' but it was: '%s' in the title row".formatted(expectedColumnTitle,
                cell.getStringCellValue()));
      }});

    if(titleErrors.size() >0){
      throw new CustomApplicationException(
          "Errors in the file title row.", "The title row doesn't have the values expected",
          HttpStatus.EXPECTATION_FAILED.value(),titleErrors);
    }
  }
}
