package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.UniqueNoAssuranceMaladie;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.NAM_LENGTH;

public class UniqueNoAssuranceMaladieValidator implements ConstraintValidator<UniqueNoAssuranceMaladie, String> {
    public void initialize(UniqueNoAssuranceMaladie constraint) {
    }

    @Autowired
    private SystemService userService;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {//TODO VALIDATE WITH MINISTER
        return value != null && !userService.isNoAssuranceMaladieExist(value) && value.length() == NAM_LENGTH;
    }
}