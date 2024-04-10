package com.poc.exceltojavapojolist.application.income;

import com.poc.exceltojavapojolist.domain.Lead;
import lombok.Data;

public interface CreateLeadInPort {

  Lead createLead(CreateLeadRequest createLeadRequest);

  @Data
  class CreateLeadRequest {
    private String name;
    private String email;
  }

}
