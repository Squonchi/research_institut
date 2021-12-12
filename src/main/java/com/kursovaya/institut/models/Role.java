package com.kursovaya.institut.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Getter
public enum Role implements GrantedAuthority{
    USER, DIRECTOR, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
