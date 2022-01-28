package com.github.afanas10101111.dfl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishTo {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Range(min = 1, max = 10000000)
    private int price;
}
