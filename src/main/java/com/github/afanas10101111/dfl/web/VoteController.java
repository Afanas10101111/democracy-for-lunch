package com.github.afanas10101111.dfl.web;

import com.github.afanas10101111.dfl.service.VoteService;
import com.github.afanas10101111.dfl.web.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = VoteController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    public static final String URL = "/vote-for";

    private final VoteService service;

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(long restaurantId) {
        long userId = SecurityUtil.getAuthUserId();
        log.info("User with id = {} voted for restaurant with id = {}", userId, restaurantId);
        service.vote(userId, restaurantId);
    }
}
