package com.edh.poc.envers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.data.history.Revisions;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Testcontainers
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
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
    }

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void savePerson() {
        Person person = new Person("Eduardo", "Hernandez");
        Person newPerson = personRepository.save(person);

        Person updatePerson = new Person("Daniel", "Hernandez");
        updatePerson.setUuid(newPerson.getUuid());
        updatePerson = personRepository.save(updatePerson);

        assertThat(newPerson).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(person);
        assertThat(newPerson).usingRecursiveComparison().ignoringExpectedNullFields().isNotEqualTo(updatePerson);
        assertThat(newPerson.getUuid()).isEqualTo(updatePerson.getUuid());

        Revisions<Long, Person> personRevisions = personRepository.findRevisions(updatePerson.getUuid());
        assertThat(personRevisions.getContent()).hasSize(2)
                .extracting(rev -> tuple(rev.getEntity().getUuid(), rev.getEntity().getName(), rev.getEntity().getSurname(), rev.getMetadata().getRevisionType()))
                .containsExactlyInAnyOrder(tuple(newPerson.getUuid(), newPerson.getName(), newPerson.getSurname(), RevisionMetadata.RevisionType.INSERT),
                        tuple(updatePerson.getUuid(), updatePerson.getName(), updatePerson.getSurname(), RevisionMetadata.RevisionType.UPDATE));

        Revision<Long, Person> lastPersonRevision = personRepository.findLastChangeRevision(updatePerson.getUuid()).get();

        assertThat(lastPersonRevision.getMetadata().getRevisionType())
                .usingRecursiveComparison()
                .isEqualTo(RevisionMetadata.RevisionType.UPDATE);
        assertThat(lastPersonRevision.getEntity())
                .usingRecursiveComparison()
                .isEqualTo(updatePerson);

        Revision<Long, Person> firstRevision = personRepository.findRevision(updatePerson.getUuid(), 1l).get();
        assertThat(firstRevision.getMetadata().getRevisionType())
                .usingRecursiveComparison()
                .isEqualTo(RevisionMetadata.RevisionType.INSERT);
        assertThat(firstRevision.getEntity())
                .usingRecursiveComparison()
                .isEqualTo(newPerson);


    }
}
