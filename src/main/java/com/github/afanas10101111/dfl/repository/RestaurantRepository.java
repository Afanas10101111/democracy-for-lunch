package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class RestaurantRepository {
    private static final Sort SORT_NAME_ADDRESS = Sort.by(Sort.Direction.ASC, "name", "address");

    private final DataJpaRestaurantRepository repository;

    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public boolean delete(long id) {
        return repository.delete(id) != 0;
    }

    public Restaurant get(long id) {
        return repository.findById(id).orElse(null);
    }

    public Restaurant getWithDishesByDate(long id, LocalDate date) {
        return repository.getWithDishesByDate(id, date);
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME_ADDRESS);
    }

    public List<Restaurant> getAllUpToDate(LocalDate date) {
        return repository.getAllUpToDate(date);
    }

    public List<Restaurant> getAllWithDishesByDate(LocalDate date) {
        return repository.getAllWithDishesByDate(date);
    }
}
