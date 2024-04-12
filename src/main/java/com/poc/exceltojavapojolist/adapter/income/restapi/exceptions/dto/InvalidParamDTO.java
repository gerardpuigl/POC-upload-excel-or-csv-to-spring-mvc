package com.poc.exceltojavapojolist.adapter.income.restapi.exceptions.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class InvalidParamDTO {

  private final String name;
  private final String reason;
}
