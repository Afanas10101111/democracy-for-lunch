package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static com.github.afanas10101111.dfl.UserTestUtil.getNew;
import static com.github.afanas10101111.dfl.UserTestUtil.getTo;
import static com.github.afanas10101111.dfl.UserTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends BaseWebTestClass {
    private static final String URL = ProfileController.URL;
    private static final String REG = ProfileController.REG;

    @Test
    void get() throws Exception {
        USER_MATCHER.assertMatch(JsonTestUtil.readValue(mapper, getGetResult(URL), User.class), admin);
    }

    @Test
    void delete() throws Exception {
        assertDoesNotThrow(() -> userService.get(USER_ID));
        mockMvc.perform(
                MockMvcRequestBuilders.delete(URL)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword()))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(getTo(getUpdated())))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.get(USER_ID), getUpdated());
    }

    @Test
    void checkMinimalisticRegisterRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + REG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New\",\"email\":\"new@new.com\",\"password\":\"12345\",\"enabled\":true,\"roles\":[\"USER\"]}")
        )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void registerForbiddenForAuthenticated() throws Exception {
        User aNew = getNew();
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + REG)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(getTo(aNew)))
        )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
