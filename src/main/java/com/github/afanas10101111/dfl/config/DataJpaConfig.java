package com.github.afanas10101111.dfl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
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

import javax.cache.CacheManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.cache.jcache.ConfigSettings.CONFIG_URI;
import static org.hibernate.cache.jcache.ConfigSettings.MISSING_CACHE_STRATEGY;
import static org.hibernate.cache.jcache.ConfigSettings.PROVIDER;
import static org.hibernate.cfg.AvailableSettings.CACHE_REGION_FACTORY;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.JPA_PROXY_COMPLIANCE;
import static org.hibernate.cfg.AvailableSettings.USE_SECOND_LEVEL_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_SQL_COMMENTS;

@Configuration
@EnableTransactionManagement
@EnableCaching
@PropertySource("classpath:spring-db.properties")
@ComponentScan("com.github.afanas10101111.dfl.repository")
@EnableJpaRepositories("com.github.afanas10101111.dfl.repository")
public class DataJpaConfig {
    public static final String MODEL_PACKAGE = "com.github.afanas10101111.dfl.model";

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

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.cache_region_factory_class}")
    private String cacheRegionFactoryClass;

    @Value("${hibernate.cache_provider}")
    private String cacheProvider;

    @Value("${hibernate.missing_cache_strategy}")
    private String missingCacheStrategy;

    @Value("${hibernate.use_second_level_cache}")
    private Boolean useSecondLevelCache;

    @Value("${hibernate.hibernate_cache_config_location}")
    private String hibernateCacheConfigLocation;

    @Value("${hibernate.spring_cache_config_location}")
    private String springCacheConfigLocation;

    @Bean
    DataSource dataSource() throws IOException {
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
    EntityManagerFactory entityManagerFactory(DataSource dataSource) throws IOException {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan(MODEL_PACKAGE);

        Map<String, Object> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put(FORMAT_SQL, hibernateFormatSql);
        jpaPropertyMap.put(USE_SQL_COMMENTS, hibernateUseSqlComments);
        jpaPropertyMap.put(JPA_PROXY_COMPLIANCE, false);
        jpaPropertyMap.put(DIALECT, hibernateDialect);
        jpaPropertyMap.put(CACHE_REGION_FACTORY, cacheRegionFactoryClass);
        jpaPropertyMap.put(PROVIDER, cacheProvider);
        jpaPropertyMap.put(MISSING_CACHE_STRATEGY, missingCacheStrategy);
        jpaPropertyMap.put(USE_SECOND_LEVEL_CACHE, useSecondLevelCache);
        jpaPropertyMap.put(CONFIG_URI, new ClassPathResource(hibernateCacheConfigLocation).getURI().toString());
        factoryBean.setJpaPropertyMap(jpaPropertyMap);

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(hibernateShowSql);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    TransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    CacheManager jCacheManagerFactoryBean() throws IOException {
        JCacheManagerFactoryBean factory = new JCacheManagerFactoryBean();
        factory.setCacheManagerUri(new ClassPathResource(springCacheConfigLocation).getURI());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    JCacheCacheManager jCacheCacheManager(CacheManager factory) {
        return new JCacheCacheManager(factory);
    }
}
