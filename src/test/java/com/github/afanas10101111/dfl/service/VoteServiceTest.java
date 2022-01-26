package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.BaseServiceTestClass;
import com.github.afanas10101111.dfl.ClockMockConfig;
import com.github.afanas10101111.dfl.RestaurantTestUtil;
import com.github.afanas10101111.dfl.UserTestUtil;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.exception.TooLateToRevoteException;
import com.github.afanas10101111.dfl.repository.VoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Clock;
import java.time.LocalDate;

import static com.github.afanas10101111.dfl.ErrorTestUtil.CORRECT_TIME;
import static com.github.afanas10101111.dfl.ErrorTestUtil.INCORRECT_DATE;
import static com.github.afanas10101111.dfl.ErrorTestUtil.INCORRECT_TIME;
import static com.github.afanas10101111.dfl.ErrorTestUtil.setClock;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.KFC_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.kfc;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.mcDonalds;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = ClockMockConfig.class)
@ExtendWith(MockitoExtension.class)
class VoteServiceTest extends BaseServiceTestClass {

    @Autowired
    private VoteService service;

    @Autowired
    private VoiceRepository repository;

    @Autowired
    private Clock clock;

    @Test
    void voteAndRevote() {
        setClock(clock, CORRECT_TIME);
        service.vote(USER_ID, MC_DONALDS_ID);
        assertEquals(mcDonalds, repository.getByUser(LocalDate.now(), user).getRestaurant());
        assertEquals(1, service.getVoicesCount(MC_DONALDS_ID));

        service.vote(USER_ID, KFC_ID);
        assertEquals(kfc, repository.getByUser(LocalDate.now(), user).getRestaurant());
        assertEquals(0, service.getVoicesCount(MC_DONALDS_ID));
        assertEquals(1, service.getVoicesCount(KFC_ID));

        service.vote(USER_ID, KFC_ID);
        assertEquals(kfc, repository.getByUser(LocalDate.now(), user).getRestaurant());

        setClock(clock, INCORRECT_DATE);
        service.vote(USER_ID, MC_DONALDS_ID);
        assertEquals(mcDonalds, repository.getByUser(INCORRECT_DATE.toLocalDate(), user).getRestaurant());
        assertEquals(1, service.getVoicesCount(MC_DONALDS_ID));
    }

    @Test
    void voteToLate() {
        setClock(clock, INCORRECT_TIME);
        service.vote(USER_ID, MC_DONALDS_ID);
        assertEquals(mcDonalds, repository.getByUser(LocalDate.now(), user).getRestaurant());
        assertEquals(1, service.getVoicesCount(MC_DONALDS_ID));

        setClock(clock, INCORRECT_TIME);
        assertThrows(TooLateToRevoteException.class, () -> service.vote(USER_ID, KFC_ID));
    }

    @Test
    void voteWithNaUserOrForNaRestaurant() {
        setClock(clock, CORRECT_TIME);
        assertThrows(NotFoundException.class, () -> service.vote(UserTestUtil.NA_ID, MC_DONALDS_ID));
        assertThrows(NotFoundException.class, () -> service.vote(USER_ID, RestaurantTestUtil.NA_ID));
    }
}
