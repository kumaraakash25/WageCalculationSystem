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

    /**
     * Wage calculation service.
     */
    @Autowired
    public WageCalculatorService service;

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
