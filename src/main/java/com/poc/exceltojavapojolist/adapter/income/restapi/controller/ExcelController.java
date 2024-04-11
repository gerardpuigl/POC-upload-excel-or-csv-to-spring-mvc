package com.poc.exceltojavapojolist.adapter.income.restapi.controller;

import com.poc.exceltojavapojolist.adapter.income.dto.LeadDto;
import com.poc.exceltojavapojolist.adapter.income.mapper.CsvFileMapper;
import com.poc.exceltojavapojolist.adapter.income.mapper.ExcelMapper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExcelController {

  public static String EXCEL_FILE_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static String CSV_FILE_TYPE = "text/csv";

  private final ExcelMapper excelMapper;
  private final CsvFileMapper csvFileMapper;

  @RequestMapping(method = RequestMethod.POST, value = "/leads/convertfile")

  public List<LeadDto> convertFile(@RequestParam("file") MultipartFile file) {
    try {
      List<LeadDto> leadDtoList = getCreateLeadRequestsFromFile(file);

      leadDtoList.forEach(l -> log.info("Lead received {}", l.toString()));
      // HERE we may send to interactor and persiste in database, publish events.
      // Or just convert to json as we do here.
      return leadDtoList;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<LeadDto> getCreateLeadRequestsFromFile(MultipartFile file) throws IOException {
    if (Objects.equals(file.getContentType(), EXCEL_FILE_TYPE)) {
      return excelMapper.excelFileToList(file.getInputStream());

    }
    if (Objects.equals(file.getContentType(), CSV_FILE_TYPE)) {
      return csvFileMapper.fromCsvWithHeader(file.getInputStream());

    } else {
      throw new RuntimeException("File has not accepted format");
    }
  }
}

