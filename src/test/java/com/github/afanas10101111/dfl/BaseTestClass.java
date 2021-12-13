package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.config.DataJpaConfig;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = DataJpaConfig.class)
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class BaseTestClass {
}
