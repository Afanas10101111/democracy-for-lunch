package com.github.afanas10101111.dfl.web.restaurant;

import com.github.afanas10101111.dfl.dto.RestaurantTo;
import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
abstract class BaseRestaurantController {

    @Autowired
    protected RestaurantService service;

    @Autowired
    protected ModelMapper mapper;

    public RestaurantTo get(long id) {
        log.info("get with id = {}", id);
        return getToWithoutMeals(service.get(id));
    }

    public RestaurantTo getWithMeals(long id) {
        log.info("getWithMeals with id = {}", id);
        return mapper.map(service.getWithMeals(id), RestaurantTo.class);
    }

    public List<RestaurantTo> getAllUpToDate() {
        log.info("getAllUpToDate");
        return service.getAllUpToDate().stream()
                .map(this::getToWithoutMeals)
                .collect(Collectors.toList());
    }

    public List<RestaurantTo> getAllWithMealsUpToDate() {
        log.info("getAllWithMealsByDate");
        return service.getAllWithMealsUpToDate().stream()
                .map(r -> mapper.map(r, RestaurantTo.class))
                .collect(Collectors.toList());
    }

    private RestaurantTo getToWithoutMeals(Restaurant restaurant) {
        restaurant.setMeals(null);
        RestaurantTo to = mapper.map(restaurant, RestaurantTo.class);
        to.setMeals(null);
        return to;
    }
}
