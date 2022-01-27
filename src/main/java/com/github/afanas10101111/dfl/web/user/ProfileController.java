package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.web.security.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = ProfileController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController extends BaseUserController {
    public static final String URL = "/v1/profile";
    public static final String REG = "/register";

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser, @Valid @RequestBody UserTo userTo) {
        long id = authUser.getId();
        log.info("update user with id = {}, to {}", id, userTo);
        super.update(id, userTo);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser) {
        long id = authUser.getId();
        log.info("delete user with id = {}", id);
        super.delete(id);
    }

    @GetMapping
    public UserTo get(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser) {
        long id = authUser.getId();
        log.info("get user with id = {}", id);
        return super.get(id);
    }

    @PostMapping(value = REG, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTo> register(@Valid @RequestBody UserTo userTo) {
        log.info("register (mail = {})", userTo.getEmail());
        return super.createWithLocation(URL, userTo);
    }
}
