package com.patria.apps.helper;

import com.patria.apps.exception.GeneralException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class ValidationHelperService {

    private final Validator validator;

    public void validate(Object object) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if (constraintViolations.size() != 0) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Some value is empty!");
        }
    }

}
