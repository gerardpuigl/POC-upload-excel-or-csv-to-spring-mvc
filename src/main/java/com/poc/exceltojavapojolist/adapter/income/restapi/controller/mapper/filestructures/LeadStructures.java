package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures;

public enum LeadStructures {

  DEFAULT("default", LeadDefaultFileStructure.class),
  OPTIONAL("optional", LeadOptionalFileStructure.class);

  private final Class<? extends LeadFileDto> fileClass;
  private final String value;

  LeadStructures(String value, Class<? extends LeadFileDto>  fileClass){
    this.fileClass = fileClass;
    this.value = value;
  }

  public static LeadStructures get(String value) {
    for (LeadStructures ft : values())
      if (ft.getValue().equals(value))
        return ft;
    return null;
  }

  public Class<? extends LeadFileDto> getFileClass() {
    return fileClass;
  }

  public String getValue() {
    return value;
  }
}