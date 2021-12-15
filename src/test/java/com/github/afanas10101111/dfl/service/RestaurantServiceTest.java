package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.BaseServiceTestClass;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MEALS_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.NA_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_WITH_MEALS_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.SUB_WAY_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.all;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.allWithActualMenu;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.bigMak;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.burgerKing;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.cheeseburger;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getNew;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getNewMeals;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.hamburger;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.kfc;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.mcDonalds;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.aNewPie;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.subWay;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantServiceTest extends BaseServiceTestClass {

    @Autowired
    private RestaurantService service;

    @Test
    void createAndGetWithMealsByDate() {
        Restaurant created = service.create(getNew());
        long createdId = created.id();
        long createdMealId = created.getMeals().stream()
                .findFirst()
                .orElseThrow()
                .id();
        Restaurant expected = getNew();
        expected.setId(createdId);
        expected.getMeals().stream()
                .findFirst()
                .orElseThrow()
                .setId(createdMealId);
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(created, expected);
        RESTAURANT_MATCHER.assertMatch(service.get(createdId), expected);
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(service.getWithMeals(createdId), expected);
    }

    @Test
    void update() {
        service.update(getUpdated());
        RESTAURANT_MATCHER.assertMatch(service.getAll(), burgerKing, kfc, getUpdated(), subWay);
    }

    @Test
    void updateNaAndNew() {
        Restaurant updated = getUpdated();
        updated.setId(NA_ID);
        assertThrows(NotFoundException.class, () -> service.update(updated));
        Restaurant aNew = getNew();
        assertThrows(IllegalArgumentException.class, () -> service.update(aNew));
    }

    @Test
    void updateMeals() {
        service.updateMeals(MC_DONALDS_ID, getNewMeals());
        MEALS_MATCHER.assertMatch(service.getWithMeals(MC_DONALDS_ID).getMeals(), aNewPie, bigMak, cheeseburger, hamburger);
    }

    @Test
    void createOrUpdateWithNull() {
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
    }

    @Test
    void delete() {
        service.delete(MC_DONALDS_ID);
        RESTAURANT_MATCHER.assertMatch(service.getAll(), burgerKing, kfc, subWay);
        assertThrows(NotFoundException.class, () -> service.delete(MC_DONALDS_ID));
    }

    @Test
    void get() {
        RESTAURANT_MATCHER.assertMatch(service.get(MC_DONALDS_ID), mcDonalds);
        assertThrows(NotFoundException.class, () -> service.get(NA_ID));
    }

    @Test
    void getWithOutOfDateMeals() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(SUB_WAY_ID));
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(service.getAll(), all);
    }

    @Test
    void getAllUpToDate() {
        RESTAURANT_MATCHER.assertMatch(service.getAllUpToDate(), allWithActualMenu);
    }

    @Test
    void getAllWithMealsUpToDate() {
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(service.getAllWithMealsUpToDate(), allWithActualMenu);
    }
}
