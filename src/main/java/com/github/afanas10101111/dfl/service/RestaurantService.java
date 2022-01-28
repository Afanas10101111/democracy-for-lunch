package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.model.Dish;
import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.repository.RestaurantRepository;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {
    private static final String ASSERT_MESSAGE = "Argument can`t be null";
    private static final String RESTAURANT_WITH_DISHES = "restaurant-with-dishes";
    private static final String RESTAURANTS = "restaurants";
    private static final String ACTUAL_RESTAURANTS = "actual-restaurants";
    private static final String ACTUAL_RESTAURANTS_WITH_DISHES = "actual-restaurants-with-dishes";

    private final RestaurantRepository repository;

    @CacheEvict(cacheNames = {RESTAURANTS, ACTUAL_RESTAURANTS, ACTUAL_RESTAURANTS_WITH_DISHES}, allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, ASSERT_MESSAGE);
        restaurant.setDishes(null);
        return repository.save(restaurant);
    }

    @CacheEvict(cacheNames = {RESTAURANT_WITH_DISHES, RESTAURANTS, ACTUAL_RESTAURANTS, ACTUAL_RESTAURANTS_WITH_DISHES}, allEntries = true)
    @Transactional
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, ASSERT_MESSAGE);
        Restaurant restaurantFromDb = get(restaurant.id());

        restaurantFromDb.setName(restaurant.getName());
        restaurantFromDb.setAddress(restaurant.getAddress());
    }

    @CacheEvict(cacheNames = {RESTAURANT_WITH_DISHES, ACTUAL_RESTAURANTS, ACTUAL_RESTAURANTS_WITH_DISHES}, allEntries = true)
    @Transactional
    public void updateDishes(long id, Collection<Dish> dishes) {
        Assert.notNull(dishes, ASSERT_MESSAGE);
        Restaurant restaurantFromDb = get(id);
        repository.deleteDishByServingDate(restaurantFromDb, LocalDate.now());
        restaurantFromDb.setDishesForDate(dishes, LocalDate.now());
    }

    @CacheEvict(cacheNames = {RESTAURANT_WITH_DISHES, RESTAURANTS, ACTUAL_RESTAURANTS, ACTUAL_RESTAURANTS_WITH_DISHES}, allEntries = true)
    public void delete(long id) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(long id) {
        return ValidationUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Cacheable(RESTAURANT_WITH_DISHES)
    public Restaurant getWithDishes(long id) {
        return ValidationUtil.checkNotFoundWithId(repository.getWithDishesByDate(id, LocalDate.now()), id);
    }

    @Cacheable(RESTAURANTS)
    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    @Cacheable(ACTUAL_RESTAURANTS)
    public List<Restaurant> getAllUpToDate() {
        return repository.getAllUpToDate(LocalDate.now());
    }

    @Cacheable(ACTUAL_RESTAURANTS_WITH_DISHES)
    public List<Restaurant> getAllWithDishesUpToDate() {
        return repository.getAllWithDishesByDate(LocalDate.now());
    }
}
