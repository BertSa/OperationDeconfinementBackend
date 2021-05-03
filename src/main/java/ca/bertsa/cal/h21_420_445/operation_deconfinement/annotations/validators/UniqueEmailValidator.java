package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.EnvironmentServer;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.UniqueEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private SystemService userService;

    @Autowired
    private EnvironmentServer env;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean flag = true;
        if (email == null || userService.isLoginExist(email)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(env.messageErrorEmail)
                    .addConstraintViolation();
            flag = false;
        }
        return flag;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }
}
