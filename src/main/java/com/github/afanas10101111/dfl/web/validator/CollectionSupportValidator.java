package com.github.afanas10101111.dfl.web.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Collection;

@RequiredArgsConstructor
public class CollectionSupportValidator implements Validator {

    private final Validator validator;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        if (Collection.class.isAssignableFrom(target.getClass())) {
            Collection<?> collection = (Collection<?>) target;
            for (Object object : collection) {
                ValidationUtils.invokeValidator(validator, object, errors);
            }
        } else {
            ValidationUtils.invokeValidator(validator, target, errors);
        }
    }
}
