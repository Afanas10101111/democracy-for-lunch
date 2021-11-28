package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.exception.TooLateToVoteException;
import com.github.afanas10101111.dfl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class VoteService {
    public static final LocalTime REVOTE_CLOSING_TIME = LocalTime.of(10, 59);
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final Clock clock;

    public void vote(long userId, long restaurantId) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDate date = now.toLocalDate();
        User user = userService.get(userId);
        LocalDate voteDate = user.getVoteDate();
        if (voteDate != null && voteDate.isEqual(date) && now.toLocalTime().isAfter(REVOTE_CLOSING_TIME)) {
            throw new TooLateToVoteException();
        }
        // TODO think about storing voices in the restaurant
        restaurantService.get(restaurantId);
        user.setVoteDate(date);
        user.setVotedForId(restaurantId);
        userService.update(user);
    }
}
