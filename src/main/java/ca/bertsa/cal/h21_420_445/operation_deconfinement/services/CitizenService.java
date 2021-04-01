package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.TUTOR_MINIMUM_AGE;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CitizenService {
    @Autowired
    private CitizenRepository citizenRepository;

    public boolean isCitizenValidTutor(String email, String password) {
        return citizenRepository.findByEmailIgnoreCaseAndPasswordAndActiveAndBirthBefore(email, password, true, LocalDate.now().minusYears(TUTOR_MINIMUM_AGE).plusDays(1)) != null;
    }

    public Citizen add(Citizen adultActive) {
        return citizenRepository.save(adultActive);
    }

    public Citizen findByEmailAndPassword(String email, String password) {
        return citizenRepository.findByEmailIgnoreCaseAndPassword(email, password);
    }

    public int getNbOfCitizen() {
        return citizenRepository.findAll().size();
    }

    public Citizen findByEmailAndPasswordAndActive(String email, String password) {
        return citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(email, password, true);
    }

    public void delete(Citizen c) {
        citizenRepository.delete(c);
    }

    public Citizen findByEmail(String email) {
        return citizenRepository.findByEmailIgnoreCase(email);
    }

    public Citizen findByNoAssuranceMaladie(String noAssuranceMaladie) {
        return citizenRepository.findByNoAssuranceMaladieIgnoreCase(noAssuranceMaladie);
    }

    public Citizen register(CitizenData user, Address save, License licenseCreated) {
        Citizen userCreated = new Citizen();
        userCreated.setEmail(user.getEmail());
        userCreated.setPassword(user.getPassword());
        userCreated.setLastName(user.getLastName());
        userCreated.setFirstName(user.getFirstName());
        userCreated.setNoAssuranceMaladie(user.getNoAssuranceMaladie());
        userCreated.setPhone(user.getPhone());
        userCreated.setBirth(user.getBirth());
        userCreated.setAddress(save);
        userCreated.setSex(user.getSex());
        userCreated.setLicense(licenseCreated);
        if (user.getTutor() != null)
            userCreated.setTutor(findByEmailAndPassword(user.getTutor().getEmail(), user.getTutor().getPassword()));
        return add(userCreated);
    }

    public boolean isNoAssuranceMaladieExist(String value) {
        return findByNoAssuranceMaladie(value) != null;
    }
}
