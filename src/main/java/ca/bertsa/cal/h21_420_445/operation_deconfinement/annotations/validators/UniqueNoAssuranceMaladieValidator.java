package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.UniqueNoAssuranceMaladie;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.NAM_LENGTH;

public class UniqueNoAssuranceMaladieValidator implements ConstraintValidator<UniqueNoAssuranceMaladie, String> {
    @Autowired
    private CitizenService citizenService;

    public void initialize(UniqueNoAssuranceMaladie constraint) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {//TODO VALIDATE WITH MINISTER
        return value != null && !citizenService.isNoAssuranceMaladieExist(value) && value.length() == NAM_LENGTH;
    }
}
