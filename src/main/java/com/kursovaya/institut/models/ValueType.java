package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValueType {
    INT("Число"), STR("Строка"), BOOL("Лог. значение");

    private final String name;
}
