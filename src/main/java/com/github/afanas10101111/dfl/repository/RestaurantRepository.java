package com.github.afanas10101111.dfl.repository;


import com.github.afanas10101111.dfl.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);

    boolean delete(long id);

    Restaurant get(long id);

    Restaurant getWithMealsByDate(long id, LocalDate date);

    List<Restaurant> getAll();
}
