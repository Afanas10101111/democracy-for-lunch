package com.github.afanas10101111.dfl.util;

import com.github.afanas10101111.dfl.config.SecurityConfig;
import com.github.afanas10101111.dfl.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtil {
    public static User prepareToSave(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(SecurityConfig.PASSWORD_ENCODER.encode(user.getPassword()));
        return user;
    }
}
