package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.dto.ErrorTo;
import com.github.afanas10101111.dfl.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.afanas10101111.dfl.ErrorTestUtil.registrationErrorTo;
import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static com.github.afanas10101111.dfl.UserTestUtil.getNew;
import static com.github.afanas10101111.dfl.UserTestUtil.getTo;
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
    void registerForbiddenForAuthenticated() throws Exception {
        User aNew = getNew();
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(getTo(aNew)))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorTo errorTo = JsonTestUtil.readValue(mapper, result, ErrorTo.class);
        assertEquals(registrationErrorTo, errorTo);
    }
}
