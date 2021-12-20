package com.github.afanas10101111.dfl.util;

import com.github.afanas10101111.dfl.model.NamedEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerUtil {
    public static <T extends NamedEntity> URI getUriOfNewResource(T created) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
    }
}
