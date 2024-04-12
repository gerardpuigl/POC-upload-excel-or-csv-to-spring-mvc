package com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto;

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
    @NotBlank
    @Size(max = 6)
    private String name;
    @Email
    private String email;

}
