package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.LeadFileDto;
import com.poiji.annotation.ExcelCellName;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

@Component
public class AutomaticFileSelector {

  private final List<Class<? extends LeadFileDto>> supportedFileStructures =
      List.of(LeadDefaultFileStructure.class, LeadOptionalFileStructure.class);

  private final Map<Class<? extends LeadFileDto>, Set<String>> mandatoryColumnsPerStructure =
      supportedFileStructures.stream().collect(Collectors.toMap(c -> c, this::getMandatoryExcelColumnNamesByGivenClass));


  public Class<? extends LeadFileDto> calculateBestStructureToUse(Sheet sheet) {
    return getMostSimilarStructure(sheet.getRow(0).cellIterator());
  }

  private Set<String> getMandatoryExcelColumnNamesByGivenClass(Class<? extends LeadFileDto> analyzedClass) {
    Set<String> mandatoryExcelColumnNames = new HashSet<>();
    Field[] fields = analyzedClass.getDeclaredFields();
    for (Field field : fields) {
      ExcelCellName excelCellNameAnnotation = field.getAnnotation(ExcelCellName.class);
      if (excelCellNameAnnotation != null && excelCellNameAnnotation.mandatoryHeader()) {
        mandatoryExcelColumnNames.add(excelCellNameAnnotation.value());
      }
    }
    return mandatoryExcelColumnNames;
  }

  private Class<? extends LeadFileDto> getMostSimilarStructure(Iterator<Cell> cellIterator) {
    Set<String> columnHeaders = new HashSet<>();
    cellIterator.forEachRemaining(cell -> columnHeaders.add(cell.getStringCellValue()));
    Map<? extends Class<? extends LeadFileDto>, Integer> calculatedCommonHeaders = calculateNumberOfCommonHeaders(columnHeaders);
   return calculatedCommonHeaders.entrySet().stream().sorted(Entry.comparingByValue()).findFirst().get().getKey();
  }

  private Map<? extends Class<? extends LeadFileDto>, Integer> calculateNumberOfCommonHeaders(Set<String> columnHeaders) {
    return mandatoryColumnsPerStructure.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey,
            m -> {
              columnHeaders.removeAll(m.getValue());
              return columnHeaders.size();
            }));
  }
}
