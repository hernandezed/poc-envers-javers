package com.edh.poc.envers;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "revinfo")
@RevisionEntity
public class CustomRevisionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "rev")
    private Long id;
    @RevisionTimestamp
    @Column(name = "revtstmp")
    private Long timestamp;

    public Long getId() {
        return id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
