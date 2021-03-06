package com.service;

import com.model.Duration;
import com.model.EmployeeDayWiseRecord;
import com.model.WageRate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.model.Duration.STANDARD_WORK_HOURS;
import static com.util.Constants.ROUND_OFF_SCALE;

/**
 * Overtime shift service to calculate the wage. If the employee work duration exceeds the standard 8 hours
 * the employee is eligible for overtime payment.
 */
@Service
public class OverTimeCompensationService implements CompensationService {

    private final static Log LOG = LogFactory.getLog(OverTimeCompensationService.class);

    @Override
    public BigDecimal getCompensation(EmployeeDayWiseRecord employeeDailyLog) {
        LOG.debug("Inside overtime compensation service");
        DateTime employeeShiftStartDateTime = employeeDailyLog.getShiftStartDateTime();
        DateTime employeeShiftEndDateTime = employeeDailyLog.getShiftEndDateTime();
        int workHours = getHourInterval(employeeShiftStartDateTime, employeeShiftEndDateTime);
        final double overTimeWage = calculateWage(workHours);
        LOG.debug("Wage " + overTimeWage + " calculated for employee ID " + employeeDailyLog.getEmployeeId());
        return new BigDecimal(overTimeWage).setScale(ROUND_OFF_SCALE, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Calculates the employee wage according to the overtime time shift.
     *
     * @param workHours
     * @return
     */
    private double calculateWage(final int workHours) {
        double wage = WAGE_INITIAL_VALUE;
        if (workHours > STANDARD_WORK_HOURS.getDuration()) {
            float overTime = workHours - STANDARD_WORK_HOURS.getDuration();
            if (overTime > Duration.FIRST_OVERTIME_DURATION.getDuration()) {
                wage = getWage(WageRate.HOURLY_WAGE, WageRate.FIRST_OVERTIME_DURATION_PERCENT,
                        Duration.FIRST_OVERTIME_DURATION.getDuration());
                overTime = overTime - Duration.FIRST_OVERTIME_DURATION.getDuration();
                if (overTime >= Duration.SECOND_OVERTIME_DURATION.getDuration()) {
                    wage = wage + getWage(WageRate.HOURLY_WAGE, WageRate.SECOND_OVERTIME_DURATION_PERCENT,
                            Duration.SECOND_OVERTIME_DURATION.getDuration());
                    overTime = overTime - Duration.SECOND_OVERTIME_DURATION.getDuration();
                    if (overTime > Duration.NULL.getDuration()) {
                        wage = wage + getWage(WageRate.HOURLY_WAGE, WageRate.THIRD_OVERTIME_DURATION_PERCENT, overTime);
                    }
                } else {
                    wage = wage + getWage(WageRate.HOURLY_WAGE, WageRate.SECOND_OVERTIME_DURATION_PERCENT, overTime);
                }
            } else {
                wage = getWage(WageRate.HOURLY_WAGE, WageRate.FIRST_OVERTIME_DURATION_PERCENT, overTime);
            }
        }
        return wage;
    }

    /**
     * Get hour difference between dates.
     *
     * @param datetime1
     * @param dateTime2
     * @return
     */
    private int getHourInterval(final DateTime datetime1, final DateTime dateTime2) {
        return Hours.hoursBetween(datetime1, dateTime2).getHours();
    }

    /**
     * Calculate hourly wage.
     *
     * @param hourlyWage
     * @param wagePercent
     * @param overTime
     * @return
     */
    private double getWage(final WageRate hourlyWage, final WageRate wagePercent, final float overTime) {
        double wage = hourlyWage.getWageRate() * wagePercent.getWageRate() * overTime;
        return wage;
    }
}
