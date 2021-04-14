package com.santander.tecnologica.poc.envers;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;


@JaversSpringDataAuditable
public interface PersonRepository extends JpaRepository<Person, String> {
}
