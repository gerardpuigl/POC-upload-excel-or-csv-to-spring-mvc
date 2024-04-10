package com.poc.exceltojavapojolist.adapter.income.mapper;

import com.poc.exceltojavapojolist.application.income.CreateLeadInPort.CreateLeadRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExcelMapper {

  public List<CreateLeadRequest> excelFileToList(InputStream file) {
    try {
      Workbook workbook = new XSSFWorkbook(file);
      Sheet sheet = workbook.getSheetAt(0);

      List<CreateLeadRequest> leadRequestList = new ArrayList<>();
      Iterator<Row> rowIterator = sheet.rowIterator();

      checkTitleRow(rowIterator.next());

      rowIterator.forEachRemaining(
          row ->{
            CreateLeadRequest leadRequest = new CreateLeadRequest();
            leadRequest.setName(row.getCell(0).getStringCellValue());
            leadRequest.setEmail(row.getCell(1).getStringCellValue());
            leadRequestList.add(leadRequest);
          }
      );

      return leadRequestList;
    } catch (Exception e) {
      log.error("Error converting file to leads", e);
      throw new RuntimeException("Error converting file to leads", e);
    }
  }

  private void checkTitleRow(Row row) {
    if(!row.getCell(0).getStringCellValue().equals("name") ||
        !row.getCell(1).getStringCellValue().equals("email")){
      throw new RuntimeException("Errors in the title row file");
    }
  }
}
