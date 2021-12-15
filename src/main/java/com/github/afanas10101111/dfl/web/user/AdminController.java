package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import com.github.afanas10101111.dfl.model.User;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = AdminController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController extends BaseUserController {
    public static final String URL = "/admin/users";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@RequestBody UserTo userTo) {
        log.info("createWithLocation (mail = {})", userTo.getEmail());
        User newFromTo = mapper.map(userTo, User.class);
        ValidationUtil.checkNew(newFromTo);
        User created = service.create(newFromTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody UserTo userTo) {
        super.update(id, userTo);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable long id, boolean enable) {
        log.info("set with id = {} enable = {}", id, enable);
        service.enable(id, enable);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        super.delete(id);
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        return super.get(id);
    }

    @GetMapping("/by-email")
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }
}
