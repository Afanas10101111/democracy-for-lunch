package com.github.afanas10101111.dfl.web;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends BaseWebTestClass {
    private static final String URL = VoteController.URL;

    @Test
    void vote() throws Exception {
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
}
