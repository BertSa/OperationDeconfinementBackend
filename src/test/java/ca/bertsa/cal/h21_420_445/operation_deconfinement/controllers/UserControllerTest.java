package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.LoginData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";
    private static final String DEFAULT_PASSWORD = "password";
    private static final LocalDate DEFAULT_BIRTH_CHILD = LocalDate.now().minusYears(10);
    private static final String API_USER = "/api/user";
    private static final String REGISTER_URL = API_USER + "/register";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private LicenseRepository licenseRepository;

    private Citizen adultActive;

    private Address address;
    private CitizenData citizen;


    @BeforeAll
    void before() {
        Address addresss = new Address("h8s3dac2",
                "39fafaef0 rue William-Macdonald",
                "Lachinafsfeeae",
                "qafefsc",
                "13");
        address = addressRepository.save(addresss);

        adultActive = new Citizen();
        adultActive.setEmail("u1@bertsa.ca");
        adultActive.setPassword(DEFAULT_PASSWORD);
        adultActive.setFirstName("firstname");
        adultActive.setLastName("lastname");
        adultActive.setBirth(LocalDate.now().minusYears(26));
        adultActive.setSex(Sex.MALE);
        adultActive.setPhone("412-131-3131");
        adultActive.setNoAssuranceMaladie("aaaa00001111");
        adultActive.setAddress(address);

        citizenRepository.save(adultActive);
    }


    @BeforeEach
    void setUp() {
        citizen = new CitizenData();
        citizen.setPassword("password");
        citizen.setFirstName("firstname");
        citizen.setLastName("lastname");
        citizen.setSex(Sex.MALE);
        citizen.setPhone("412-131-3131");
        citizen.setAddress(address);
    }

    @AfterEach
    void afterEach() {
        licenseRepository.deleteAll();
    }

    @Test
    void loginTest() throws Exception {
        assertNotNull(citizenRepository.findByEmailIgnoreCaseAndPassword(adultActive.getEmail(), adultActive.getPassword()));

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(API_USER +"/login")
                        .param(PARAM_EMAIL, adultActive.getEmail())
                        .param(PARAM_PASSWORD, adultActive.getPassword()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void badLogin() throws Exception {
        String email = "emailRandom@bertsa.ca";
        String password = "randomPassword";

        assertNull(citizenRepository.findByEmailIgnoreCaseAndPassword(email, password));

        this.mockMvc.perform(
                post("/api/user/login")
                        .param(PARAM_EMAIL, email)
                        .param(PARAM_PASSWORD, password))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andDo(print());

    }

    @Test
    void registerNegativeTest() throws Exception {
        citizen.setEmail("u3@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa00001113");
        citizen.setBirth(LocalDate.now().minusYears(67));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Citizen created"))
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore + 1, sizeAfter);

        Citizen c = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(citizen.getEmail(), citizen.getPassword(), true);

        assertNotNull(c);
        assertEquals(TypeLicense.NegativeTest, c.getLicense().getType());
        assertEquals(CategoryLicence.Senior, c.getLicense().getCategory());

        citizenRepository.delete(c);
    }

    @Test
    void registerVaccineTest() throws Exception {

        citizen.setEmail("u4@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa00001114");
        citizen.setBirth(LocalDate.now().minusYears(55));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/vaccine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Citizen created"))
                .andReturn();

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore + 1, sizeAfter);

        Citizen c = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(citizen.getEmail(), citizen.getPassword(), true);

        assertNotNull(c);
        assertEquals(TypeLicense.Vaccine, c.getLicense().getType());
        assertEquals(CategoryLicence.Adult, c.getLicense().getCategory());

        citizenRepository.delete(c);

    }

    @Test
    void registerNegativeChildrenTest() throws Exception {
        citizen.setEmail("child1@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa00001115");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData(adultActive.getEmail(), adultActive.getPassword()));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Children created"))
                .andReturn();

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore + 1, sizeAfter);

        Citizen c = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(citizen.getEmail(), citizen.getPassword(), true);

        assertNotNull(c);
        assertEquals(TypeLicense.NegativeTest, c.getLicense().getType());
        assertEquals(CategoryLicence.Children, c.getLicense().getCategory());

        citizenRepository.delete(c);
    }


    @Test
    void registerChildrenWithInvalidTutorLogin() throws Exception {
        citizen.setEmail("child4@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa00001324");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData("NonExistant@bertsa.ca", DEFAULT_PASSWORD));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(citizen.getEmail(), citizen.getPassword(), true);
        assertNull(c);
    }


    @Test
    void registerChildrenWithoutTutor() throws Exception {
        citizen.setEmail("child2@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa22222222");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c2 = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(citizen.getEmail(), citizen.getPassword(), true);
        assertNull(c2);
    }

    @Test
    void registerChildrenWithInactiveTutor() throws Exception {
        Citizen adultInactive = new Citizen();
        adultInactive.setEmail("u2@bertsa.ca");
        adultInactive.setPassword(DEFAULT_PASSWORD);
        adultInactive.setFirstName("firstname");
        adultInactive.setLastName("lastname");
        adultInactive.setBirth(LocalDate.now().minusYears(26));
        adultInactive.setSex(Sex.MALE);
        adultInactive.setPhone("412-131-3131");
        adultInactive.setNoAssuranceMaladie("aaaa00001112");
        adultInactive.setAddress(address);
        adultInactive.setActive(false);

        Citizen saveInactive = citizenRepository.save(adultInactive);

        citizen.setEmail("child3@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa33333333");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData(adultInactive.getEmail(), adultInactive.getPassword()));


        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(citizen.getEmail(), citizen.getPassword(), true);
        assertNull(c);

        citizenRepository.delete(saveInactive);
    }

    @Test
    void registerNotValidNegativeChildrenTest() throws Exception {
        Citizen tooYoung = new Citizen();
        tooYoung.setEmail("tooYoung@bertsa.ca");
        tooYoung.setPassword(DEFAULT_PASSWORD);
        tooYoung.setFirstName("firstname");
        tooYoung.setLastName("lastname");
        tooYoung.setBirth(LocalDate.now().minusYears(16));
        tooYoung.setSex(Sex.MALE);
        tooYoung.setPhone("412-131-3131");
        tooYoung.setNoAssuranceMaladie("bbbb00001112");
        tooYoung.setAddress(address);

        Citizen saveTooYoung = citizenRepository.save(tooYoung);

        citizen.setEmail("child3@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa44444444");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData(tooYoung.getEmail(), tooYoung.getPassword()));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(citizen.getEmail(), citizen.getPassword(), true);
        assertNull(c);

        citizenRepository.delete(saveTooYoung);
    }

    @Test
    void registerEmailAlreadyUsed() throws Exception {
        citizen.setEmail(adultActive.getEmail());
        citizen.setNoAssuranceMaladie("dadw123212214");
        citizen.setBirth(LocalDate.now().minusYears(35));

        assertNotNull(citizenRepository.findByEmailIgnoreCase(citizen.getEmail()));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);

        assertNotNull(citizenRepository.findByEmailIgnoreCase(adultActive.getEmail()));
    }

    @Test
    void registerEmailNotValid() throws Exception {
        citizen.setEmail("not an email");
        citizen.setNoAssuranceMaladie("dadw12321221");
        citizen.setBirth(LocalDate.now().minusYears(67));
        int sizeBefore = citizenRepository.findAll().size();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerNoAssuranceMaladieAlreadyUsed() throws Exception {
        citizen.setEmail("freeEmail@bertsa.ca");
        citizen.setNoAssuranceMaladie(adultActive.getNoAssuranceMaladie());
        citizen.setBirth(LocalDate.now().minusYears(59));

        assertNotNull(citizenRepository.findByNoAssuranceMaladieIgnoreCase(citizen.getNoAssuranceMaladie()));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerNoAssuranceMaladieTooShort() throws Exception {
        citizen.setEmail("freeEmail2@bertsa.ca");
        citizen.setNoAssuranceMaladie("dadd0000111");
        citizen.setBirth(LocalDate.now().minusYears(58));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerNoAssuranceMaladieTooLong() throws Exception {
        citizen.setEmail("freeEmail2@bertsa.ca");
        citizen.setNoAssuranceMaladie("dadd000011112");
        citizen.setBirth(LocalDate.now().minusYears(58));

        int sizeBefore = citizenRepository.findAll().size();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenRepository.findAll().size();
        assertEquals(sizeBefore, sizeAfter);
    }

}