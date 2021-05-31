package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CitizenRepositoryTest {

    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private AddressRepository addressRepository;

    private Address savedAddress;
    private Citizen adultActive;

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(citizenRepository);
        assertNotNull(addressRepository);
    }

    @BeforeAll
    void before() {
        Address address = new Address("h8s3dac2",
                "39fafaef0 rue William-Macdonald",
                "Lachinafsfeeae",
                "qafefsc",
                "13");
        savedAddress = addressRepository.save(address);
    }

    @BeforeEach
    void setUp() {
        adultActive = new Citizen();
        adultActive.setPassword("password");
        adultActive.setFirstName("firstname");
        adultActive.setLastName("lastname");
        adultActive.setSex(Sex.MALE);
        adultActive.setPhone("412-131-3131");
        adultActive.setAddress(savedAddress);
        adultActive.setEmail("u1@bertsa.ca");
        adultActive.setBirth(LocalDate.now().minusYears(26));
        adultActive.setNoAssuranceMaladie("aaaa00001111");
    }

    @Test
    void saveTest() {
        int sizeBefore = citizenRepository.findAll().size();
        Citizen save = citizenRepository.save(adultActive);
        int sizeAfter = citizenRepository.findAll().size();

        assertNotNull(save);
        assertNotNull(save.getId());
        assertEquals(LocalDate.now(), save.getDateJoined());
        assertEquals(sizeBefore + 1, sizeAfter);

        citizenRepository.delete(save);
    }

    @Test
    void findByEmailIgnoreCaseAndPasswordAndActive() {
        Citizen save = citizenRepository.save(adultActive);
        Citizen notAUser = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive("samdddada@bertsa.ca", "aadadadmin", true);
        Citizen aUser = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(adultActive.getEmail(), adultActive.getPassword(), true);


        assertNull(notAUser);
        assertNotNull(aUser);
        assertEquals(save, aUser);
        assertEquals(adultActive.getNoAssuranceMaladie(), aUser.getNoAssuranceMaladie());
        citizenRepository.delete(aUser);
    }


}