package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.repository.RestaurantRepository;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {
    private static final String ASSERT_MESSAGE = "Argument can`t be null";

    private final RestaurantRepository repository;

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, ASSERT_MESSAGE);
        return repository.save(restaurant);
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, ASSERT_MESSAGE);
        ValidationUtil.checkNotFoundWithId(repository.save(restaurant), restaurant.id());
    }

    public void delete(long id) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(long id) {
        return ValidationUtil.checkNotFoundWithId(repository.get(id), id);
    }

    public Restaurant getWithMeals(long id) {
        return ValidationUtil.checkNotFoundWithId(repository.getWithMealsByDate(id, LocalDate.now()), id);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }
}
