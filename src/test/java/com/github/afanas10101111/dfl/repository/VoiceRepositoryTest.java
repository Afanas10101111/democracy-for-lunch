package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.BaseTestClass;
import com.github.afanas10101111.dfl.model.Voice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.kfc;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.mcDonalds;
import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoiceRepositoryTest extends BaseTestClass {

    @Autowired
    private VoiceRepository repository;

    private static final LocalDate NOW = LocalDate.now();

    @Test
    void save() {
        repository.save(new Voice(NOW, user, mcDonalds));
        Voice voice = repository.getByUser(NOW, user);
        assertEquals(mcDonalds, voice.getRestaurant());
    }

    @Test
    void saveConstraintViolation() {
        repository.save(new Voice(NOW, user, mcDonalds));
        Voice voice = new Voice(NOW, user, kfc);
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(voice));
    }

    @Test
    void getByUser() {
        assertNull(repository.getByUser(NOW, user));
        repository.save(new Voice(NOW.minusDays(1), user, mcDonalds));
        assertNull(repository.getByUser(NOW, user));
        repository.save(new Voice(NOW, user, mcDonalds));
        assertEquals(mcDonalds, repository.getByUser(NOW, user).getRestaurant());
    }

    @Test
    void getVoicesCountByDateAndRestaurant() {
        repository.save(new Voice(NOW.minusDays(1), user, mcDonalds));
        assertEquals(0, repository.getVoicesCountByDateAndRestaurant(NOW, mcDonalds));
        repository.save(new Voice(NOW, user, mcDonalds));
        repository.save(new Voice(NOW, admin, mcDonalds));
        assertEquals(2, repository.getVoicesCountByDateAndRestaurant(NOW, mcDonalds));
    }
}
