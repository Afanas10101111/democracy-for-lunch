package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER_FOR_PASSWORDLESS_FROM_TOS;
import static com.github.afanas10101111.dfl.UserTestUtil.getTo;
import static com.github.afanas10101111.dfl.UserTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends BaseWebTestClass {
    private static final String URL = ProfileController.URL;

    @Test
    void get() throws Exception {
        USER_MATCHER_FOR_PASSWORDLESS_FROM_TOS.assertMatch(JsonTestUtil.readValue(mapper, getGetResult(URL), User.class), user);
    }

    @Test
    void delete() throws Exception {
        assertDoesNotThrow(() -> userService.get(USER_ID));
        mockMvc.perform(MockMvcRequestBuilders.delete(URL))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(getTo(getUpdated())))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.get(USER_ID), getUpdated());
    }
}
