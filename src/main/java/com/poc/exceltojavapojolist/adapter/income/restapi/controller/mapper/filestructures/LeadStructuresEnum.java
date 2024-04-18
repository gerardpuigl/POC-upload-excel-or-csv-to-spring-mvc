package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.LeadFileDto;

public enum LeadStructuresEnum {

  DEFAULT("default", LeadDefaultFileStructure.class),
  OPTIONAL("optional", LeadOptionalFileStructure.class);

  private final Class<? extends LeadFileDto> fileClass;
  private final String value;

  LeadStructuresEnum(String value, Class<? extends LeadFileDto>  fileClass){
    this.fileClass = fileClass;
    this.value = value;
  }

  public static LeadStructuresEnum get(String value) {
    for (LeadStructuresEnum ft : values())
      if (ft.getValue().equals(value))
        return ft;
    return LeadStructuresEnum.DEFAULT;
  }

  public Class<? extends LeadFileDto> getFileClass() {
    return fileClass;
  }

  public String getValue() {
    return value;
  }
}