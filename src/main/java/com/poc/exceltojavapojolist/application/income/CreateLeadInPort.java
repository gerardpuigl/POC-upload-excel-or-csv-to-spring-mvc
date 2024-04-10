package com.poc.exceltojavapojolist.application.income;

import com.poc.exceltojavapojolist.domain.Lead;

public interface CreateLeadInPort {

  Lead createLead(CreateLeadRequest createLeadRequest);

  record CreateLeadRequest( String name, String email) {

  }

}
