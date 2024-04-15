package com.poc.exceltojavapojolist.adapter.income.restapi.mapper;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import com.poiji.bind.Poiji;
import com.poiji.exception.HeaderMissingException;
import com.poiji.option.PoijiOptions.PoijiOptionsBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final Map<String, String> mappingErrors = new LinkedHashMap<>();

  public List<LeadDto> excelFileToList(InputStream file) throws IOException {
    Workbook workbook = new XSSFWorkbook(file);
    return workBookToList(workbook);
  }

  public List<LeadDto> workBookToList(Workbook workbook) {
    Sheet sheet = workbook.getSheetAt(0);

    List<LeadDto> leadDtoList = getLeadDtoList(sheet);
    validateResults(leadDtoList);
    return leadDtoList;
  }

  private List<LeadDto> getLeadDtoList(Sheet sheet) {
    try {
      return Poiji.fromExcel(sheet, LeadDto.class);
    } catch (HeaderMissingException ex){
      throw new CustomApplicationException(
          "Missing expected columns.",
          "Following columns are missing %s".formatted(ex.getMissingExcelCellNameHeaders()),
          HttpStatus.EXPECTATION_FAILED.value(), mappingErrors);
    }
  }

  private void validateResults(List<LeadDto> leadDtoList) {
    mappingErrors.clear();

    leadDtoList.forEach(this::validateLead);

    if (mappingErrors.size() > 0) {
      throw new CustomApplicationException(
          "Errors in leads data.", "The leads data in the file has errors, please fix them and retry.",
          HttpStatus.EXPECTATION_FAILED.value(), mappingErrors);
    }
  }

  private void validateLead(LeadDto leadDto) {
    Set<ConstraintViolation<LeadDto>> validate = validator.validate(leadDto);
    validate.forEach(e -> mappingErrors.put(
        "Error for lead in row: %d".formatted(leadDto.getRowIndex() + 1),
        "'%s' '%s'".formatted(e.getPropertyPath(), e.getMessage())));
  }
}
