package com.poc.exceltojavapojolist.adapter.income.restapi.exceptions;

import com.poc.exceltojavapojolist.adapter.income.restapi.exceptions.dto.ErrorDTO;
import com.poc.exceltojavapojolist.adapter.income.restapi.exceptions.dto.InvalidParamDTO;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException;
import com.poc.exceltojavapojolist.domain.exception.CustomApplicationException.InvalidParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomApplicationException.class)
  public ResponseEntity<ErrorDTO> handleMaxSizeException(CustomApplicationException ex) {
    log.warn("Exception catch:",ex);
    return getErrorDTOResponseEntity(ex.getTitle(), ex.getDetail(), ex.getStatus(),
        mapToInvalidParamDto(ex.getInvalidParams()));
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleUnexpectedException(Exception ex) {
    log.error("Unexpected Exception catch:",ex);
    return getErrorDTOResponseEntity("Unexpected Exception",
        ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(), List.of());
  }

  private ResponseEntity<ErrorDTO> getErrorDTOResponseEntity(String title, String message, int status,
      List<InvalidParamDTO> invalidParams) {
    return ResponseEntity.status(status).body(new ErrorDTO(title, message, status, invalidParams));
  }

  private List<InvalidParamDTO> mapToInvalidParamDto(List<InvalidParam> invalidParam){
    return invalidParam.stream().map(ip ->
        new InvalidParamDTO(ip.getName(),ip.getReason())).toList();
  }

}
