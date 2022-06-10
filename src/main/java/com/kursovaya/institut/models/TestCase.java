package com.kursovaya.institut.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.ORDINAL)
    private TestCaseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testCase")
    @JsonManagedReference
    private Set<TestStep> steps;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testCase")
    @JsonManagedReference
    private Set<Result> results;
}
