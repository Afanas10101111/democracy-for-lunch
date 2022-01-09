package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.BaseWebTestClass;
import com.github.afanas10101111.dfl.JsonTestUtil;
import com.github.afanas10101111.dfl.dto.ErrorTo;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.afanas10101111.dfl.ErrorTestUtil.updateRestrictionExceptionErrorTo;
import static com.github.afanas10101111.dfl.UserTestUtil.USER_ID;
import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static com.github.afanas10101111.dfl.UserTestUtil.getTo;
import static com.github.afanas10101111.dfl.UserTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.service.UserService.PROFILE_WITH_RESTRICTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(PROFILE_WITH_RESTRICTION)
class HerokuControllerTest extends BaseWebTestClass {
    private static final String URL = AdminController.URL + '/';

    @Test
    void delete() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete(URL + USER_ID)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword())))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
        ErrorTo errorTo = JsonTestUtil.readValue(mapper, result, ErrorTo.class);
        assertEquals(updateRestrictionExceptionErrorTo, errorTo);
    }

    @Test
    void enable() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.patch(URL + USER_ID)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .param("enable", "false")
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
        ErrorTo errorTo = JsonTestUtil.readValue(mapper, result, ErrorTo.class);
        assertEquals(updateRestrictionExceptionErrorTo, errorTo);
    }

    @Test
    void update() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put(URL + USER_ID)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(getTo(getUpdated())))
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
        ErrorTo errorTo = JsonTestUtil.readValue(mapper, result, ErrorTo.class);
        assertEquals(updateRestrictionExceptionErrorTo, errorTo);
    }
}
