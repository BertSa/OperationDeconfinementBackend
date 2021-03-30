package ca.bertsa.cal.h21_420_445.operation_deconfinement.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER, LOCAL_VARIABLE, CONSTRUCTOR, TYPE_USE})
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

    public String message() default "There is already user with this email!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}