package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators.UniqueNoAssuranceMaladieValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.NAM_ALREADY_USED;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SuppressWarnings("unused")
@Retention(RUNTIME)
@Target({FIELD, PARAMETER, LOCAL_VARIABLE})
@Constraint(validatedBy = UniqueNoAssuranceMaladieValidator.class)
public @interface UniqueNoAssuranceMaladie {

    String message() default NAM_ALREADY_USED;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

