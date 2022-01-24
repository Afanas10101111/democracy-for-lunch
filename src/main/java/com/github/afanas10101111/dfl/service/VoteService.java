package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.exception.TooLateToRevoteException;
import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class VoteService {
    public static final LocalTime REVOTE_CLOSING_TIME = LocalTime.of(10, 59, 59);
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final Clock clock;

    @Transactional
    public void vote(long userId, long restaurantId) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDate nowDate = now.toLocalDate();

        User user = userService.get(userId);
        boolean votedToday = Objects.equals(user.getVoteDate(), nowDate);
        boolean sameChoice = Objects.equals(user.getVotedForId(), restaurantId);
        if (votedToday && now.toLocalTime().isAfter(REVOTE_CLOSING_TIME)) {
            throw new TooLateToRevoteException();
        } else if (votedToday && sameChoice) {
            return;
        }

        Restaurant restaurant = restaurantService.get(restaurantId);
        if (!sameChoice && votedToday) {
            Restaurant previousChoice = restaurantService.get(user.getVotedForId());
            previousChoice.removeVoice();
        }
        restaurant.addVoice();
        user.setVotedForId(restaurantId);
        user.setVoteDate(nowDate);
    }
}
