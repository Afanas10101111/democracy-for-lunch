package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.web.security.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ProfileController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController extends BaseUserController {
    public static final String URL = "/profile";

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo userTo) {
        super.update(SecurityUtil.getAuthUserId(), userTo);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(SecurityUtil.getAuthUserId());
    }

    @GetMapping
    public User get() {
        return super.get(SecurityUtil.getAuthUserId());
    }
}
