package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.LeadFileDto;

public enum LeadStructures {

  DEFAULT("default","default", LeadDefaultFileStructure.class),
  OPTIONAL("optional","format2", LeadOptionalFileStructure.class);

  private final Class<? extends LeadFileDto> fileClass;
  private final String fileNameKeyWord;
  private final String value;

  LeadStructures(String value,String fileNameKeyWord, Class<? extends LeadFileDto>  fileClass){
    this.fileClass = fileClass;
    this.fileNameKeyWord = fileNameKeyWord;
    this.value = value;
  }

  public static LeadStructures get(String value) {
    for (LeadStructures ft : values())
      if (ft.getValue().equals(value))
        return ft;
    return null;
  }

  //we can get by fileName or we can decide use other strategy to get the right LeadStructure
  public static LeadStructures getByFileName(String fileName) {
    for (LeadStructures ft : values())
      if (!ft.fileNameKeyWord.equals("default") && fileName.contains(ft.fileNameKeyWord))
        return ft;
    return LeadStructures.DEFAULT;
  }

  public Class<? extends LeadFileDto> getFileClass() {
    return fileClass;
  }

  public String getValue() {
    return value;
  }
  public String getFileNameKeyWord() {
    return fileNameKeyWord;
  }
}