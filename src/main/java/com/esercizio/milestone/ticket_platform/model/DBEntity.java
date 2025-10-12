package com.esercizio.milestone.ticket_platform.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;

@MappedSuperclass
public abstract class DBEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @SequenceGenerator(name = "idgenerator", initialValue = 0)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
