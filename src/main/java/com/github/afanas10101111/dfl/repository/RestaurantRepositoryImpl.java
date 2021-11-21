package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private static final Sort SORT_NAME_ADDRESS = Sort.by(Sort.Direction.ASC, "name", "address");

    private final DataJpaRestaurantRepository repository;

    @Override
    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    @Override
    public boolean delete(long id) {
        return repository.delete(id) != 0;
    }

    @Override
    public Restaurant get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Restaurant getWithMealsByDate(long id, LocalDate date) {
        return repository.getWithMealsByDate(id, date);
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME_ADDRESS);
    }
}
