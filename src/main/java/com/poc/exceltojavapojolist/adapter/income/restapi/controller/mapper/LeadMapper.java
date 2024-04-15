package com.poc.exceltojavapojolist.adapter.income.restapi.controller.mapper;

import com.poc.exceltojavapojolist.adapter.income.restapi.controller.dto.LeadDto;
import org.springframework.stereotype.Component;

@Component
public class LeadMapper {

  //use mapstruct
  public LeadDto toLeadDto(LeadFileDto leadFileDto){
    return new LeadDto(
        leadFileDto.getName(),
        leadFileDto.getLanguage(),
        leadFileDto.getEmail()
    );
  }

}
