package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CitizenServiceTest {
    @Autowired
    private CitizenService citizenService;
    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("isCitizenValidTutorTest")
    void isCitizenValidTutorTest() {
        Address address = new Address("h8s3c2", "391 rue William", "Mock", "qc", "2");
        Address save = addressRepository.save(address);

        Citizen user = new Citizen();
        user.setNoAssuranceMaladie("dlwi14441241");
        user.setFirstName("Sam");
        user.setLastName("Bertd");
        user.setAddress(save);
        user.setPassword("admin");
        user.setPhone("+1-555-555-5555");
        user.setSex(Sex.MALE);
        user.setEmail("sammmmmmmmmmmmm@bertsa.ca");
        user.setBirth(LocalDate.now().minusYears(15));

        Citizen user2 = new Citizen();
        user2.setNoAssuranceMaladie("dlwi14541241");
        user2.setFirstName("Sam");
        user2.setLastName("Bertd");
        user2.setAddress(save);
        user2.setPassword("admin");
        user2.setPhone("+1-555-555-5555");
        user2.setSex(Sex.MALE);
        user2.setEmail("sammmmmum@bertsa.ca");
        user2.setBirth(LocalDate.now().minusYears(18));

        citizenRepository.save(user);
        Citizen userValidBefore = citizenRepository.save(user2);

        assertFalse(citizenService.isCitizenValidTutor(user.getEmail(), user.getPassword()));
        assertTrue(citizenService.isCitizenValidTutor(user2.getEmail(), user2.getPassword()));

        userValidBefore.setActive(false);
        citizenRepository.save(userValidBefore);

        assertFalse(citizenService.isCitizenValidTutor(user2.getEmail(), user2.getPassword()));
    }

}