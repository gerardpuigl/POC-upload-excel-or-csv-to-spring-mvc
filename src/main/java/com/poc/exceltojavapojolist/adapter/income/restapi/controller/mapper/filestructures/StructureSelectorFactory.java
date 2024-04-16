package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.LeadFileDto;
import org.apache.poi.ss.usermodel.Sheet;

public interface StructureSelectorFactory {

  Class<? extends LeadFileDto> getStructureClassToUse(Sheet sheet);
}
