package com.github.afanas10101111.dfl.web.security;

import com.github.afanas10101111.dfl.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private final long id;

    public AuthorizedUser(User user) {
        super(user.getName(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        id = user.id();
    }
}
