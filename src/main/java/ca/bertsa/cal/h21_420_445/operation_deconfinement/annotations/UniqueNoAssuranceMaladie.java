package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators.UniqueNoAssuranceMaladieValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD,  PARAMETER, LOCAL_VARIABLE})
@Constraint(validatedBy = UniqueNoAssuranceMaladieValidator.class)
public @interface UniqueNoAssuranceMaladie {

    public String message() default NAM_ALREADY_USED;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

