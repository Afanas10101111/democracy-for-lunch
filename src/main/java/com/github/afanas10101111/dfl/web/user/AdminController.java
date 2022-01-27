package com.github.afanas10101111.dfl.web.user;

import com.github.afanas10101111.dfl.dto.UserTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = AdminController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController extends BaseUserController {
    public static final String URL = "/v1/admin/users";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTo> createWithLocation(@Valid @RequestBody UserTo userTo) {
        log.info("createWithLocation (mail = {})", userTo.getEmail());
        return super.createWithLocation(URL, userTo);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @Valid @RequestBody UserTo userTo) {
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
    public UserTo get(@PathVariable long id) {
        return super.get(id);
    }

    @GetMapping("/by-email")
    public UserTo getByEmail(String email) {
        log.info("getByEmail {}", email);
        return getTo(service.getByEmail(email.toLowerCase()));
    }

    @GetMapping
    public List<UserTo> getAll() {
        log.info("getAll");
        return service.getAll().stream()
                .map(this::getTo)
                .collect(Collectors.toList());
    }
}
