package com.github.afanas10101111.dfl.web;

import com.github.afanas10101111.dfl.service.VoteService;
import com.github.afanas10101111.dfl.web.security.AuthorizedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = VoteController.URL)
public class VoteController {
    public static final String URL = "/v1/vote-for";

    private final VoteService service;

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser, long restaurantId) {
        long userId = authUser.getId();
        log.info("User with id = {} voted for restaurant with id = {}", userId, restaurantId);
        service.vote(userId, restaurantId);
    }
}
