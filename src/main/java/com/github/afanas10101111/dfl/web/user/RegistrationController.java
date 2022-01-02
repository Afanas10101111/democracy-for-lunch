package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.web.security.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = RegistrationController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController extends BaseUserController {
    public static final String URL = "/register";
    public static final String ASSERT_MESSAGE = "Only allowed for unauthenticated user";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTo> register(@AuthenticationPrincipal AuthorizedUser user, @Valid @RequestBody UserTo userTo) {
        Assert.isNull(user, ASSERT_MESSAGE);
        log.info("register (mail = {})", userTo.getEmail());
        return super.createWithLocation(userTo);
    }
}
