package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.web.security.AuthorizedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ProfileController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('USER')")
public class ProfileController extends BaseUserController {
    public static final String URL = "/profile";

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthorizedUser authUser, @Valid @RequestBody UserTo userTo) {
        super.update(authUser.getId(), userTo);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        super.delete(authUser.getId());
    }

    @GetMapping
    public UserTo get(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.get(authUser.getId());
    }
}
