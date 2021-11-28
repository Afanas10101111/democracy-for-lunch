package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.config.DataJpaConfig;
import com.github.afanas10101111.dfl.config.ServiceConfig;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.afanas10101111.dfl.UserTestUtil.ADMIN_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.NA_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static com.github.afanas10101111.dfl.UserTestUtil.all;
import static com.github.afanas10101111.dfl.UserTestUtil.getNew;
import static com.github.afanas10101111.dfl.UserTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataJpaConfig.class, ServiceConfig.class})
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void create() {
        User saved = service.create(getNew());
        long id = saved.id();
        User expected = getNew();
        expected.setId(id);
        USER_MATCHER.assertMatch(saved, expected);
        USER_MATCHER.assertMatch(service.get(id), expected);
    }

    @Test
    public void update() {
        service.update(getUpdated());
        USER_MATCHER.assertMatch(service.getAll(), admin, getUpdated());
    }

    @Test
    public void createOrUpdateWithNull() {
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
    }

    @Test
    public void delete() {
        service.delete(ADMIN_ID);
        USER_MATCHER.assertMatch(service.getAll(), user);
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_ID));
    }

    @Test
    public void get() {
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), admin);
        assertThrows(NotFoundException.class, () -> service.get(NA_ID));
    }

    @Test
    public void getByEmail() {
        USER_MATCHER.assertMatch(service.getByEmail(user.getEmail()), user);
        assertThrows(NotFoundException.class, () -> service.getByEmail("someEmail"));
    }

    @Test
    public void getAll() {
        USER_MATCHER.assertMatch(service.getAll(), all);
    }
}
