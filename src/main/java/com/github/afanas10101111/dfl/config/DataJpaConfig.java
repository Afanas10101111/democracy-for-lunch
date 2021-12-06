package com.github.afanas10101111.dfl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.JPA_PROXY_COMPLIANCE;
import static org.hibernate.cfg.AvailableSettings.USE_SQL_COMMENTS;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:spring-db.properties")
@ComponentScan("com.github.afanas10101111.dfl.repository")
@EnableJpaRepositories("com.github.afanas10101111.dfl.repository")
public class DataJpaConfig {

    @Value("${jdbc.driver_class_name}")
    private String jdbcDriverClassName;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${jdbc.init_location}")
    private String initLocation;

    @Value("${jdbc.populate_location}")
    private String populateLocation;

    @Value("${hibernate.format_sql}")
    private Boolean hibernateFormatSql;

    @Value("${hibernate.use_sql_comments}")
    private Boolean hibernateUseSqlComments;

    @Value("${hibernate.show_sql}")
    private Boolean hibernateShowSql;

    @Bean
    public DataSource dataSource() throws IOException {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(jdbcDriverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new String(new ClassPathResource(initLocation).getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        stringBuilder.append(new String(new ClassPathResource(populateLocation).getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        dataSource.setInitSQL(stringBuilder.toString());
        return dataSource;
    }

    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.github.afanas10101111.dfl.model");

        Map<String, Boolean> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put(FORMAT_SQL, hibernateFormatSql);
        jpaPropertyMap.put(USE_SQL_COMMENTS, hibernateUseSqlComments);
        jpaPropertyMap.put(JPA_PROXY_COMPLIANCE, false);
        factoryBean.setJpaPropertyMap(jpaPropertyMap);

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(hibernateShowSql);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    @Autowired
    public TransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
