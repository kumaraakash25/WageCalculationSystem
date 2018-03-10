package com.service;

import com.model.EmployeeDayRecord;
import com.model.WageRate;
import com.util.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Hours;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Regular compensation service to calculate the wage for regular hours.
 */
@Service
public class RegularCompensationService implements CompensationService {

    private final static Log LOG = LogFactory.getLog(RegularCompensationService.class);

    @Override
    public BigDecimal getCompensation(EmployeeDayRecord employeeDailyLog) {
        LOG.debug("Inside regular compensation service");
        Hours hours = Hours.hoursBetween(employeeDailyLog.getShiftStartDateTime(), employeeDailyLog.getShiftEndDateTime());
        int workHours = hours.getHours();
        return new BigDecimal(WageRate.HOURLY_WAGE.getWageRate() * workHours).
                setScale(Constants.ROUND_OFF_SCALE, BigDecimal.ROUND_HALF_EVEN);
    }
}
