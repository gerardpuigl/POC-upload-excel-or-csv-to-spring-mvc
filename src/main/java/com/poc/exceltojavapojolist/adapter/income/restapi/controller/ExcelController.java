package com.poc.exceltojavapojolist.adapter.income.restapi.controller;

import com.poc.exceltojavapojolist.adapter.income.mapper.ExcelMapper;
import com.poc.exceltojavapojolist.application.income.CreateLeadInPort;
import com.poc.exceltojavapojolist.application.income.CreateLeadInPort.CreateLeadRequest;
import java.io.IOException;
import java.util.List;
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
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  private final ExcelMapper excelMapper;

  private final CreateLeadInPort createLeadInPort;

  @RequestMapping(method = RequestMethod.POST, value = "/uploadExcelFile")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void uploadFile(@RequestParam("file") MultipartFile file) {

    validateExcelFormat(file);

    try {
      List<CreateLeadRequest> leadRequestList = excelMapper.excelFileToList(file.getInputStream());
      leadRequestList.forEach(l -> log.info("Lead received {}", l.toString()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void validateExcelFormat(MultipartFile file) {
    if (!TYPE.equals(file.getContentType())) {
      throw new RuntimeException("File has not accepted excel format");
    }
  }
}

