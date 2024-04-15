package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.filestructures;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper.LeadFileDto;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LeadOptionalFileStructure implements LeadFileDto {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName(value = "Vorname", mandatoryHeader = true)
    @NotBlank
    @Size(max = 6)
    private String name;

    @ExcelCellName(value = "Sprache", mandatoryHeader = true)
    private String language;

    @ExcelCellName(value = "E-Mail", mandatoryHeader = true)
    @Email
    private String email;

    //use getter to transform locale if necessary as the library poiji library use reflection internally
    public String getLanguage(){
        return language + "_DE";
    }
}
