package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CitizenService {
    @Autowired
    private CitizenRepository citizenRepository;

    public boolean isCitizenValidTutor(String email, String password) {
        return citizenRepository.findByEmailIgnoreCaseAndPasswordAndActiveAndBirthBefore(email, password, true, LocalDate.now().minusYears(Consts.TUTOR_MINIMUM_AGE).plusDays(1)) != null;
    }
}
