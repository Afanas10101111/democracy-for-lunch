package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.afanas10101111.dfl.UserTestUtil.ADMIN_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.NA_EMAIL;
import static com.github.afanas10101111.dfl.UserTestUtil.NA_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static com.github.afanas10101111.dfl.UserTestUtil.all;
import static com.github.afanas10101111.dfl.UserTestUtil.getNew;
import static com.github.afanas10101111.dfl.UserTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest extends BaseServiceTestClass {

    @Autowired
    private UserService service;

    @Test
    void create() {
        User created = service.create(getNew());
        long id = created.id();
        User expected = getNew();
        expected.setId(id);
        USER_MATCHER.assertMatch(created, expected);
        USER_MATCHER.assertMatch(service.get(id), expected);
    }

    @Test
    void update() {
        service.update(getUpdated());
        USER_MATCHER.assertMatch(service.getAll(), admin, getUpdated());
    }

    @Test
    void updateNaAndNew() {
        User updated = getUpdated();
        updated.setId(NA_ID);
        assertThrows(NotFoundException.class, () -> service.update(updated));
        User aNew = getNew();
        assertThrows(IllegalArgumentException.class, () -> service.update(aNew));
    }

    @Test
    void createOrUpdateWithNull() {
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
    }

    @Test
    void delete() {
        service.delete(ADMIN_ID);
        USER_MATCHER.assertMatch(service.getAll(), user);
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_ID));
    }

    @Test
    void get() {
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), admin);
        assertThrows(NotFoundException.class, () -> service.get(NA_ID));
    }

    @Test
    void getByEmail() {
        USER_MATCHER.assertMatch(service.getByEmail(user.getEmail()), user);
        assertThrows(NotFoundException.class, () -> service.getByEmail(NA_EMAIL));
    }

    @Test
    void getAll() {
        USER_MATCHER.assertMatch(service.getAll(), all);
    }
}
