package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Getter
public enum TestCaseStatus {
    IN_PROGRESS("В работе"),
    IS_READY("Готов");

    private final String name;
}
