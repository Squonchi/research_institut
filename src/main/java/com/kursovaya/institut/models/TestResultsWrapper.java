package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class TestResultsWrapper {
    private List<Result> resultList;

    public TestResultsWrapper(Iterable<Result> resultList) {
        this.resultList = new ArrayList<>();
//        while (resultList.iterator().hasNext()) {
//            this.resultList.add(resultList.iterator().next());
//        }
        resultList.forEach(this.resultList::add);
        sortTestResults();
    }

    public void sortTestResults() {
        if (resultList.size() > 0) {
            resultList.sort(new ResultsComparator());
        }
    }
}

class ResultsComparator implements Comparator<Result> {
    @Override
    public int compare(Result o1, Result o2) {
        return o1.getRunDateTime().isAfter(o2.getRunDateTime()) ? -1 : Objects.equals(o1.getRunDateTime(), o2.getRunDateTime()) ? 0 : 1;
    }
}