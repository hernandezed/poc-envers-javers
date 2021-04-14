package com.edh.poc.envers;

import org.javers.core.Javers;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Testcontainers
@EnableJpaAuditing
@EnableJpaRepositories
class EnversApplicationTests {

    @Container
    static MariaDBContainer mariadb = (MariaDBContainer) new MariaDBContainer("mariadb")
            .withExposedPorts(3306)
            .waitingFor(Wait.forListeningPort());


    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> mariadb.getJdbcUrl());
        registry.add("spring.datasource.username", () -> mariadb.getUsername());
        registry.add("spring.datasource.password", () -> mariadb.getPassword());
        registry.add("spring.datasource.driver-class-name", () -> mariadb.getDriverClassName());
        registry.add("spring.datasource.driver-class-name", () -> mariadb.getDriverClassName());
        registry.add("spring.datasource.driver-class-name", () -> mariadb.getDriverClassName());
        registry.add("spring.datasource.driver-class-name", () -> mariadb.getDriverClassName());
        registry.add("spring.datasource.driver-class-name", () -> mariadb.getDriverClassName());
    }

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    Javers javers;

    @Test
    @Transactional
    void savePerson() throws JsonProcessingException {
        Person person = new Person("Eduardo", "Hernandez");
        Person newPerson = personRepository.save(person);

        Person updatePerson = new Person("Daniel", "Hernandez");
        updatePerson.setUuid(newPerson.getUuid());
        updatePerson = personRepository.save(updatePerson);

        assertThat(javers.findChanges(QueryBuilder.byInstanceId(newPerson.getUuid(),Person.class).build()).size())
                .isEqualTo(5);


    }
}
