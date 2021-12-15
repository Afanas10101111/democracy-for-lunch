package com.github.afanas10101111.dfl.web.restaurant;

import com.github.afanas10101111.dfl.dto.RestaurantTo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantForUserController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantForUserController extends BaseRestaurantController {
    public static final String URL = "/restaurants";
    public static final String WITH_MEALS_SUFFIX = "/with-meals";

    @Override
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable long id) {
        return super.get(id);
    }

    @Override
    @GetMapping(WITH_MEALS_SUFFIX + "/{id}")
    public RestaurantTo getWithMeals(@PathVariable long id) {
        return super.getWithMeals(id);
    }

    @Override
    @GetMapping
    public List<RestaurantTo> getAllUpToDate() {
        return super.getAllUpToDate();
    }

    @Override
    @GetMapping(WITH_MEALS_SUFFIX)
    public List<RestaurantTo> getAllWithMealsUpToDate() {
        return super.getAllWithMealsUpToDate();
    }
}
