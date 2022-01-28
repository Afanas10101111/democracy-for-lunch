package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.exception.TooLateToRevoteException;
import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.model.Voice;
import com.github.afanas10101111.dfl.repository.RestaurantRepository;
import com.github.afanas10101111.dfl.repository.UserRepository;
import com.github.afanas10101111.dfl.repository.VoiceRepository;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class VoteService {
    public static final LocalTime REVOTE_CLOSING_TIME = LocalTime.of(10, 59, 59);

    private final VoiceRepository voiceRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final Clock clock;

    @Transactional
    public void vote(long userId, long restaurantId) {
        User user = ValidationUtil.checkNotFoundWithId(userRepository.get(userId), userId);
        Restaurant restaurant = ValidationUtil.checkNotFoundWithId(restaurantRepository.get(restaurantId), restaurantId);

        LocalDateTime now = LocalDateTime.now(clock);
        Voice voice = voiceRepository.getByUser(now.toLocalDate(), user);
        if (voice == null) {
            voiceRepository.save(new Voice(now.toLocalDate(), user, restaurant));
        } else if (now.toLocalTime().isAfter(REVOTE_CLOSING_TIME)) {
            throw new TooLateToRevoteException();
        } else if (!voice.getRestaurant().equals(restaurant)) {
            voice.setRestaurant(restaurant);
        }
    }

    public int getVoicesCount(long restaurantId) {
        Restaurant restaurant = ValidationUtil.checkNotFoundWithId(restaurantRepository.get(restaurantId), restaurantId);
        return voiceRepository.getVoicesCountByDateAndRestaurant(LocalDate.now(clock), restaurant);
    }
}
