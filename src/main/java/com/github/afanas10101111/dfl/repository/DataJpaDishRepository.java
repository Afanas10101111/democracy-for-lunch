package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.Dish;
import com.github.afanas10101111.dfl.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface DataJpaDishRepository extends JpaRepository<Dish, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.restaurant = ?1 AND d.servingDate = ?2")
    void deleteByServingDate(Restaurant restaurant, LocalDate servingDate);
}
