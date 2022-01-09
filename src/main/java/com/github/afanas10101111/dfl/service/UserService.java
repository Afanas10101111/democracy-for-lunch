package com.github.afanas10101111.dfl.service;

import com.github.afanas10101111.dfl.exception.UpdateRestrictionException;
import com.github.afanas10101111.dfl.model.NamedEntity;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.repository.UserRepository;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

import static com.github.afanas10101111.dfl.util.UserUtil.prepareToSave;

@RequiredArgsConstructor
@Service
public class UserService {
    public static final String PROFILE_WITH_RESTRICTION = "heroku";

    private static final String ASSERT_MESSAGE = "Argument can`t be null";

    private final UserRepository repository;

    private boolean modificationRestriction;

    @Autowired
    public void setEnvironment(Environment environment) {
        modificationRestriction = environment.acceptsProfiles(Profiles.of(PROFILE_WITH_RESTRICTION));
    }

    public User create(User user) {
        Assert.notNull(user, ASSERT_MESSAGE);
        return repository.save(prepareToSave(user));
    }

    @Transactional
    public void update(User user) {
        Assert.notNull(user, ASSERT_MESSAGE);
        long id = user.id();
        checkModificationAllowed(id);
        User userFromDb = get(id);
        userFromDb.setName(user.getName());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setEnabled(user.isEnabled());
        userFromDb.setRoles(user.getRoles());
        prepareToSave(userFromDb);
    }

    public void enable(long id, boolean enable) {
        checkModificationAllowed(id);
        ValidationUtil.checkNotFoundWithId(repository.enable(id, enable), id);
    }

    public void delete(long id) {
        checkModificationAllowed(id);
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

    private void checkModificationAllowed(long id) {
        if (modificationRestriction && id < NamedEntity.START_SEQ + 2) {
            throw new UpdateRestrictionException(id);
        }
    }
}
