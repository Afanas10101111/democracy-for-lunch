package com.github.afanas10101111.dfl.util;

import com.github.afanas10101111.dfl.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtil {
    public static User prepareToSave(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
