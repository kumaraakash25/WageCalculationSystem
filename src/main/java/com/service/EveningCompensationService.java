package com.service;

import com.model.Duration;
import com.model.EmployeeDayRecord;
import com.model.WageRate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.util.Constants.ROUND_OFF_SCALE;

/**
 * Evening shift service to compute the wage.
 */
@Service
public class EveningCompensationService implements CompensationService {

    private final static Log LOG = LogFactory.getLog(EveningCompensationService.class);

    @Override
    public BigDecimal getCompensation(final EmployeeDayRecord employeeDailyLog) {
        LOG.debug("Inside evening compensation service");
        DateTime eveningShiftStartDate = new DateTime(employeeDailyLog.getDate()).
                withHourOfDay((int)Duration.EVENING_SHIFT_START_HOUR.getDuration()).
                withMinuteOfHour((int)Duration.NULL.getDuration());
        DateTime eveningShiftEndDate = new DateTime(employeeDailyLog.getDate()).
                withHourOfDay((int)Duration.EVENING_SHIFT_END_HOUR.getDuration()).plusDays(1).
                withMinuteOfHour((int)Duration.NULL.getDuration());
        DateTime employeeShiftEndDateTime = employeeDailyLog.getShiftEndDateTime();
        int workHours = (int)Duration.NULL.getDuration();
        if (employeeShiftEndDateTime.isAfter(eveningShiftStartDate)) {
            if (DateUtils.isSameDay(employeeDailyLog.getShiftEndDateTime().toDate(), eveningShiftStartDate.toDate())) {
                Hours hoursAheadOfStart = Hours.hoursBetween(eveningShiftStartDate, employeeShiftEndDateTime);
                workHours = hoursAheadOfStart.getHours();
            } else {
                if (employeeShiftEndDateTime.isAfter(eveningShiftEndDate)) {
                    Hours hoursAheadOfStart = Hours.hoursBetween(eveningShiftStartDate, eveningShiftEndDate);
                    workHours = hoursAheadOfStart.getHours();
                } else {
                    Hours hoursAheadOfStart = Hours.hoursBetween(employeeShiftEndDateTime, eveningShiftEndDate);
                    workHours = hoursAheadOfStart.getHours();
                }

            }
        }
        return new BigDecimal(WageRate.EVENING_COMPENSATION.getWageRate() * workHours).
                setScale(ROUND_OFF_SCALE, BigDecimal.ROUND_HALF_EVEN);
    }
}
