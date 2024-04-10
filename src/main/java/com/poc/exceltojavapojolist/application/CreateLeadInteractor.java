package com.poc.exceltojavapojolist.application;

import com.poc.exceltojavapojolist.application.income.CreateLeadInPort;
import com.poc.exceltojavapojolist.domain.Lead;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateLeadInteractor implements CreateLeadInPort {

  public Lead createLead(CreateLeadRequest createLeadRequest) {
    return null;
  }
}
