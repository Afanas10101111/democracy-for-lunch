package com.github.afanas10101111.dfl.dto;

import com.github.afanas10101111.dfl.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTo {
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean enabled;
    private LocalDate voteDate;
    private Long votedForId;
    private Set<Role> roles;
}
