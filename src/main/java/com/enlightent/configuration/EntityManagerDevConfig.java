package com.enlightent.configuration;

/**
* @author 作者 wr:
* @version 创建时间：2018年2月8日 下午12:08:31
* 类说明
*/
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;  
/** 
 * @author  
 * 
 */  
@Configuration  
@EnableTransactionManagement  
@EnableJpaRepositories(  
        entityManagerFactoryRef="entityManagerFactoryDev",  
        transactionManagerRef="transactionManagerDev" )
public class EntityManagerDevConfig {  
      
    @Autowired  
    private JpaProperties jpaProperties;  
  
    @Autowired  
    @Qualifier("devDataSource")  
    private DataSource devDataSource;  
  
    @Bean(name = "entityManagerDev")  
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {  
        return entityManagerFactoryDev(builder).getObject().createEntityManager();  
    }  
  
    @Bean(name = "entityManagerFactoryDev")  
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryDev (EntityManagerFactoryBuilder builder) {  
        return  builder
                .dataSource(devDataSource)  
                .properties(getVendorProperties(devDataSource))  
                .packages("com.enlightent.dev")
                .persistenceUnit("devPersistenceUnit")  
                .build(); 
    }  
  
    private Map<String, String> getVendorProperties(DataSource dataSource) {  
        return jpaProperties.getHibernateProperties(dataSource);  
    }  
  
    @Bean(name = "transactionManagerDev")  
    PlatformTransactionManager transactionManagerDev(EntityManagerFactoryBuilder builder) {  
        return new JpaTransactionManager(entityManagerFactoryDev(builder).getObject());  
    }  
  
}  
