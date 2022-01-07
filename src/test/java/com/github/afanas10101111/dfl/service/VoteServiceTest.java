package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.BaseServiceTestClass;
import com.github.afanas10101111.dfl.ClockMockConfig;
import com.github.afanas10101111.dfl.RestaurantTestUtil;
import com.github.afanas10101111.dfl.UserTestUtil;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.exception.TooLateToRevoteException;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Clock;

import static com.github.afanas10101111.dfl.ErrorTestUtil.CORRECT_TIME;
import static com.github.afanas10101111.dfl.ErrorTestUtil.INCORRECT_DATE;
import static com.github.afanas10101111.dfl.ErrorTestUtil.INCORRECT_TIME;
import static com.github.afanas10101111.dfl.ErrorTestUtil.setClock;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.KFC_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.getVotedUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = ClockMockConfig.class)
@ExtendWith(MockitoExtension.class)
class VoteServiceTest extends BaseServiceTestClass {
    @Autowired
    private VoteService voteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private Clock clock;

    @Test
    void voteAndRevote() {
        setClock(clock, CORRECT_TIME);
        voteService.vote(USER_ID, MC_DONALDS_ID);
        User actual = userRepository.get(USER_ID);
        User expected = getVotedUser();
        USER_MATCHER.assertMatch(actual, expected);
        assertEquals(1, restaurantService.get(MC_DONALDS_ID).getVoices());

        voteService.vote(USER_ID, KFC_ID);
        actual = userRepository.get(USER_ID);
        expected.setVotedForId(KFC_ID);
        USER_MATCHER.assertMatch(actual, expected);
        assertEquals(0, restaurantService.get(MC_DONALDS_ID).getVoices());
        assertEquals(1, restaurantService.get(KFC_ID).getVoices());

        voteService.vote(USER_ID, KFC_ID);
        actual = userRepository.get(USER_ID);
        USER_MATCHER.assertMatch(actual, expected);
        assertEquals(1, restaurantService.get(KFC_ID).getVoices());

        setClock(clock, INCORRECT_DATE);
        voteService.vote(USER_ID, MC_DONALDS_ID);
        actual = userRepository.get(USER_ID);
        expected.setVotedForId(MC_DONALDS_ID);
        expected.setVoteDate(INCORRECT_DATE.toLocalDate());
        USER_MATCHER.assertMatch(actual, expected);
        assertEquals(1, restaurantService.get(MC_DONALDS_ID).getVoices());
    }

    @Test
    void voteToLate() {
        setClock(clock, INCORRECT_TIME);
        voteService.vote(USER_ID, MC_DONALDS_ID);
        User actual = userRepository.get(USER_ID);
        User expected = getVotedUser();
        USER_MATCHER.assertMatch(actual, expected);

        setClock(clock, INCORRECT_TIME);
        assertThrows(TooLateToRevoteException.class, () -> voteService.vote(USER_ID, KFC_ID));
    }

    @Test
    void voteWithNaUserOrForNaRestaurant() {
        setClock(clock, CORRECT_TIME);
        assertThrows(NotFoundException.class, () -> voteService.vote(UserTestUtil.NA_ID, MC_DONALDS_ID));
        assertThrows(NotFoundException.class, () -> voteService.vote(USER_ID, RestaurantTestUtil.NA_ID));
    }
}
