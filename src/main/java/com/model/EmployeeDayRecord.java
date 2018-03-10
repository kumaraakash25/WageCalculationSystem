package com.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Employee day record read from the CSV file line by line.
 */
public class EmployeeDayRecord {
    /**
     * Field for employee name.
     */
    @CsvBindByPosition(position = 0)
    private String employeeName;
    /**
     * Field for employee ID.
     */
    @CsvBindByPosition(position = 1)
    private int employeeId;
    /**
     * Field for log date.
     */
    @CsvBindByPosition(position = 2)
    @CsvDate(value = "dd.MM.yyyy")
    private Date date;
    /**
     * Field for start time of employee shift.
     */
    @CsvBindByPosition(position = 3)
    private String startTime;
    /**
     * Field for end time of employee shift.
     */
    @CsvBindByPosition(position = 4)
    private String endTime;
    /**
     * Field for start date time of the shift.
     */
    private DateTime shiftStartDateTime;
    /**
     * Field for the end date time of the shift.
     */
    private DateTime shiftEndDateTime;

    public DateTime getShiftStartDateTime() {
        return shiftStartDateTime;
    }

    public void setShiftStartDateTime(final DateTime shiftStartDateTime) {
        this.shiftStartDateTime = shiftStartDateTime;
    }

    public DateTime getShiftEndDateTime() {
        return shiftEndDateTime;
    }

    public void setShiftEndDateTime(final DateTime shiftEndDateTime) {
        this.shiftEndDateTime = shiftEndDateTime;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public Date getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
