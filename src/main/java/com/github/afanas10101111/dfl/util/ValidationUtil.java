package com.github.afanas10101111.dfl.util;


import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.model.NamedEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {
    public static void checkNew(NamedEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new");
        }
    }

    public static void checkIdConsistent(long id, NamedEntity entity) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id = " + id);
        }
    }

    public static <T> T checkNotFoundWithId(T object, long id) {
        checkNotFound(object instanceof Boolean ? (boolean) object : object != null, String.format("id = %s", id));
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
