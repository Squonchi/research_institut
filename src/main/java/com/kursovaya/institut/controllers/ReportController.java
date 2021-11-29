package com.kursovaya.institut.controllers;

import com.kursovaya.institut.models.Report;
import com.kursovaya.institut.models.User;
import com.kursovaya.institut.repo.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/reports")
    public String reportsMain(Model model) {
        Iterable<Report> reports = reportRepository.findAll();
        model.addAttribute("reports", reports);
        return "reports_main";
    }

    @GetMapping("/reports/add")
    public String reportsAdd(Model model) {
        return "reports_add";
    }

    @PostMapping("/reports/add")
    public String reportNewAdd(@AuthenticationPrincipal User user,
                               @RequestParam String title,
                               @RequestParam String anons,
                               @RequestParam String full_text, Model model) {
        Report report = new Report(title, anons, full_text, user);
        reportRepository.save(report);
        return "redirect:/reports";
    }

    @GetMapping("/reports/{id}")
    public String reportDetails(@PathVariable(value = "id") Long id, Model model) {
        if(!reportRepository.existsById(id)) {
            return "redirect:/repots";
        }
        Optional<Report> report = reportRepository.findById(id);
        report.ifPresent(rep -> model.addAttribute("report", rep));
        return "report_details";
    }
}
