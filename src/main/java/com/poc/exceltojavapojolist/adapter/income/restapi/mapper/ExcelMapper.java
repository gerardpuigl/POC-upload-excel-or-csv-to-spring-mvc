package com.poc.exceltojavapojolist.adapter.income.restapi.mapper;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto.LeadDtoBuilder;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExcelMapper {

  private final Validator validator;
  private final List<String> titleExpectedStructure = List.of("name", "email");
  private final Map<String, String> mappingErrors = new LinkedHashMap<>();

  public List<LeadDto> excelFileToList(InputStream file) throws IOException {
    Workbook workbook = new XSSFWorkbook(file);
    Sheet sheet = workbook.getSheetAt(0);
    Iterator<Row> rowIterator = sheet.rowIterator();

    mappingErrors.clear();
    checkTitleRow(rowIterator.next());

    return mapToLeadsDtoList(rowIterator);
  }

  private void checkTitleRow(Row row) {
    row.forEach(cell -> {
      String expectedColumnTitle = titleExpectedStructure.get(cell.getColumnIndex());
      if (!cell.getStringCellValue().equals(expectedColumnTitle)) {
        mappingErrors.put(
            "Error in title row: %d column: %d".formatted(cell.getRowIndex() + 1, cell.getColumnIndex() + 1),
            "Expected value: '%s' but it was: '%s'".formatted(expectedColumnTitle, cell.getStringCellValue()));
      }
    });

    if (mappingErrors.size() > 0) {
      throw new CustomApplicationException(
          "Errors in the file title row.", "The title row doesn't have the expected values.",
          HttpStatus.EXPECTATION_FAILED.value(), mappingErrors);
    }
  }

  private List<LeadDto> mapToLeadsDtoList(Iterator<Row> rowIterator) {
    List<LeadDto> leadRequestList = new ArrayList<>();

    rowIterator.forEachRemaining(row -> leadRequestList.add(mapRowToLeadDto(row)));

    if (mappingErrors.size() > 0) {
      throw new CustomApplicationException(
          "Errors in leads data.", "The leads data in the file has errors please fix them and retry.",
          HttpStatus.EXPECTATION_FAILED.value(), mappingErrors);
    }
    return leadRequestList;
  }

  private LeadDto mapRowToLeadDto(Row row) {
    LeadDtoBuilder leadDtoBuilder = LeadDto.builder();
    leadDtoBuilder.name(row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
    leadDtoBuilder.email(row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
    LeadDto leadDto = leadDtoBuilder.build();

    checkLeadInRow(leadDto, row.getRowNum() + 1);

    return leadDto;
  }

  private void checkLeadInRow(LeadDto leadDto, int rowNumber) {
    Set<ConstraintViolation<LeadDto>> validate = validator.validate(leadDto);
    validate.forEach(e -> mappingErrors.put(
        "Error for lead in row: %d".formatted(rowNumber),
        "'%s' '%s'".formatted(e.getPropertyPath(), e.getMessage())));
  }
}
