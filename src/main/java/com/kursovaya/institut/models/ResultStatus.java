package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultStatus {
    DONE, FAIL, BROKEN_TEST
}
