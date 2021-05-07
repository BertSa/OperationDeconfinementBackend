package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD})
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Problem with email verification!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

