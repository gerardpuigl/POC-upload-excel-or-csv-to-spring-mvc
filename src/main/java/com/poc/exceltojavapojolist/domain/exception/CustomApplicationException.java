package com.poc.exceltojavapojolist.domain.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CustomApplicationException extends RuntimeException{

  private final String title;
  private final String detail;
  private final int status;
  private final List<InvalidParam> invalidParams;

  @Data
  public class InvalidParam {
    final String name;
    final String reason;
  }

  public CustomApplicationException(String title, String detail, int status) {
    this.title = title;
    this.detail = detail;
    this.status = status;
    this.invalidParams = new ArrayList<>();
  }

  public CustomApplicationException(String title, String detail, int status, Map<String,String> invalidParamsMap) {
    this.title = title;
    this.detail = detail;
    this.status = status;
    this.invalidParams = new ArrayList<>();
    addInvalidParams(invalidParamsMap);
  }

  public CustomApplicationException(String title, String detail, int status, Throwable cause) {
    super(cause);
    this.title = title;
    this.detail = detail;
    this.status = status;
    this.invalidParams = new ArrayList<>();
  }

  public void addInvalidParam(String name, String reason) {
    invalidParams.add(new InvalidParam(name, reason));
  }

  public void addInvalidParams(Map<String,String> invalidParamsMap) {
    invalidParamsMap.forEach(((name,reson) ->
        invalidParams.add(new InvalidParam(name,reson))));
  }
}
