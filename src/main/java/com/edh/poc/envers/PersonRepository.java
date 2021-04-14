package com.edh.poc.envers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

//RevisionRepository<Entity, IDType, RevisionNumber>
public interface PersonRepository extends RevisionRepository<Person, String, Long>, JpaRepository<Person, String> {
}
