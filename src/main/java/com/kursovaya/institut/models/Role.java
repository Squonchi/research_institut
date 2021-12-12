package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority{
    USER("Сотрудник"), DIRECTOR("Руководитель"), ADMIN("Администратор");

    private final String name;

    @Override
    public String getAuthority() {
        return name();
    }
}
