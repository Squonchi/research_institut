package com.kursovaya.institut.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String anons;
    private String full_text;
    private String comment;
    @Enumerated(EnumType.ORDINAL)
    private ReportStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;


    public Report() {
    }

    public Report(String title) {
        this.title = title;
        this.status = ReportStatus.WAITING_APPOINTMENT;
    }

    public Report(String title, String anons, String full_text, User user) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.author = user;
        this.status = ReportStatus.WAITING_APPOINTMENT;
    }


}
