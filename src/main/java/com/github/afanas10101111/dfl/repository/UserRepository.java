package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final DataJpaUserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public boolean enable(long id, boolean enable) {
        return repository.enable(id, enable) != 0;
    }

    public boolean delete(long id) {
        return repository.delete(id) != 0;
    }

    public User get(long id) {
        return repository.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }
}
