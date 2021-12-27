package com.github.afanas10101111.dfl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.afanas10101111.dfl.config.SecurityConfig;
import com.github.afanas10101111.dfl.config.WebConfig;
import com.github.afanas10101111.dfl.service.RestaurantService;
import com.github.afanas10101111.dfl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static com.github.afanas10101111.dfl.UserTestUtil.admin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {WebConfig.class, SecurityConfig.class})
public abstract class BaseWebTestClass extends BaseServiceTestClass {
    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    protected MockMvc mockMvc;
    protected ObjectMapper mapper;

    @Autowired
    protected UserService userService;

    @Autowired
    protected RestaurantService restaurantService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @PostConstruct
    private void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    protected MvcResult getGetResult(String url, MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform((params == null ? MockMvcRequestBuilders.get(url) : MockMvcRequestBuilders.get(url).params(params))
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    protected MvcResult getGetResult(String url) throws Exception {
        return getGetResult(url, null);
    }

    protected MvcResult getPostResult(String url, Object body) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    protected void performPut(String url, Object body) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    protected void performDelete(String url) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword())))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    protected void checkValidation(String url, Object invalid) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
