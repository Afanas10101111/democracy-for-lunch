package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.model.Role;
import com.github.afanas10101111.dfl.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;

public class UserTestUtil {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.createWithFieldsToIgnore("registered");

    public static final long ADMIN_ID = 100000;
    public static final long USER_ID = 100001;
    public static final long NA_ID = 0;
    public static final String NA_EMAIL = "someEmail@email.me";

    public static final User admin = new User("Admin", "admin@gmail.com", "admin", true, Set.of(Role.USER, Role.ADMIN));
    public static final User user = new User("User", "user@yandex.ru", "password", true, Set.of(Role.USER));

    public static final List<User> all = List.of(admin, user);

    static {
        admin.setId(ADMIN_ID);
        user.setId(USER_ID);
    }

    public static User getNew() {
        return new User("New", "new@new.com", "1234", true, Set.of(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User("Updated", "up@up.up", "4321", false, Set.of(Role.USER, Role.ADMIN));
        updated.setId(USER_ID);
        return updated;
    }

    public static User getVotedUser() {
        User user = new User(
                UserTestUtil.user.getName(),
                UserTestUtil.user.getEmail(),
                UserTestUtil.user.getPassword(),
                UserTestUtil.user.isEnabled(),
                UserTestUtil.user.getRoles()
        );
        user.setId(USER_ID);
        user.setVoteDate(LocalDate.now());
        user.setVotedForId(MC_DONALDS_ID);
        return user;
    }
}
