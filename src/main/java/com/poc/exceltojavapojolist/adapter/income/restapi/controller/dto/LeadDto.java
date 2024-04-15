package com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadDto {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName(value = "name", mandatoryHeader = true)
    @NotBlank
    @Size(max = 6)
    private String name;

    @ExcelCellName(value = "email", mandatoryHeader = true)
    @Email
    private String email;

}
