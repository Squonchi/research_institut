package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
public enum ReportStatus {
    IN_PROGRESS("В работе"),
    FOR_CORRECTION("На исправлении"),
    IS_READY("Готов"),
    WAITING_APPOINTMENT("Ожидает назначения"),
    ON_INSPECTION("Отправлен на проверку");

    private final String name;
}
