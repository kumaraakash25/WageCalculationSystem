package com.service;

import com.model.EmployeeDayWiseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for redirecting the employee day wise record to particular time shift for wage calculation.
 */
@Service
public class CompensationServiceFacade {

    @Autowired
    private OverTimeCompensationService overTimeCompensationService;

    @Autowired
    private EveningCompensationService eveningCompensationService;
    
    @Autowired
    private RegularCompensationService regularCompensationService;

    public BigDecimal getOverTimeCompensation(final EmployeeDayWiseRecord employeeDayWiseRecord){
        return overTimeCompensationService.getCompensation(employeeDayWiseRecord);
    }

    public BigDecimal getEveningCompensation(final EmployeeDayWiseRecord employeeDayWiseRecord){
        return eveningCompensationService.getCompensation(employeeDayWiseRecord);
    }

    public BigDecimal getRegularCompensation(final EmployeeDayWiseRecord employeeDayWiseRecord){
        return regularCompensationService.getCompensation(employeeDayWiseRecord);
    }
}
