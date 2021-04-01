package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators.TutorNeededOrNotValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SuppressWarnings("unused")
@Retention(RUNTIME)
@Target({TYPE})
@Constraint(validatedBy = TutorNeededOrNotValidator.class)
public @interface TutorNeededOrNot {
    String message() default "Tutor not valid!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String birth();

    String tutor();
}
