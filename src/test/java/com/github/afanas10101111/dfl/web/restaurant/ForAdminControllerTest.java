package com.github.afanas10101111.dfl.web.restaurant;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.dto.ErrorTo;
import com.github.afanas10101111.dfl.dto.MealTo;
import com.github.afanas10101111.dfl.dto.RestaurantTo;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.afanas10101111.dfl.ErrorTestUtil.restaurantBeanPropertyBindingResultErrorTo;
import static com.github.afanas10101111.dfl.ErrorTestUtil.methodArgumentNotValidExceptionErrorTo;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MEAL_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_TO_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_TO_WITH_MEALS_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.aaNewPie;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.abNewPie;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.allTos;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.allTosWithActualMenu;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.bigMak;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.cheeseburger;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getMealTo;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getNew;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getTo;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getToWithMeals;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.hamburger;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.mcDonalds;
import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static com.github.afanas10101111.dfl.dto.ErrorTo.ErrorType.BAD_REQUEST;
import static com.github.afanas10101111.dfl.web.restaurant.ForAdminController.MEALS_SUFFIX;
import static com.github.afanas10101111.dfl.web.restaurant.ForAdminController.UP_TO_DATE_SUFFIX;
import static com.github.afanas10101111.dfl.web.restaurant.ForAdminController.URL;
import static com.github.afanas10101111.dfl.web.restaurant.ForAdminController.WITH_MEALS_SUFFIX;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ForAdminControllerTest extends BaseWebTestClass {
    private static final char SLASH = '/';

    @Test
    void createWithLocation() throws Exception {
        RestaurantTo expected = getTo(getNew());
        MvcResult result = getPostResult(URL, expected);
        RestaurantTo actual = JsonTestUtil.readValue(mapper, result, RestaurantTo.class);
        expected.setId(actual.getId());
        RESTAURANT_TO_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void update() throws Exception {
        performPut(URL + SLASH + MC_DONALDS_ID, getTo(getUpdated()));
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(MC_DONALDS_ID), getUpdated());
    }

    @Test
    void updateMeals() throws Exception {
        performPut(URL + SLASH + MC_DONALDS_ID + MEALS_SUFFIX, (List.of(getMealTo(aaNewPie), getMealTo(abNewPie))));
        MEAL_MATCHER.assertMatch(restaurantService.getWithMeals(MC_DONALDS_ID).getMeals(), aaNewPie, abNewPie, bigMak, cheeseburger, hamburger);
    }

    @Test
    void delete() throws Exception {
        assertDoesNotThrow(() -> restaurantService.get(MC_DONALDS_ID));
        performDelete(URL + SLASH + MC_DONALDS_ID);
        assertThrows(NotFoundException.class, () -> restaurantService.get(MC_DONALDS_ID));
    }

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
    void getAll() throws Exception {
        RESTAURANT_TO_MATCHER.assertMatch(
                JsonTestUtil.readValues(mapper, getGetResult(URL), RestaurantTo.class),
                allTos
        );
    }

    @Test
    void getAllUpToDate() throws Exception {
        RESTAURANT_TO_MATCHER.assertMatch(
                JsonTestUtil.readValues(mapper, getGetResult(URL + UP_TO_DATE_SUFFIX), RestaurantTo.class),
                allTosWithActualMenu
        );
    }

    @Test
    void getAllWithMealsUpToDate() throws Exception {
        RESTAURANT_TO_WITH_MEALS_MATCHER.assertMatch(
                JsonTestUtil.readValues(mapper, getGetResult(URL + WITH_MEALS_SUFFIX), RestaurantTo.class),
                allTosWithActualMenu
        );
    }

    @Test
    void createNotValid() throws Exception {
        Restaurant invalid = getNew();
        invalid.setName("a");
        checkValidation(URL, getTo(invalid), restaurantBeanPropertyBindingResultErrorTo);
    }

    @Test
    void addNotValidMeals() throws Exception {
        MealTo invalid = getMealTo(aaNewPie);
        invalid.setPrice(99);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put(URL + SLASH + MC_DONALDS_ID + MEALS_SUFFIX)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(List.of(invalid)))
        )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        ErrorTo errorTo = JsonTestUtil.readValue(mapper, result, ErrorTo.class);
        assertEquals(methodArgumentNotValidExceptionErrorTo, errorTo);
    }

    @Test
    void checkMinimalisticRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"aa\",\"address\":\"Neq York\"}")
        )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void checkRequestWithUnknownFields() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"aa\",\"address\":\"Neq York\",\"unknown\":\"field\"}")
        )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        ErrorTo errorTo = JsonTestUtil.readValue(mapper, result, ErrorTo.class);
        assertEquals(BAD_REQUEST, errorTo.getError());
    }
}
