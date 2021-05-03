package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.EnvironmentServer;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Email;
import java.time.LocalDate;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.NAM_LENGTH;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.TUTOR_MINIMUM_AGE;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CitizenService {
    @Autowired
    private EnvironmentServer env;
    @Autowired
    private CitizenRepository citizenRepository;

    public boolean isCitizenValidTutor(String email, String password) {
        return citizenRepository.findByEmailIgnoreCaseAndPasswordAndActiveAndBirthBefore(email, password, true, LocalDate.now().minusYears(TUTOR_MINIMUM_AGE).plusDays(1)) != null;
    }

    public Citizen addOrUpdate(Citizen adultActive) {
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


    public Citizen findByEmail(String email) {
        return citizenRepository.findByEmailIgnoreCase(email);
    }

    public Citizen register(CitizenData user, Citizen infoFromMinistere) {
        Citizen userCreated = new Citizen();
        userCreated.setEmail(user.getEmail());
        userCreated.setPassword(user.getPassword());
        userCreated.setNoAssuranceMaladie(user.getNoAssuranceMaladie());
        userCreated.setPhone(user.getPhone());

        userCreated.setLastName(infoFromMinistere.getLastName());
        userCreated.setFirstName(infoFromMinistere.getFirstName());
        userCreated.setBirth(infoFromMinistere.getBirth());
        userCreated.setSex(infoFromMinistere.getSex());

        return addOrUpdate(userCreated);
    }

    public boolean isNASSMAlreadyRegistered(String nassm) {
        return citizenRepository.findByNoAssuranceMaladieIgnoreCase(nassm) != null;
    }
    public boolean doesNASSMExistMinistere(String nassm){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(env.ministereUrl + "/exist/" + nassm, Boolean.class);
        Boolean body = responseEntity.getBody();
        return body != null && body;
    }

    public boolean isNotEligibleForLicense(TypeLicense typeValidation, String input) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(env.ministereUrl + "/validate/" + typeValidation.toString().toLowerCase() + "/" + input, Boolean.class);
        Boolean body = responseEntity.getBody();
        return body == null || !body;
    }

    public Citizen getCitizenInfo(String nassm) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Citizen> responseEntity = restTemplate.getForEntity(env.ministereUrl + "/info/" + nassm, Citizen.class);

        return responseEntity.getBody();
    }

    public boolean isEmailAlreadyTaken(@Email String email) {
        return findByEmail(email) != null;
    }
}
