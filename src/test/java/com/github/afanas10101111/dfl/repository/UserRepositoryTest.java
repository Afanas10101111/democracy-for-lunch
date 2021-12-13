package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.BaseTestClass;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static com.github.afanas10101111.dfl.UserTestUtil.NA_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.all;
import static com.github.afanas10101111.dfl.UserTestUtil.getNew;
import static com.github.afanas10101111.dfl.UserTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest extends BaseTestClass {

    @Autowired
    private UserRepository repository;

    @Test
    void save() {
        User saved = repository.save(getNew());
        long savedId = saved.id();
        User expectedSaved = getNew();
        expectedSaved.setId(savedId);
        USER_MATCHER.assertMatch(saved, expectedSaved);
        USER_MATCHER.assertMatch(repository.get(savedId), expectedSaved);

        User updatedFromDb = repository.save(getUpdated());
        long updatedId = updatedFromDb.id();
        User expectedUpdated = getUpdated();
        USER_MATCHER.assertMatch(updatedFromDb, expectedUpdated);
        USER_MATCHER.assertMatch(repository.get(updatedId), expectedUpdated);
    }

    @Test
    void saveConstraintViolation() {
        User aNew = getNew();
        aNew.setEmail(user.getEmail());
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(aNew));
    }

    @Test
    void delete() {
        assertTrue(repository.delete(USER_ID));
        assertNull(repository.get(USER_ID));
        assertFalse(repository.delete(USER_ID));
    }

    @Test
    void get() {
        USER_MATCHER.assertMatch(repository.get(USER_ID), user);
    }

    @Test
    void getNotExisted() {
        assertNull(repository.get(NA_ID));
    }

    @Test
    void getByEmail() {
        USER_MATCHER.assertMatch(repository.getByEmail(user.getEmail()), user);
    }

    @Test
    void getAll() {
        USER_MATCHER.assertMatch(repository.getAll(), all);
    }
}
