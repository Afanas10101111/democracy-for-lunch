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

    private final DataJpaRestaurantRepository rRepository;
    private final DataJpaDishRepository dRepository;

    public Restaurant save(Restaurant restaurant) {
        return rRepository.save(restaurant);
    }

    public boolean delete(long id) {
        return rRepository.delete(id) != 0;
    }

    public Restaurant get(long id) {
        return rRepository.findById(id).orElse(null);
    }

    public Restaurant getWithDishesByDate(long id, LocalDate date) {
        return rRepository.getWithDishesByDate(id, date);
    }

    public List<Restaurant> getAll() {
        return rRepository.findAll(SORT_NAME_ADDRESS);
    }

    public List<Restaurant> getAllUpToDate(LocalDate date) {
        return rRepository.getAllUpToDate(date);
    }

    public List<Restaurant> getAllWithDishesByDate(LocalDate date) {
        return rRepository.getAllWithDishesByDate(date);
    }

    public void deleteDishByServingDate(LocalDate servingDate) {
        dRepository.deleteByServingDate(servingDate);
    }
}
