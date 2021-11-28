package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.RestaurantTestUtil;
import com.github.afanas10101111.dfl.UserTestUtil;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.exception.TooLateToVoteException;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.KFC_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.getVotedUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest extends BaseServiceTestClass {
    private static final LocalDateTime CORRECT_TIME
            = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 59));
    private static final LocalDateTime INCORRECT_TIME
            = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));

    @InjectMocks
    private VoteService voteService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private Clock clock;

    @Autowired
    @Spy
    private UserService userService;

    @Autowired
    @Spy
    private RestaurantService restaurantService;

    @Test
    void voteAndRevote() {
        setClock(CORRECT_TIME);
        voteService.vote(USER_ID, MC_DONALDS_ID);
        User actual = userRepository.get(USER_ID);
        User expected = getVotedUser();
        USER_MATCHER.assertMatch(actual, expected);

        voteService.vote(USER_ID, KFC_ID);
        actual = userRepository.get(USER_ID);
        expected.setVotedForId(KFC_ID);
        USER_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void voteToLate() {
        setClock(INCORRECT_TIME);
        voteService.vote(USER_ID, MC_DONALDS_ID);
        User actual = userRepository.get(USER_ID);
        User expected = getVotedUser();
        USER_MATCHER.assertMatch(actual, expected);

        setClock(INCORRECT_TIME);
        assertThrows(TooLateToVoteException.class, () -> voteService.vote(USER_ID, KFC_ID));
    }

    @Test
    void voteWithNaUserOrForNaRestaurant() {
        setClock(CORRECT_TIME);
        assertThrows(NotFoundException.class, () -> voteService.vote(UserTestUtil.NA_ID, MC_DONALDS_ID));
        assertThrows(NotFoundException.class, () -> voteService.vote(USER_ID, RestaurantTestUtil.NA_ID));
    }

    private void setClock(LocalDateTime dateTime) {
        Clock fixedClock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }
}
