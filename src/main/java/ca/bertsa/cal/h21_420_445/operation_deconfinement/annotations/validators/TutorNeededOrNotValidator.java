package ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.validators;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.TutorNeededOrNot;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.LoginData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.CitizenService;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class TutorNeededOrNotValidator implements ConstraintValidator<TutorNeededOrNot, CitizenData> {
    private String birth;
    private String tutor;
    @Autowired
    private CitizenService citizenService;

    public void initialize(TutorNeededOrNot constraint) {
        birth = constraint.birth();
        tutor = constraint.tutor();

    }

    public boolean isValid(CitizenData value, ConstraintValidatorContext context) {
        LocalDate fieldValue = (LocalDate) new BeanWrapperImpl(value)
                .getPropertyValue(birth);
        LoginData fieldMatchValue = (LoginData) new BeanWrapperImpl(value)
                .getPropertyValue(tutor);

        assert fieldValue != null;
        if (fieldValue.isAfter(LocalDate.now().minusYears(Consts.YOUNG_ADULT_AGE))) {

            //                context.disableDefaultConstraintViolation();
            //                context.buildConstraintViolationWithTemplate("this detail is wrong")
            //                        .addConstraintViolation();
            return fieldMatchValue != null && citizenService.isCitizenValidTutor(fieldMatchValue.getEmail(), fieldMatchValue.getPassword());

        }
        value.setTutor(null);
        return true;
    }
}
