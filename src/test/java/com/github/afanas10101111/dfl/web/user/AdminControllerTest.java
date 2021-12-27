package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_MATCHER_FOR_PASSWORDLESS_FROM_TOS;
import static com.github.afanas10101111.dfl.UserTestUtil.all;
import static com.github.afanas10101111.dfl.UserTestUtil.getNew;
import static com.github.afanas10101111.dfl.UserTestUtil.getTo;
import static com.github.afanas10101111.dfl.UserTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.UserTestUtil.user;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends BaseWebTestClass {
    private static final String URL = AdminController.URL + '/';

    @Test
    void getAll() throws Exception {
        USER_MATCHER_FOR_PASSWORDLESS_FROM_TOS.assertMatch(
                JsonTestUtil.readValues(mapper, getGetResult(URL), User.class), all
        );
    }

    @Test
    void get() throws Exception {
        USER_MATCHER_FOR_PASSWORDLESS_FROM_TOS.assertMatch(
                JsonTestUtil.readValue(mapper, getGetResult(URL + USER_ID), User.class), user
        );
    }

    @Test
    void getByEmail() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "user@yandex.ru");
        USER_MATCHER_FOR_PASSWORDLESS_FROM_TOS.assertMatch(
                JsonTestUtil.readValue(mapper, getGetResult(URL + "by-email", params), User.class), user
        );
    }

    @Test
    void delete() throws Exception {
        assertDoesNotThrow(() -> userService.get(USER_ID));
        performDelete(URL + USER_ID);
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void enable() throws Exception {
        assertTrue(userService.get(USER_ID).isEnabled());
        mockMvc.perform(MockMvcRequestBuilders.patch(URL + USER_ID).param("enable", "false"))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(userService.get(USER_ID).isEnabled());
    }

    @Test
    void update() throws Exception {
        performPut(URL + USER_ID, getTo(getUpdated()));
        USER_MATCHER.assertMatch(userService.get(USER_ID), getUpdated());
    }

    @Test
    void createWithLocation() throws Exception {
        User expected = getNew();
        MvcResult result = getPostResult(URL, getTo(expected));
        User actual = JsonTestUtil.readValue(mapper, result, User.class);
        expected.setId(actual.getId());
        USER_MATCHER_FOR_PASSWORDLESS_FROM_TOS.assertMatch(actual, expected);
    }

    @Test
    void createNotValid() throws Exception {
        UserTo invalid = getTo(getNew());
        invalid.setEnabled(null);
        checkValidation(URL, invalid);
    }

    @Test
    void checkMinimalisticRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New\",\"email\":\"new@new.com\",\"password\":\"12345\",\"enabled\":true,\"roles\":[\"USER\"]}")
        )
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
