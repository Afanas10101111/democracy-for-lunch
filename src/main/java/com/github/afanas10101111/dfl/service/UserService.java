package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.repository.UserRepository;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserService {
    private static final String ASSERT_MESSAGE = "Argument can`t be null";

    private final UserRepository repository;

    public User create(User user) {
        Assert.notNull(user, ASSERT_MESSAGE);
        return repository.save(user);
    }

    @Transactional
    public void update(User user) {
        Assert.notNull(user, ASSERT_MESSAGE);
        User userFromDb = get(user.id());

        userFromDb.setName(user.getName());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setEnabled(user.isEnabled());
        userFromDb.setVoteDate(user.getVoteDate());
        userFromDb.setVotedForId(user.getVotedForId());
        userFromDb.setRoles(user.getRoles());
    }

    public void enable(long id, boolean enable) {
        ValidationUtil.checkNotFoundWithId(repository.enable(id, enable), id);
    }

    public void delete(long id) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(long id) {
        return ValidationUtil.checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, ASSERT_MESSAGE);
        return ValidationUtil.checkNotFoundWithMsg(repository.getByEmail(email), String.format("email=%s", email));
    }

    public List<User> getAll() {
        return repository.getAll();
    }
}
