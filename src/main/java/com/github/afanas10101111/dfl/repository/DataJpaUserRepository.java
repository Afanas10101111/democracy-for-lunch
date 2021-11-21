package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DataJpaUserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = ?1")
    int delete(long id);

    User getByEmail(String email);
}
