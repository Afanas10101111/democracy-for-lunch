package com.github.afanas10101111.dfl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealTo {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Range(min = 100, max = 10000000)
    private int price;

    @Data
    public static class ValidList implements List<MealTo> {

        @Valid
        @Delegate
        private List<MealTo> list = new ArrayList<>();
    }
}
