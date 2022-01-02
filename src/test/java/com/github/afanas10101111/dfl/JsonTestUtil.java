package com.github.afanas10101111.dfl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;

public class JsonTestUtil {
    public static <T> List<T> readValues(ObjectMapper mapper, MvcResult result, Class<T> clazz) throws IOException {
        return mapper.readerFor(clazz).<T>readValues(result.getResponse().getContentAsString()).readAll();
    }

    public static <T> T readValue(ObjectMapper mapper, MvcResult result, Class<T> clazz) throws IOException {
        return mapper.readerFor(clazz).readValue(result.getResponse().getContentAsString());
    }
}
