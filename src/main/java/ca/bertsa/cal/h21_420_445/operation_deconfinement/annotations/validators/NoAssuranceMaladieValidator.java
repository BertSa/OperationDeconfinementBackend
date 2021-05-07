package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.NoAssuranceMaladie;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.MessagesError.*;

public class NoAssuranceMaladieValidator implements ConstraintValidator<NoAssuranceMaladie, String> {
    @Autowired
    private CitizenService citizenService;

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
                        context.buildConstraintViolationWithTemplate(MESSAGE_ERROR_NASSM_DOESNT_EXIST)
                                .addConstraintViolation();
                    }
                } else {
                    context.buildConstraintViolationWithTemplate(MESSAGE_ERROR_NASSM_REGISTERED)
                            .addConstraintViolation();
                }
            } else {
                context.buildConstraintViolationWithTemplate(MESSAGE_ERROR_NASSM_INVALID)
                        .addConstraintViolation();
            }
        } else {
            context.buildConstraintViolationWithTemplate(MESSAGE_ERROR_NASSM_IS_NULL)
                    .addConstraintViolation();
        }


        return flag;
    }
}
