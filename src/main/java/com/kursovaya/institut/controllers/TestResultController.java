package com.kursovaya.institut.controllers;

import com.kursovaya.institut.models.Result;
import com.kursovaya.institut.models.TestCase;
import com.kursovaya.institut.models.TestResultsWrapper;
import com.kursovaya.institut.repo.TestCaseRepository;
import com.kursovaya.institut.repo.TestResultRepository;
import com.kursovaya.institut.repo.TestStepRepository;
import com.kursovaya.institut.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestResultController {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestStepRepository testStepRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @GetMapping("/test_results")
    public String testResultsMain(Model model) {
        Iterable<Result> testResults = testResultRepository.findAll();
        TestResultsWrapper testResultList = new TestResultsWrapper(testResults);
        testResultList.sortTestResults();
        model.addAttribute("testResults", testResultList.getResultList());
        return "test_results";
    }
}
