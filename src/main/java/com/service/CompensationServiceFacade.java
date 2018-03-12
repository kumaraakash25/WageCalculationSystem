package com.service;

import com.model.EmployeeDayRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CompensationServiceFacade {

    @Autowired
    private OverTimeCompensationService overTimeCompensationService;

    @Autowired
    private EveningCompensationService eveningCompensationService;
    
    @Autowired
    private RegularCompensationService regularCompensationService;

    public BigDecimal getOverTimeCompensation(final EmployeeDayRecord employeeDayRecord){
        return overTimeCompensationService.getCompensation(employeeDayRecord);
    }

    public BigDecimal getEveningCompensation(final EmployeeDayRecord employeeDayRecord){
        return eveningCompensationService.getCompensation(employeeDayRecord);
    }

    public BigDecimal getRegularCompensation(final EmployeeDayRecord employeeDayRecord){
        return regularCompensationService.getCompensation(employeeDayRecord);
    }
}
