package com.github.afanas10101111.dfl.util;


import com.github.afanas10101111.dfl.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {
    public static <T> T checkNotFoundWithId(T object, long id) {
        checkNotFound(object instanceof Boolean ? (boolean) object : object != null, String.format("id=%s", id));
        return object;
    }

    public static <T> T checkNotFoundWithMsg(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    private static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException(String.format("Not found entity with %s", msg));
        }
    }
}