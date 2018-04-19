package com.model;

import java.math.BigDecimal;

/**
 * Employee monthly record. POJO for holding the employee name and salary for a particular period
 */
public class EmployeeMonthRecord {
    /**
     * Field for employee name.
     */
    private String employeeName;
    /**
     * Field for employee wage.
     */
    private BigDecimal totalWage;

    public EmployeeMonthRecord(final String employeeName, final BigDecimal totalWage) {
        this.totalWage = totalWage;
        this.employeeName = employeeName;
    }

    public BigDecimal getTotalWage() {
        return totalWage;
    }

    public void setTotalWage(final BigDecimal totalWage) {
        this.totalWage = totalWage;
    }

    public String getEmployeeName() {
        return employeeName;
    }

}
