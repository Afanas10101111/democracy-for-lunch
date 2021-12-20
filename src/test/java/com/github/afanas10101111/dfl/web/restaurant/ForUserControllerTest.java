package com.github.afanas10101111.dfl.web.restaurant;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.dto.RestaurantTo;
import org.junit.jupiter.api.Test;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_TO_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_TO_WITH_MEALS_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.allTosWithActualMenu;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getToWithMeals;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.mcDonalds;
import static com.github.afanas10101111.dfl.web.restaurant.ForUserController.URL;
import static com.github.afanas10101111.dfl.web.restaurant.ForUserController.WITH_MEALS_SUFFIX;

class ForUserControllerTest extends BaseWebTestClass {
    private static final char SLASH = '/';

    @Test
    void get() throws Exception {
        RESTAURANT_TO_MATCHER.assertMatch(
                JsonTestUtil.readValue(mapper, getGetResult(URL + SLASH + MC_DONALDS_ID), RestaurantTo.class),
                getToWithMeals(mcDonalds)
        );
    }

    @Test
    void getWithMeals() throws Exception {
        RESTAURANT_TO_WITH_MEALS_MATCHER.assertMatch(
                JsonTestUtil.readValue(mapper, getGetResult(URL + WITH_MEALS_SUFFIX + SLASH + MC_DONALDS_ID), RestaurantTo.class),
                getToWithMeals(mcDonalds)
        );
    }

    @Test
    void getAllUpToDate() throws Exception {
        RESTAURANT_TO_MATCHER.assertMatch(
                JsonTestUtil.readValues(mapper, getGetResult(URL), RestaurantTo.class),
                allTosWithActualMenu
        );
    }

    @Test
    void getAllWithMealsByDate() throws Exception {
        RESTAURANT_TO_WITH_MEALS_MATCHER.assertMatch(
                JsonTestUtil.readValues(mapper, getGetResult(URL + WITH_MEALS_SUFFIX), RestaurantTo.class),
                allTosWithActualMenu
        );
    }
}
