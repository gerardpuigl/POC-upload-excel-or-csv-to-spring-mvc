package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LeadDefaultFileStructure implements LeadFileDto {

  @ExcelRow
  private int rowIndex;

  @ExcelCellName(value = "name", mandatoryHeader = true)
  @NotBlank
  @Size(max = 6)
  private String name;

  @ExcelCellName(value = "language", mandatoryHeader = true)
  private String language;

  @ExcelCellName(value = "email", mandatoryHeader = true)
  @Email
  private String email;

}
