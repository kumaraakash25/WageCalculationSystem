package com.service;

import com.model.EmployeeDayWiseRecord;

import java.math.BigDecimal;

/**
 * Interface for compensation.
 */
public interface CompensationService {

    /**
     * Wage initial value.
     */
    double WAGE_INITIAL_VALUE = 0.0;

    BigDecimal getCompensation(EmployeeDayWiseRecord employeeDailyLog);
}
