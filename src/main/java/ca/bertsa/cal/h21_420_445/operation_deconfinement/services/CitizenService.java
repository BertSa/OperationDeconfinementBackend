package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.ServerConst.MIN_AGE_TUTOR;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class CitizenService {
    @Value("${bertsa.ministere.url}")
    private String ministereUrl;
    @Autowired
    private CitizenRepository citizenRepository;

    public boolean isCitizenValidTutor(String email, String password) {
        return citizenRepository.findByEmailIgnoreCaseAndPasswordAndActiveAndBirthBefore(email, password, true, LocalDate.now().minusYears(MIN_AGE_TUTOR).plusDays(1)) != null;
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
        Citizen userCreated = citizenRepository.findByNoAssuranceMaladieLikeAndActive(user.getNoAssuranceMaladie(), false);

        if (userCreated == null) {
            userCreated = new Citizen();
        }
        userCreated.setEmail(user.getEmail());
        userCreated.setPassword(user.getPassword());
        userCreated.setPhone(user.getPhone());
        userCreated.setNoAssuranceMaladie(user.getNoAssuranceMaladie());

        userCreated.setLastName(infoFromMinistere.getLastName());
        userCreated.setFirstName(infoFromMinistere.getFirstName());
        userCreated.setBirth(infoFromMinistere.getBirth());
        userCreated.setSex(infoFromMinistere.getSex());
        userCreated.setActive(true);

        return addOrUpdate(userCreated);
    }

    public boolean isNASSMAlreadyRegisteredAndActive(String nassm) {
        return citizenRepository.existsByNoAssuranceMaladieLikeAndActive(nassm, true);
    }

    public boolean doesNASSMExistMinistere(String nassm) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(ministereUrl + "/exist/" + nassm, Boolean.class);
        Boolean body = responseEntity.getBody();
        return body != null && body;
    }

    public boolean isNotEligibleForLicense(TypeLicense typeValidation, String input) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(ministereUrl + "/validate/" + typeValidation.toString().toLowerCase() + "/" + input, Boolean.class);
        Boolean body = responseEntity.getBody();
        return body == null || !body;
    }

    public Citizen getCitizenInfo(String nassm) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Citizen> responseEntity = restTemplate.getForEntity(ministereUrl + "/info/" + nassm, Citizen.class);
        return responseEntity.getBody();
    }


    public TypeLicense getUserTypeValid(String nassm) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TypeLicense> responseEntity = restTemplate.getForEntity(ministereUrl + "/type/" + nassm, TypeLicense.class);
        return responseEntity.getBody();
    }
}
