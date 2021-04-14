package com.santander.tecnologica.poc.envers;

import org.javers.core.Javers;
import org.javers.core.MappingStyle;
import org.javers.hibernate.integration.HibernateUnproxyObjectAccessHook;
import org.javers.repository.sql.ConnectionProvider;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.javers.spring.jpa.JpaHibernateConnectionProvider;
import org.javers.spring.jpa.TransactionalJaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;

@Configuration
public class JaversConfig {

    @Bean
    public Javers javers(ConnectionProvider jpaHibernateConnectionProvider, PlatformTransactionManager platformTransactionManager) {
        JaversSqlRepository sqlRepository = SqlRepositoryBuilder.sqlRepository().withConnectionProvider(jpaHibernateConnectionProvider).withSchemaManagementEnabled(false).withDialect(DialectName.MYSQL).build();
        Javers javers = TransactionalJaversBuilder.javers().withTxManager(platformTransactionManager).withMappingStyle(MappingStyle.FIELD).withObjectAccessHook(new HibernateUnproxyObjectAccessHook()).registerJaversRepository(sqlRepository).build();
        return javers;
    }
}
