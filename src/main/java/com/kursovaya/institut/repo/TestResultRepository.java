package com.kursovaya.institut.repo;

import com.kursovaya.institut.models.Result;
import com.kursovaya.institut.models.TestCase;
import com.kursovaya.institut.models.TestStep;
import org.springframework.data.repository.CrudRepository;

public interface TestResultRepository extends CrudRepository<Result, Long> {
}
