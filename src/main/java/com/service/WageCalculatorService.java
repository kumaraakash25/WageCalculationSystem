package com.service;

import com.dao.EmployeeLogDao;
import com.model.EmployeeDayWiseRecord;
import com.model.EmployeeMonthRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.util.Constants.*;

/**
 * Service calculates employee monthly wage summary and saves the result period wise in a map.
 */
@Service
public class WageCalculatorService {

    /**
     * Static decimal format.
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

    /**
     * Map for saving the month wise total wage summary.
     */
    private static Map<String, Map<Integer, EmployeeMonthRecord>> MONTHWISE_EMPLOYEE_WAGE_MAP = new HashMap<>();

    @Autowired
    private CompensationServiceFacade compensationServiceFacade;

    @Autowired
    private EmployeeLogDao employeeTimeLogDao;

    /**
     * Sample CSV file name.
     */
    @Value("${employee.csv}")
    private String EMPLOYEE_CSV_FILE;

    /**
     * Returns employee wage summary for the month from a pre-populated map. If the API is requested for the first time,
     * it calculates the overall summary for all the periods.
     *
     * @param queryTimePeriod
     * @return
     */
    public Map<Integer, EmployeeMonthRecord> getMonthSummary(final String queryTimePeriod) {
        Map<Integer, EmployeeMonthRecord> map;
        if (CollectionUtils.isEmpty(MONTHWISE_EMPLOYEE_WAGE_MAP)) {
            MONTHWISE_EMPLOYEE_WAGE_MAP = createEmployeeMonthlySummary();
        }
        // If the map is pre-populated the summary is returned for a particular period.
        map = MONTHWISE_EMPLOYEE_WAGE_MAP.get(queryTimePeriod);
        return map;
    }

    /**
     * Iterates the daily records of the employee and created a month wise summary map.
     *
     * @return
     */
    private Map<String, Map<Integer, EmployeeMonthRecord>> createEmployeeMonthlySummary() {
        final List<EmployeeDayWiseRecord> employeeTimeLogList = employeeTimeLogDao.getEmployeeTimeLogList(EMPLOYEE_CSV_FILE);
        for (EmployeeDayWiseRecord employeeTimeLogRecord : employeeTimeLogList) {
            employeeDayWiseWageCalculator(employeeTimeLogRecord);
        }
        return MONTHWISE_EMPLOYEE_WAGE_MAP;
    }

    /**
     * Calculates daily wage for an employee and adds it to the month wise map.
     *
     * @param employeeDayWiseRecord
     */
    private void employeeDayWiseWageCalculator(final EmployeeDayWiseRecord employeeDayWiseRecord) {
        String monthYear = String.valueOf(DECIMAL_FORMAT.format(employeeDayWiseRecord.getShiftStartDateTime()
                .getMonthOfYear())).concat(SEPERATOR)
                .concat(String.valueOf(employeeDayWiseRecord.getShiftStartDateTime().getYear()));
        Map<Integer, EmployeeMonthRecord> employeeMonthlyWorkSummary = MONTHWISE_EMPLOYEE_WAGE_MAP.get(monthYear);
        if (employeeMonthlyWorkSummary == null) {
            MONTHWISE_EMPLOYEE_WAGE_MAP.put(monthYear, createEmployeeRecordForMonth(employeeDayWiseRecord));
        } else {
            updateEmployeeMonthlyWage(employeeMonthlyWorkSummary, employeeDayWiseRecord);
        }
    }

    /**
     * Creates a new record according to the employee id.
     *
     * @param employeeDayWiseRecord
     * @return
     */
    private Map<Integer, EmployeeMonthRecord> createEmployeeRecordForMonth(final EmployeeDayWiseRecord employeeDayWiseRecord) {
        Map<Integer, EmployeeMonthRecord> employeeMonthlyWorkSummary = new HashMap<>();
        EmployeeMonthRecord e = new EmployeeMonthRecord(employeeDayWiseRecord.getEmployeeName(),
                computeDayWage(employeeDayWiseRecord));
        employeeMonthlyWorkSummary.put(employeeDayWiseRecord.getEmployeeId(), e);
        return employeeMonthlyWorkSummary;
    }

    /**
     * Update existing employee wage monthly record.
     *
     * @param employeeMonthlyWorkSummary
     * @param employeeDayWiseRecord
     */
    private void updateEmployeeMonthlyWage(final Map<Integer, EmployeeMonthRecord> employeeMonthlyWorkSummary,
                                           final EmployeeDayWiseRecord employeeDayWiseRecord) {
        EmployeeMonthRecord employeeMonthlyEntry = employeeMonthlyWorkSummary.get(employeeDayWiseRecord.getEmployeeId());
        if (employeeMonthlyEntry != null) {
            BigDecimal wage = employeeMonthlyEntry.getTotalWage();
            if (wage == null) {
                employeeMonthlyEntry.setTotalWage(computeDayWage(employeeDayWiseRecord));
            } else {
                employeeMonthlyEntry.setTotalWage(wage.add(computeDayWage(employeeDayWiseRecord)));
            }
        } else {
            employeeMonthlyEntry = new EmployeeMonthRecord(employeeDayWiseRecord.getEmployeeName(),
                    computeDayWage(employeeDayWiseRecord));
            employeeMonthlyWorkSummary.put(employeeDayWiseRecord.getEmployeeId(), employeeMonthlyEntry);
        }
    }

    /**
     * Compute wage for the employee Record. Each record is read from the sample CSV file translated to a POJO.
     *
     * @param employeeDailyLog
     * @return Total wage of an employee for each day record POJO.
     */
    private BigDecimal computeDayWage(final EmployeeDayWiseRecord employeeDailyLog) {
        BigDecimal regularWage = compensationServiceFacade.getRegularCompensation(employeeDailyLog);
        BigDecimal overTimeWage = compensationServiceFacade.getOverTimeCompensation(employeeDailyLog);
        BigDecimal eveningWage = new BigDecimal(WAGE_INITIAL_VALUE);
        if (overTimeWage == new BigDecimal(WAGE_INITIAL_VALUE)) {
            eveningWage = compensationServiceFacade.getEveningCompensation(employeeDailyLog);
        }
        return regularWage.add(eveningWage).add(overTimeWage);
    }


}
