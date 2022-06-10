package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TestStepsWrapper {
    private List<TestStep> testSteps;

    public TestStepsWrapper() {
        testSteps = new ArrayList<>();
    }

    public TestStepsWrapper(Set<TestStep> steps) {
        testSteps = new ArrayList<>(steps);
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public void sortSteps() {
        if (testSteps.size() > 0) {
            testSteps.sort(new NumberComparator());
            for (int i = 0; i < testSteps.size(); i++)
                testSteps.get(i).setStepNumber(i);
        }
    }

    public void add(TestStep testStep) {
        testSteps.add(testStep);
        sortSteps();
    }

    public void removeStep(Integer index) {
        testSteps.remove(index.intValue());
        sortSteps();
    }
}

class NumberComparator implements Comparator<TestStep> {
    @Override
    public int compare(TestStep o1, TestStep o2) {
        return o1.getStepNumber() < o2.getStepNumber() ? -1 : Objects.equals(o1.getStepNumber(), o2.getStepNumber()) ? 0 : 1;
    }
}
