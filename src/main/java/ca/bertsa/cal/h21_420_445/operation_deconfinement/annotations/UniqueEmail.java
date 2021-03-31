package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations;


import ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.EMAIL_ALREADY_USED;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD, PARAMETER, LOCAL_VARIABLE})
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

    public String message() default EMAIL_ALREADY_USED;

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}