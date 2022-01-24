package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DataJpaRestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = ?1")
    int delete(long id);

    @EntityGraph(attributePaths = "dishes")
    @Query("SELECT r FROM Restaurant r JOIN r.dishes d WHERE r.id = ?1 AND d.servingDate = ?2")
    Restaurant getWithDishesByDate(long id, LocalDate date);

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN r.dishes d WHERE d.servingDate = ?1 ORDER BY r.name, r.address")
    List<Restaurant> getAllUpToDate(LocalDate date);

    @EntityGraph(attributePaths = "dishes")
    @Query("SELECT r FROM Restaurant r JOIN r.dishes d WHERE d.servingDate = ?1 ORDER BY r.name, r.address")
    List<Restaurant> getAllWithDishesByDate(LocalDate date);
}
