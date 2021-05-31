package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.UniqueEmail;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.env.MessagesError;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private SystemService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean flag = true;
        if (email == null || userService.isLoginExist(email)) {
            if (userService.isActive(email)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(MessagesError.MESSAGE_ERROR_EMAIL)
                        .addConstraintViolation();
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }
}
