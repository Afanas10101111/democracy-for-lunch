package com.github.afanas10101111.dfl.web.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@RequiredArgsConstructor
@ControllerAdvice
public class ValidatorAdvice {

    private final Validator validator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new CollectionSupportValidator(validator));
    }
}
