package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface DataJpaRestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = ?1")
    int delete(long id);

    @EntityGraph(attributePaths = "meals", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r JOIN Meal m ON m.restaurant.id = r.id WHERE r.id = ?1 AND m.date = ?2")
    Restaurant getWithMealsByDate(long id, LocalDate date);
}
