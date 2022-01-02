package com.github.afanas10101111.dfl.web;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.ClockMockConfig;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.UserTestUtil;
import com.github.afanas10101111.dfl.dto.ErrorTo;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.service.VoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.LocalDate;

import static com.github.afanas10101111.dfl.ErrorTestUtil.CORRECT_TIME;
import static com.github.afanas10101111.dfl.ErrorTestUtil.INCORRECT_TIME;
import static com.github.afanas10101111.dfl.ErrorTestUtil.setClock;
import static com.github.afanas10101111.dfl.ErrorTestUtil.tooLateToVoteExceptionErrorTo;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = ClockMockConfig.class)
class VoteControllerTest extends BaseWebTestClass {
    private static final String URL = VoteController.URL;

    @Autowired
    private Clock clock;

    @Autowired
    private VoteService service;

    @Test
    void vote() throws Exception {
        setClock(clock, CORRECT_TIME);
        mockMvc.perform(
                MockMvcRequestBuilders.patch(URL)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword()))
                        .param("restaurantId", String.valueOf(MC_DONALDS_ID))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThat(restaurantService.get(MC_DONALDS_ID).getVoices()).isEqualTo(1);

        User user = userService.get(USER_ID);
        assertThat(user.getVotedForId()).isEqualTo(MC_DONALDS_ID);
        assertThat(user.getVoteDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void voteToLate() throws Exception {
        setClock(clock, INCORRECT_TIME);
        service.vote(USER_ID, MC_DONALDS_ID);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.patch(URL)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestUtil.user.getEmail(), UserTestUtil.user.getPassword()))
                        .param("restaurantId", String.valueOf(MC_DONALDS_ID))
        )
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();
        ErrorTo errorTo = JsonTestUtil.readValue(mapper, result, ErrorTo.class);
        assertEquals(tooLateToVoteExceptionErrorTo, errorTo);
    }
}
