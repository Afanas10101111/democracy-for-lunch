package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.config.DataJpaConfig;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.cache.CacheManager;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringJUnitConfig(classes = DataJpaConfig.class)
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class BaseTestClass {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CacheManager cm;

    @BeforeEach
    public void clear2ndLevelHibernateCache() {
        Session session = (Session) em.getDelegate();
        session.getSessionFactory().getCache().evictAllRegions();
        cm.getCacheNames()
                .forEach(c -> cm.getCache(c).clear());
    }
}
