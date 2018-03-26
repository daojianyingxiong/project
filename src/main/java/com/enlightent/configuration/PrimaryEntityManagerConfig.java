package com.enlightent.configuration;

/**
* @author 作者 wr:
* @version 创建时间：2018年2月8日 下午12:08:31
* 类说明
*/
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef="primaryEntityManagerFactory",  
        transactionManagerRef="primaryTransactionManager",
        basePackages= { "com.enlightent.repository" })
public class PrimaryEntityManagerConfig {
      
    @Autowired  
    private JpaProperties jpaProperties;  
  
    @Autowired  
    @Qualifier("primaryDataSource")  
    private DataSource primaryDataSource;  
  
    @Bean(name = "entityManager")  
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {  
        return primaryEntityManagerFactory(builder).getObject().createEntityManager();  
    }  
  
    @Bean(name = "primaryEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {  
    	Map<String, String> vendorProperties = getVendorProperties(primaryDataSource);
    	LocalContainerEntityManagerFactoryBean build = builder
        .dataSource(primaryDataSource)  
        .properties(vendorProperties)  
        .packages("com.enlightent.entity")
        .persistenceUnit("entityManager")  
        .build();
        return  build; 
    }
  
    private Map<String, String> getVendorProperties(DataSource dataSource) {  
        return jpaProperties.getHibernateProperties(dataSource);  
    }  
  
    @Bean(name = "primaryTransactionManager")  
    @Primary  
    PlatformTransactionManager primaryTransactionManager(EntityManagerFactoryBuilder builder) {  
        return new JpaTransactionManager(primaryEntityManagerFactory(builder).getObject());  
    }  
  
}  
