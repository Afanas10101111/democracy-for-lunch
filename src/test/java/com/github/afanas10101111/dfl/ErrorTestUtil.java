package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.dto.ErrorTo;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.NOW;
import static org.mockito.Mockito.doReturn;

public class ErrorTestUtil {
    public static final LocalDateTime CORRECT_TIME = LocalDateTime.of(NOW, LocalTime.of(10, 59));
    public static final LocalDateTime INCORRECT_TIME = LocalDateTime.of(NOW, LocalTime.of(11, 0));
    public static final LocalDateTime INCORRECT_DATE = LocalDateTime.of(NOW.plusDays(1), LocalTime.of(10, 0));

    public static final ErrorTo notFoundExceptionErrorTo
            = new ErrorTo(ErrorTo.ErrorType.DATA_ACCESS, "Not found entity with id = 0");
    public static final ErrorTo illegalArgumentExceptionErrorTo
            = new ErrorTo(ErrorTo.ErrorType.DATA_ACCESS, "Updated must be with id = 100001");
    public static final ErrorTo tooLateToVoteExceptionErrorTo
            = new ErrorTo(ErrorTo.ErrorType.VOTING, "Too late to revote! Voting ends at 10:59:59");
    public static final ErrorTo methodArgumentNotValidExceptionErrorTo
            = new ErrorTo(ErrorTo.ErrorType.BAD_REQUEST, "[dishToList.list[0].price,list[0].price] [must be between 1 and 10000000]");
    public static final ErrorTo restaurantBeanPropertyBindingResultErrorTo
            = new ErrorTo(ErrorTo.ErrorType.BAD_REQUEST, "[restaurantTo.name,name] [size must be between 2 and 100]");
    public static final ErrorTo userBeanPropertyBindingResultErrorTo
            = new ErrorTo(ErrorTo.ErrorType.BAD_REQUEST, "[userTo.enabled,enabled] [must not be null]");
    public static final ErrorTo accessDeniedExceptionErrorTo
            = new ErrorTo(ErrorTo.ErrorType.ACCESS, "Access is denied");
    public static final ErrorTo updateRestrictionExceptionErrorTo
            = new ErrorTo(ErrorTo.ErrorType.ACCESS, "Update is restricted for entity with id = 100001");

    public static void setClock(Clock clock, LocalDateTime dateTime) {
        Clock fixedClock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }
}
