package com.poc.exceltojavapojolist.adapter.income.restapi.controller;

import com.poc.exceltojavapojolist.adapter.income.mapper.CsvFileMapper;
import com.poc.exceltojavapojolist.adapter.income.mapper.ExcelMapper;
import com.poc.exceltojavapojolist.application.income.CreateLeadInPort;
import com.poc.exceltojavapojolist.application.income.CreateLeadInPort.CreateLeadRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ExcelController {

  public static String EXCEL_FILE_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static String CSV_FILE_TYPE = "text/csv";

  private final ExcelMapper excelMapper;
  private final CsvFileMapper csvFileMapper;

  private final CreateLeadInPort createLeadInPort;

  @RequestMapping(method = RequestMethod.POST, value = "/leads/file")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      List<CreateLeadRequest> leadRequestList = getCreateLeadRequestsFromFile(file);
      leadRequestList.forEach(l -> log.info("Lead received {}", l.toString()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<CreateLeadRequest> getCreateLeadRequestsFromFile(MultipartFile file) throws IOException {
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

