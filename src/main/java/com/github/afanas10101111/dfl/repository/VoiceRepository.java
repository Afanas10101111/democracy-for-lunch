package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.model.Voice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@RequiredArgsConstructor
@Repository
public class VoiceRepository {
    private final DataJpaVoiceRepository repository;

    public void save(Voice voice) {
        repository.save(voice);
    }

    public Voice getByUser(LocalDate date, User user) {
        return repository.getByDateAndUser(date, user);
    }

    public int getVoicesCountByDateAndRestaurant(LocalDate date, Restaurant restaurant) {
        return repository.getVoicesCountByDateAndRestaurant(date, restaurant);
    }
}
