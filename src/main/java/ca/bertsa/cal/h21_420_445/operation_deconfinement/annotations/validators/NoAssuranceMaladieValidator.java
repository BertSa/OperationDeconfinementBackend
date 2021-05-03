package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.EnvironmentServer;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.CitizenService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.NoAssuranceMaladie;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoAssuranceMaladieValidator implements ConstraintValidator<NoAssuranceMaladie, String> {
    @Autowired
    private CitizenService citizenService;
    @Autowired
    private EnvironmentServer env;

    public void initialize(NoAssuranceMaladie constraint) {
    }

    public boolean isValid(String str, ConstraintValidatorContext context) {
        boolean flag = false;
        context.disableDefaultConstraintViolation();
        if (str != null) {
            String nassm = str.replaceAll("[^A-Za-z0-9]+?", "").toLowerCase();
            if (nassm.matches("[A-Za-z]{4}[0-9]{8}")) {
                if (!citizenService.isNASSMAlreadyRegistered(nassm)) {
                    if (citizenService.doesNASSMExistMinistere(nassm)) {
                        flag = true;
                    } else {
                        context.buildConstraintViolationWithTemplate(env.messageErrorNassmDoesntExist)
                                .addConstraintViolation();
                    }
                } else {
                    context.buildConstraintViolationWithTemplate(env.messageErrorNassmRegistered)
                            .addConstraintViolation();
                }
            } else {
                context.buildConstraintViolationWithTemplate(env.messageErrorNassmInvalid)
                        .addConstraintViolation();
            }
        } else {
            context.buildConstraintViolationWithTemplate(env.messageErrorNassmIsNull)
                    .addConstraintViolation();
        }


        return flag;
    }
}
