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
        return getTo(service.get(id));
    }

    public RestaurantTo getWithMeals(long id) {
        log.info("getWithMeals with id = {}", id);
        return getToWithMeals(service.getWithMeals(id));
    }

    public List<RestaurantTo> getAllUpToDate() {
        log.info("getAllUpToDate");
        return getTos(service.getAllUpToDate());
    }

    public List<RestaurantTo> getAllWithMealsUpToDate() {
        log.info("getAllWithMealsByDate");
        return getTosWithMeals(service.getAllWithMealsUpToDate());
    }

    protected RestaurantTo getToWithMeals(Restaurant restaurant) {
        return mapper.map(restaurant, RestaurantTo.class);
    }

    protected List<RestaurantTo> getTosWithMeals(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::getToWithMeals)
                .collect(Collectors.toList());
    }

    protected RestaurantTo getTo(Restaurant restaurant) {
        restaurant.setMeals(null);
        RestaurantTo to = getToWithMeals(restaurant);
        to.setMeals(null);
        return to;
    }

    protected List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::getTo)
                .collect(Collectors.toList());
    }
}
