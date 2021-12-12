package com.kursovaya.institut.controllers;

import com.kursovaya.institut.models.Report;
import com.kursovaya.institut.models.ReportStatus;
import com.kursovaya.institut.models.User;
import com.kursovaya.institut.models.Role;
import com.kursovaya.institut.repo.ReportRepository;
import com.kursovaya.institut.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/reports")
    public String reportsMain(@AuthenticationPrincipal User currentUser, Model model) {
        Iterable<Report> reports = reportRepository.findAll();
        Iterable<User> users = userRepository.findAllByRoles(Role.USER);
        model.addAttribute("users", users);
        model.addAttribute("curUser", currentUser);
        model.addAttribute("reports", reports);
        return "reports_main";
    }

    /*@GetMapping("/reports/add")
    public String reportsAdd(Model model) {
        return "reports_add";
    }*/

    @PostMapping(value = "/reports", params = {"title"})
    public String addNewTheme(@RequestParam String title, Model model) {
        Report report = new Report(title);
        reportRepository.save(report);
        return "redirect:/reports";
    }

    @PostMapping(value = "/reports", params = {"username", "id"})
    public String giveThemeToEmployee(@RequestParam String username, @RequestParam Long id, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        User user = userRepository.findUserByUsername(username);
        if (report.isPresent()) {
            report.get().setAuthor(user);
            report.get().setStatus(ReportStatus.IN_PROGRESS);
            reportRepository.save(report.get());
        }
        return "redirect:/reports";
    }


    @GetMapping("/reports/{id}/add")
    public String writeReport(@PathVariable(value = "id") Long id, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        report.ifPresent(rep -> model.addAttribute("report", rep));
        return "reports_add";
    }

    @PostMapping(value = "/reports/{id}/add", params = {"anons", "full_text"})
    public String addReport(@PathVariable(value = "id") Long id,
                            @RequestParam String anons,
                            @RequestParam String full_text,
                            @RequestParam ReportStatus status,Model model) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            report.get().setAnons(anons);
            report.get().setFull_text(full_text);
            report.get().setStatus(status);
            reportRepository.save(report.get());
        }
        return "redirect:/reports";
    }

    @GetMapping("/reports/{id}/edit")
    public String reportEdit(@PathVariable(value = "id") Long id, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        report.ifPresent(rep -> model.addAttribute("report", rep));
        return "reports_edit";
    }


    @PostMapping(value = "/reports/{id}/edit", params = {"anons", "full_text"})
    public String addEditingReport(@PathVariable(value = "id") Long id,
                                   @RequestParam String anons,
                                   @RequestParam String full_text,
                                   @RequestParam ReportStatus status, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            report.get().setAnons(anons);
            report.get().setFull_text(full_text);
            report.get().setStatus(status);
            reportRepository.save(report.get());
        }
        return "redirect:/reports";
    }

    @GetMapping("/reports/{id}")
    public String reportDetails(@PathVariable(value = "id") Long id, Model model) {
        if (!reportRepository.existsById(id)) {
            return "redirect:/reports";
        }
        Optional<Report> report = reportRepository.findById(id);
        report.ifPresent(rep -> model.addAttribute("report", rep));
        return "report_details";
    }

    @PostMapping("/reports/{id}")
    public String sendFeedback(@PathVariable(value = "id") Long id,
                               @RequestParam ReportStatus status,
                               @RequestParam String comment, Model model) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            report.get().setStatus(status);
            report.get().setComment(comment);
            reportRepository.save(report.get());
        }
        return "redirect:/reports";
    }
}
