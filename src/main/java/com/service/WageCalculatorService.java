package com.service;

import com.dao.EmployeeLogDao;
import com.model.EmployeeDayRecord;
import com.model.EmployeeMonthRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.util.Constants.*;

@Service
public class WageCalculatorService {

    @Autowired
    private CompensationServiceFacade compensationServiceFacade;
    @Autowired
    private EmployeeLogDao employeeTimeLogDao;

    private Map<String, Map<Integer, EmployeeMonthRecord>> monthWiseEmployeeLog;

    public static final DecimalFormat decimalFormat = new DecimalFormat("00");

    @Value("${employee.csv}")
    private String employeeCsvFile;

    /**
     * Get employee summary for the month.
     *
     * @param queryTimePeriod
     * @return
     */
    public Map<Integer, EmployeeMonthRecord> getMonthSummary(final String queryTimePeriod) {
        Map<Integer, EmployeeMonthRecord> map;
        if (monthWiseEmployeeLog == null) {
            monthWiseEmployeeLog = createEmployeeMonthlySummary();
        }
        map = monthWiseEmployeeLog.get(queryTimePeriod);
        return map;
    }

    /**
     * Create employee summary if not exists.
     *
     * @return
     */
    private Map<String, Map<Integer, EmployeeMonthRecord>> createEmployeeMonthlySummary() {
        final List<EmployeeDayRecord> employeeTimeLogList = employeeTimeLogDao.getEmployeeTimeLogList(employeeCsvFile);
        monthWiseEmployeeLog = new HashMap<>();
        for (EmployeeDayRecord employeeTimeLogRecord : employeeTimeLogList) {
            String monthYear = String.valueOf(decimalFormat.format(employeeTimeLogRecord.getShiftStartDateTime().getMonthOfYear())).concat(SEPERATOR)
                    .concat(String.valueOf(employeeTimeLogRecord.getShiftStartDateTime().getYear()));
            Map<Integer, EmployeeMonthRecord> employeeMonthlyWorkSummary = monthWiseEmployeeLog.get(monthYear);
            if (employeeMonthlyWorkSummary == null) {
                employeeMonthlyWorkSummary = new HashMap<>();
                EmployeeMonthRecord e = new EmployeeMonthRecord(employeeTimeLogRecord.getEmployeeName(),
                        computeWage(employeeTimeLogRecord));
                employeeMonthlyWorkSummary.put(employeeTimeLogRecord.getEmployeeId(), e);
                monthWiseEmployeeLog.put(monthYear, employeeMonthlyWorkSummary);
            } else {
                EmployeeMonthRecord employeeMonthlyEntry = employeeMonthlyWorkSummary.get(employeeTimeLogRecord.getEmployeeId());
                if (employeeMonthlyEntry != null) {
                    BigDecimal wage = employeeMonthlyEntry.getTotalWage();
                    if (wage == null) {
                        employeeMonthlyEntry.setTotalWage(computeWage(employeeTimeLogRecord));
                    } else {
                        employeeMonthlyEntry.setTotalWage(wage.add(computeWage(employeeTimeLogRecord)));
                    }
                } else {
                    employeeMonthlyEntry = new EmployeeMonthRecord(employeeTimeLogRecord.getEmployeeName(),
                            computeWage(employeeTimeLogRecord));
                    employeeMonthlyWorkSummary.put(employeeTimeLogRecord.getEmployeeId(), employeeMonthlyEntry);
                }
            }
        }
        return monthWiseEmployeeLog;
    }

    /**
     * Compute wage for the employee Record.
     *
     * @param employeeDailyLog
     * @return
     */
    private BigDecimal computeWage(final EmployeeDayRecord employeeDailyLog) {
        BigDecimal regularWage = compensationServiceFacade.getRegularCompensation(employeeDailyLog);
        BigDecimal overTimeWage = compensationServiceFacade.getOverTimeCompensation(employeeDailyLog);
        BigDecimal eveningWage = new BigDecimal(WAGE_INITIAL_VALUE);
        if (overTimeWage == new BigDecimal(WAGE_INITIAL_VALUE)) {
            eveningWage = compensationServiceFacade.getEveningCompensation(employeeDailyLog);
        }
        return regularWage.add(eveningWage).add(overTimeWage);
    }

}
