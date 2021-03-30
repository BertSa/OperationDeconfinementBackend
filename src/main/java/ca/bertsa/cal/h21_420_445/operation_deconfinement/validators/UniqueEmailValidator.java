package ca.bertsa.cal.h21_420_445.operation_deconfinement.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private SystemService userService;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null&&!userService.isLoginExist(value);
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }
}
