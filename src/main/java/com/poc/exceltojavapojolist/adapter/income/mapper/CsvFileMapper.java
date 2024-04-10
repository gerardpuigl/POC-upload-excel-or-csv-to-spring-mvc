package com.poc.exceltojavapojolist.adapter.income.mapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.poc.exceltojavapojolist.application.income.CreateLeadInPort.CreateLeadRequest;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvFileMapper {

  public List<CreateLeadRequest> fromCsvWithHeader(InputStream file) {
    try {
      CsvSchema schema = CsvSchema.emptySchema().withHeader();
      MappingIterator<CreateLeadRequest> genericIterator = new CsvMapper()
          .readerWithSchemaFor(CreateLeadRequest.class).with(schema).readValues(file);
      return genericIterator.readAll();
    } catch (Exception e) {
      log.error("Error converting file to leads", e);
      throw new RuntimeException("Error converting file to leads", e);
    }
  }

}
