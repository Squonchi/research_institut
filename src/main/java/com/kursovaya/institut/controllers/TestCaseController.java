package com.kursovaya.institut.controllers;

import com.kursovaya.institut.business.TestCaseRunner;
import com.kursovaya.institut.models.*;
import com.kursovaya.institut.repo.TestCaseRepository;
import com.kursovaya.institut.repo.TestResultRepository;
import com.kursovaya.institut.repo.TestStepRepository;
import com.kursovaya.institut.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TestCaseController {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestStepRepository testStepRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @GetMapping("/test_cases")
    public String testCasesMain(Model model) {
        Iterable<TestCase> testCaseList = testCaseRepository.findAll();
        //Iterable<User> users = userRepository.findAllByRoles(Role.USER);
        //model.addAttribute("users", users);
        //model.addAttribute("curUser", currentUser);
        model.addAttribute("testCases", testCaseList);
        return "test_cases";
    }

    @GetMapping("/test_cases/{id}")
    public String reportDetails(@PathVariable(value = "id") Long id, Model model) {
        if (!testCaseRepository.existsById(id)) {
            return "redirect:/test_cases";
        }
        Optional<TestCase> testCase = testCaseRepository.findById(id);
        testCase.ifPresent(test -> model.addAttribute("testCase", test));
        return "test_case_details";
    }


    @GetMapping("/test_cases/create")
    public String testCaseCreatePage(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TestCase testCase = TestCase.builder()
                .author(currentUser)
                .status(TestCaseStatus.IN_PROGRESS)
                .description("")
                .title("Новый кейс")
                .build();
        TestStep testStep = TestStep.builder()
                .stepNumber(0)
                .body("stepBody")
                .build();
        TestStepsWrapper testStepsWrapper = new TestStepsWrapper();
        testStepsWrapper.add(testStep);
        List<TestStep> testSteps = testStepsWrapper.getTestSteps();
        model.addAttribute("testCase", testCase);
        model.addAttribute("testSteps", testStepsWrapper);
        return "test_case_creating";
    }

    @PostMapping(value = "/test_cases/create", params = {"save"})
    public String testCaseCreateSave(@AuthenticationPrincipal User currentUser,
                                     @ModelAttribute("testCase") TestCase testCase,
                                     @ModelAttribute("testSteps") TestStepsWrapper testSteps,
                                     Model model) {
        List<TestStep> steps = testSteps.getTestSteps();
        steps.forEach(e -> e.setTestCase(testCase));
        testCase.setAuthor(currentUser);
        testCase.setStatus(TestCaseStatus.IS_READY);
        testCaseRepository.save(testCase);
        testStepRepository.saveAll(steps);
        return "redirect:/test_cases/" + testCase.getId();
    }

    @PostMapping(value = "/test_cases/{id}", params = {"edit"})
    public String testCaseCreateEdit(@PathVariable(value = "id") Long id, Model model) {
        if (!testCaseRepository.existsById(id)) {
            return "redirect:/test_cases";
        }
        Optional<TestCase> testCase = testCaseRepository.findById(id);
        if (testCase.isPresent()) {
            model.addAttribute("testCase", testCase.get());
            model.addAttribute("testSteps", new TestStepsWrapper(testCase.get().getSteps()));
        }
        return "test_case_creating";
    }

    @PostMapping(value = "/test_cases/{id}", params = {"run"})
    public String testCaseRun(@PathVariable(value = "id") Long id) {
        if (!testCaseRepository.existsById(id)) {
            return "redirect:/test_cases";
        }
        Optional<TestCase> testCase = testCaseRepository.findById(id);
        Result result = new Result();
        if (testCase.isPresent())
            result = TestCaseRunner.testCaseRun(testCase.get());
        testResultRepository.save(result);
        return "redirect:/test_results";
    }

    @PostMapping(value = "/test_cases/create", params = {"addRow"})
    public String testCaseCreateAddRow(@ModelAttribute("testCase") TestCase testCase,
                                       @ModelAttribute("testSteps") TestStepsWrapper testSteps,
                                       Model model) {
        TestStep testStep = TestStep.builder().stepNumber(testSteps.getTestSteps().size()).body("newTestStep").build();
        testSteps.add(testStep);
        testSteps.sortSteps();
        return "test_case_creating";
    }

    @PostMapping(value = "/test_cases/create", params = {"removeRow"})
    public String testCaseCreateRemoveRow(@ModelAttribute("testCase") TestCase testCase,
                                          @ModelAttribute("testSteps") TestStepsWrapper testSteps,
                                          @RequestParam Integer removeRow,
                                          Model model) {
        testSteps.removeStep(removeRow);
        testSteps.sortSteps();
        return "test_case_creating";
    }

    @PostMapping(value = "/test_cases/create", params = {"plusNumber"})
    public String testCaseCreateUpStep(@ModelAttribute("testCase") TestCase testCase,
                                       @ModelAttribute("testSteps") TestStepsWrapper testSteps,
                                       @RequestParam Integer plusNumber,
                                       Model model) {
        List<TestStep> steps = testSteps.getTestSteps();
        if (plusNumber < testSteps.getTestSteps().size() - 1) {
            steps.get(plusNumber).setStepNumber(plusNumber + 1);
            steps.get(plusNumber + 1).setStepNumber(plusNumber);
        }
        testSteps.setTestSteps(steps);
        testSteps.sortSteps();
        return "test_case_creating";
    }

    @PostMapping(value = "/test_cases/create", params = {"minusNumber"})
    public String testCaseCreateDownStep(@ModelAttribute("testCase") TestCase testCase,
                                         @ModelAttribute("testSteps") TestStepsWrapper testSteps,
                                         @RequestParam Integer minusNumber,
                                         Model model) {
        List<TestStep> steps = testSteps.getTestSteps();
        if (minusNumber > 0) {
            steps.get(minusNumber).setStepNumber(minusNumber - 1);
            steps.get(minusNumber - 1).setStepNumber(minusNumber);
        }
        testSteps.setTestSteps(steps);
        testSteps.sortSteps();
        return "test_case_creating";
    }
}
