package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.BaseTestClass;
import com.github.afanas10101111.dfl.model.Meal;
import com.github.afanas10101111.dfl.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.NOW;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.NA_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_WITH_MEALS_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.all;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.allWithActualMenu;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getNew;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.hamburger;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.mcDonalds;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantRepositoryTest extends BaseTestClass {

    @Autowired
    private RestaurantRepository repository;

    @Test
    void save() {
        Restaurant saved = repository.save(getNew());
        long savedId = saved.id();
        Restaurant expectedSaved = getNew();
        expectedSaved.setId(savedId);
        expectedSaved.setMeals(saved.getMeals());
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(saved, expectedSaved);
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(repository.getWithMealsByDate(savedId, LocalDate.now()), expectedSaved);

        Restaurant updatedFromDb = repository.save(getUpdated());
        Restaurant expectedUpdated = getUpdated();
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(updatedFromDb, expectedUpdated);
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(repository.getWithMealsByDate(updatedFromDb.id(), LocalDate.now()), expectedUpdated);
    }

    @Test
    void saveConstraintViolation() {
        Restaurant doubleRestaurant = getNew();
        doubleRestaurant.setName(mcDonalds.getName());
        doubleRestaurant.setAddress(mcDonalds.getAddress());
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(doubleRestaurant));

        Restaurant doubleMeal = getNew();
        doubleMeal.setMeals(List.of(
                new Meal(hamburger.getName(), hamburger.getPrice()),
                new Meal(hamburger.getName(), hamburger.getPrice())
        ));
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(doubleMeal));
    }

    @Test
    void delete() {
        assertTrue(repository.delete(MC_DONALDS_ID));
        assertNull(repository.get(MC_DONALDS_ID));
        assertFalse(repository.delete(MC_DONALDS_ID));
    }

    @Test
    void get() {
        RESTAURANT_MATCHER.assertMatch(repository.get(MC_DONALDS_ID), mcDonalds);
    }

    @Test
    void getNa() {
        assertNull(repository.get(NA_ID));
    }

    @Test
    void getWithMealsByDate() {
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(repository.getWithMealsByDate(MC_DONALDS_ID, NOW), mcDonalds);
    }

    @Test
    void getWithOutOfDateMeals() {
        assertNull(repository.getWithMealsByDate(MC_DONALDS_ID, NOW.minusDays(1)));
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(repository.getAll(), all);
    }

    @Test
    void getAllUpToDate() {
        RESTAURANT_MATCHER.assertMatch(repository.getAllUpToDate(NOW), allWithActualMenu);
    }

    @Test
    void getAllWithMealsByDate() {
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(repository.getAllWithMealsByDate(NOW), allWithActualMenu);
    }
}
