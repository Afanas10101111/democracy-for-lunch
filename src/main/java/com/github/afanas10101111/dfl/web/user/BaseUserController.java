package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.service.UserService;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
abstract class BaseUserController {

    @Autowired
    protected UserService service;

    @Autowired
    protected ModelMapper mapper;

    public void update(long id, UserTo userTo) {
        log.info("update with id = {}, set {}", id, userTo);
        User updatedFromTo = mapper.map(userTo, User.class);
        ValidationUtil.checkIdConsistent(id, updatedFromTo);
        service.update(updatedFromTo);
    }

    public void delete(long id) {
        log.info("delete with id = {}", id);
        service.delete(id);
    }

    public User get(long id) {
        log.info("get with id = {}", id);
        return service.get(id);
    }
}
