package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators.NoAssuranceMaladieValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD})
@Constraint(validatedBy = NoAssuranceMaladieValidator.class)
public @interface NoAssuranceMaladie {
    String message() default "Problem with NoAssuranceMaladie verification!";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
