package com.model;

/**
 * Enum for the wage rate calculation.
 */
public enum WageRate {
    /**
     * Regular work hours hourly wage rate.
     */
    HOURLY_WAGE ( 4.25),
    /**
     * Evening work hours hourly wage rate.
     */
    EVENING_COMPENSATION( 1.25),
    /**
     * Overtime wage percentage for first three hour duration.
     */
    FIRST_OVERTIME_DURATION_PERCENT( .25),
    /**
     * Overtime wage percentage for the next one hour duration.
     */
    SECOND_OVERTIME_DURATION_PERCENT( .50),
    /**
     * Overtime wage percent after 4 hours.
     */
    THIRD_OVERTIME_DURATION_PERCENT( 1.00);

    private final double wageRate;

    /**
     * Return the matching wage rate.
     *
     * @return wageRate
     */
    public double getWageRate() {
        return wageRate;
    }

    WageRate(final double wageRate) {
        this.wageRate = wageRate;
    }
}
