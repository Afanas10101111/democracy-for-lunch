package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.config.DataJpaConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataJpaConfig.class)
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class BaseTestClass {
}
