package com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadDto {
    private String name;
    private String email;
}
