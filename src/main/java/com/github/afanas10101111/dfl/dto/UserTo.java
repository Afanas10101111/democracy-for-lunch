package com.github.afanas10101111.dfl.dto;

import com.github.afanas10101111.dfl.model.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
public class UserTo {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Date registered;
    private boolean enabled;
    private LocalDate voteDate;
    private Long votedForId;
    private Set<Role> roles;
}
