package com.poc.exceltojavapojolist.adapter.income.restapi.exceptions.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ErrorDTO {

  private final String title;
  private final String detail;
  private final int status;
  private final List<InvalidParamDTO> invalidParams;
}