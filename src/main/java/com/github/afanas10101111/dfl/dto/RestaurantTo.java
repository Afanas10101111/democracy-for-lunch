package com.github.afanas10101111.dfl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTo {
    private Long id;
    private String name;
    private String address;
    private int voices;
    private Set<MealTo> meals;
}
