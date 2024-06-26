package com.poc.exceltojavapojolist.adapter.income.restapi.controller;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.CsvFileMapper;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.ExcelMapper;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.LeadMapper;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.LeadFileDto;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LeadFileController {

  public static String EXCEL_FILE_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static String CSV_FILE_TYPE = "text/csv";

  private final ExcelMapper excelMapper;
  private final CsvFileMapper csvFileMapper;
  private final LeadMapper leadMapper;

  @RequestMapping(method = RequestMethod.POST, value = "/leads/file")
  public List<LeadDto> convertFile(@RequestParam("file") MultipartFile file, @RequestParam Optional<String> structure) {

    try {
      List<LeadFileDto> leadDtoList = geLeadDtoListFromFile(file,structure);

      leadDtoList.forEach(l -> log.info("Lead received {}", l.toString()));
      // HERE we may send to interactor and persiste in database, publish events.
      // Or just convert to json as we do here.
      return leadDtoList.stream().map(leadMapper::toLeadDto).toList();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<LeadFileDto> geLeadDtoListFromFile(MultipartFile file, Optional<String> structure) throws IOException {
    if (Objects.equals(file.getContentType(), EXCEL_FILE_TYPE)) {
      return excelMapper.excelFileToList(file.getInputStream(),structure);

    }
    if (Objects.equals(file.getContentType(), CSV_FILE_TYPE)) {
      XSSFWorkbook workbook = csvFileMapper.csvFileToExcelWorkbook(file.getInputStream());
      return excelMapper.workBookToList(workbook,structure);

    } else {
      throw new RuntimeException("File has not accepted format");
    }
  }
}

