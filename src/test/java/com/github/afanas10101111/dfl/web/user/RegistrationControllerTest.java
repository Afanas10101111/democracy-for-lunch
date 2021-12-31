package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static com.github.afanas10101111.dfl.UserTestUtil.getNew;
import static com.github.afanas10101111.dfl.UserTestUtil.getTo;
import static com.github.afanas10101111.dfl.web.user.RegistrationController.ASSERT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegistrationControllerTest extends BaseWebTestClass {
    private static final String URL = RegistrationController.URL;

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

    @Test
    void registerForbiddenForAuthenticated() {
        User aNew = getNew();
        try {
            getPostResult(URL, getTo(aNew));
        } catch (Exception e) {
            assertEquals(ASSERT_MESSAGE, Objects.requireNonNull(NestedExceptionUtils.getRootCause(e)).getMessage());
        }
    }
}
