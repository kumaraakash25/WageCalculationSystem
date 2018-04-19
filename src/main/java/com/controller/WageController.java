package com.controller;

import com.model.EmployeeMonthRecord;
import com.service.WageCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Employee Wage controller.
 */
@Controller
public class WageController {

    @Autowired
    public WageCalculatorService service;

    /**
     * Rest endpoint for getting the month wise salary report for the employees.
     *
     * @param model
     * @param monthYear URL parameter for monthYear in format mmYYYY
     * @return
     */
    @RequestMapping("/getMonthlySummary/{monthYear}")
    public String getMonthlySummary(Model model, @PathVariable("monthYear") String monthYear) {
        Map<Integer, EmployeeMonthRecord> resultMap = service.getMonthSummary(monthYear);
        model.addAttribute("monthYear", monthYear);
        if (resultMap == null) {
            return "noResult";
        }
        model.addAttribute("monthResult", resultMap);
        return "wageResult";
    }
}
