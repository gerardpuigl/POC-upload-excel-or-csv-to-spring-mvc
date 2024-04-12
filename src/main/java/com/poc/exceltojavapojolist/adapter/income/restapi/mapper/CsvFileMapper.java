package com.poc.exceltojavapojolist.adapter.income.restapi.mapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvFileMapper {

  public List<LeadDto> fromCsvWithHeader(InputStream file) {
    try {
      CsvSchema schema = CsvSchema.emptySchema().withHeader();
      MappingIterator<LeadDto> genericIterator = new CsvMapper()
          .readerWithSchemaFor(LeadDto.class).with(schema).readValues(file);
      return genericIterator.readAll();
    } catch (Exception e) {
      log.error("Error converting file to leads", e);
      throw new CustomApplicationException(
          "Error converting file to leads",e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
    }
  }
}