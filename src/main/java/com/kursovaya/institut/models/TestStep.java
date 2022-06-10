package com.kursovaya.institut.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestStep {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String body;
    private Long expStatusCode;
    @Enumerated(EnumType.ORDINAL)
    private HttpMethod httpMethod;
    private String valuePath;
    private String value;
    private String url;
    @Enumerated(EnumType.ORDINAL)
    private Condition condition;
    @Enumerated(EnumType.ORDINAL)
    private ValueType valueType;
    private Integer stepNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_case_id", nullable = false)
    @JsonBackReference
    private TestCase testCase;
}
