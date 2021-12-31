package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.service.UserService;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static com.github.afanas10101111.dfl.util.ControllerUtil.getUriOfNewResource;

@Slf4j
abstract class BaseUserController {

    @Autowired
    protected UserService service;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<UserTo> createWithLocation(UserTo userTo) {
        User newFromTo = getFromTo(userTo);
        ValidationUtil.checkNew(newFromTo);
        User created = service.create(newFromTo);
        return ResponseEntity.created(getUriOfNewResource(created)).body(getTo(created));
    }

    public void update(long id, UserTo userTo) {
        log.info("update with id = {}, set {}", id, userTo);
        User updatedFromTo = getFromTo(userTo);
        ValidationUtil.checkIdConsistent(id, updatedFromTo);
        service.update(updatedFromTo);
    }

    public void delete(long id) {
        log.info("delete with id = {}", id);
        service.delete(id);
    }

    public UserTo get(long id) {
        log.info("get with id = {}", id);
        return getTo(service.get(id));
    }

    protected User getFromTo(UserTo to) {
        return mapper.map(to, User.class);
    }

    protected UserTo getTo(User user) {
        return mapper.map(user, UserTo.class);
    }
}
