package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.model.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface DataJpaVoiceRepository extends JpaRepository<Voice, Long> {

    @Query("SELECT v FROM Voice v WHERE v.votingDate = ?1 AND v.user = ?2")
    Voice getByDateAndUser(LocalDate date, User user);

    @Query("SELECT COUNT (v) FROM Voice v WHERE v.votingDate = ?1 AND v.restaurant = ?2")
    int getVoicesCountByDateAndRestaurant(LocalDate date, Restaurant restaurant);
}
