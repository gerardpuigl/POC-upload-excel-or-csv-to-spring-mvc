package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures.AutomaticFileSelector;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures.LeadStructuresEnum;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import com.poiji.bind.Poiji;
import com.poiji.exception.HeaderMissingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  private final AutomaticFileSelector fileStructureSelector;


  public List<LeadFileDto> excelFileToList(InputStream file, Optional<String> structure) throws IOException {
    Workbook workbook = new XSSFWorkbook(file);
    return workBookToList(workbook,structure);
  }

  public List<LeadFileDto> workBookToList(Workbook workbook, Optional<String> structure) {
    Sheet sheet = workbook.getSheetAt(0);
    List<LeadFileDto> leadDtoList = getLeadDtoList(sheet,structure);
    validateResults(leadDtoList);
    return leadDtoList;
  }

  private List<LeadFileDto> getLeadDtoList(Sheet sheet, Optional<String> structure) {
    try {
      Class<? extends LeadFileDto> classToMap = getClassToMap(sheet,structure);

      return Poiji.fromExcel(sheet, classToMap)
      .stream().map(leadFileDto -> (LeadFileDto) leadFileDto).toList();

    } catch (HeaderMissingException ex){
      throw new CustomApplicationException(
          "Missing expected columns.",
          "The following columns were not found %s".formatted(ex.getMissingExcelCellNameHeaders()),
          HttpStatus.EXPECTATION_FAILED.value(), mappingErrors);
    }
  }

  private Class<? extends LeadFileDto> getClassToMap(Sheet sheet,Optional<String> structure) {
    if(structure.isPresent()){
      return LeadStructuresEnum.get(structure.get()).getFileClass();
    }else {
      return fileStructureSelector.calculateBestStructureToUse(sheet);
    }
  }

  private void validateResults(List<LeadFileDto> leadDtoList) {
    Map<String, String> mappingErrors = new LinkedHashMap<>();

    leadDtoList.forEach(l -> validateLead(l, mappingErrors));

    if (mappingErrors.size() > 0) {
      throw new CustomApplicationException(
          "Errors in leads data.", "The leads data in the file has errors, please fix them and retry.",
          HttpStatus.EXPECTATION_FAILED.value(), mappingErrors);
    }
  }

  private void validateLead(LeadFileDto leadDto, Map<String, String> mappingErrors) {
    Set<ConstraintViolation<LeadFileDto>> validate = validator.validate(leadDto);
    validate.forEach(e -> mappingErrors.put(
        "Error for lead in row: %d".formatted(leadDto.getRowIndex() + 1),
        "'%s' '%s'".formatted(e.getPropertyPath(), e.getMessage())));
  }
}
