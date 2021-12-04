package com.kursovaya.institut.controllers;

import com.kursovaya.institut.models.Report;
import com.kursovaya.institut.models.ReportStatus;
import com.kursovaya.institut.repo.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /*@GetMapping("/reports/add")
    public String reportsAdd(Model model) {
        return "reports_add";
    }*/

    @PostMapping("/reports")
    public String addNewTheme(@RequestParam String title, Model model) {
        Report report = new Report(title);
        reportRepository.save(report);
        return "redirect:/reports";
    }

    @PostMapping(value = "/reports/add", params = {"id"})
    public String writeReport(@RequestParam Long id, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        report.ifPresent(rep -> model.addAttribute("report", rep));
        return "reports_add";
    }

    @PostMapping(value = "/reports/add", params = {"id", "anons", "full_text"})
    public String addReport(@RequestParam Long id,
                            @RequestParam String anons,
                            @RequestParam String full_text, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            report.get().setAnons(anons);
            report.get().setFull_text(full_text);
            report.get().setStatus(ReportStatus.ON_INSPECTION);
            reportRepository.save(report.get());
        }
        return "redirect:/reports";
    }

    @PostMapping("/reports/edit")
    public String reportEdit(@RequestParam Long id,
                             Model model) {
        Optional<Report> report = reportRepository.findById(id);
        report.ifPresent(rep -> model.addAttribute("report", rep));
        return "redirect:/reports";
    }

    @PostMapping(value = "/reports/edit", params = {"id", "anons", "full_text"})
    public String addEditingReport(@RequestParam Long id,
                            @RequestParam String anons,
                            @RequestParam String full_text, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            report.get().setAnons(anons);
            report.get().setFull_text(full_text);
            report.get().setStatus(ReportStatus.ON_INSPECTION);
            reportRepository.save(report.get());
        }
        return "redirect:/reports";
    }

    @GetMapping("/reports/{id}")
    public String reportDetails(@PathVariable(value = "id") Long id, Model model) {
        if (!reportRepository.existsById(id)) {
            return "redirect:/repots";
        }
        Optional<Report> report = reportRepository.findById(id);
        report.ifPresent(rep -> model.addAttribute("report", rep));
        return "report_details";
    }

    /*@GetMapping("/themes")
    public String themesList(Model model) {
        Iterable<Report> reports = reportRepository.findAll();
        model.addAttribute("reports", reports);
        return "themes";
    }*/
}
