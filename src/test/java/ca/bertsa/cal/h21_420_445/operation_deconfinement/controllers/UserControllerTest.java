package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.LoginData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.AddressService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.CitizenService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.LicenseService;
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

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.RESPONSE_MESSAGE_USER_CREATED;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.RESPONSE_MESSAGE_USER_CREATED_CHILDREN;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpellCheckingInspection")
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
    private CitizenService citizenService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private LicenseService licenseService;


    private Citizen adultActive;

    private Address address;
    private CitizenData citizen;

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(citizenService);
        assertNotNull(addressService);
        assertNotNull(licenseService);
    }

    @BeforeAll
    void before() {
        address = addressService.createOrGetAddress("h8s3dac2", "39fafaef0 rue William-Macdonald", "Lachinafsfeeae", "qafefsc", "13");

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


        citizenService.add(adultActive);
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
        licenseService.deleteAll();
    }

    @Test
    void loginTest() throws Exception {
        assertNotNull(citizenService.findByEmailAndPassword(adultActive.getEmail(), adultActive.getPassword()));

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(API_USER + "/login")
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

        assertNull(citizenService.findByEmailAndPassword(email, password));

        this.mockMvc.perform(
                post(API_USER + "/login")
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

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(RESPONSE_MESSAGE_USER_CREATED))
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore + 1, sizeAfter);

        Citizen c = citizenService.findByEmailAndPasswordAndActive(citizen.getEmail(), citizen.getPassword());

        assertNotNull(c);
        assertEquals(TypeLicense.NegativeTest, c.getLicense().getType());
        assertEquals(CategoryLicence.Senior, c.getLicense().getCategory());

        citizenService.delete(c);
    }

    @Test
    void registerVaccineTest() throws Exception {

        citizen.setEmail("u4@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa00001114");
        citizen.setBirth(LocalDate.now().minusYears(55));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/vaccine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(RESPONSE_MESSAGE_USER_CREATED))
                .andReturn();

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore + 1, sizeAfter);

        Citizen c = citizenService.findByEmailAndPasswordAndActive(citizen.getEmail(), citizen.getPassword());

        assertNotNull(c);
        assertEquals(TypeLicense.Vaccine, c.getLicense().getType());
        assertEquals(CategoryLicence.Adult, c.getLicense().getCategory());

        citizenService.delete(c);

    }

    @Test
    void registerNegativeChildrenTest() throws Exception {
        citizen.setEmail("child1@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa00001115");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData(adultActive.getEmail(), adultActive.getPassword()));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(RESPONSE_MESSAGE_USER_CREATED_CHILDREN))
                .andReturn();

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore + 1, sizeAfter);

        Citizen c = citizenService.findByEmailAndPasswordAndActive(citizen.getEmail(), citizen.getPassword());

        assertNotNull(c);
        assertEquals(TypeLicense.NegativeTest, c.getLicense().getType());
        assertEquals(CategoryLicence.Children, c.getLicense().getCategory());

        citizenService.delete(c);
    }


    @Test
    void registerChildrenWithInvalidTutorLogin() throws Exception {
        citizen.setEmail("child4@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa00001324");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData("NonExistant@bertsa.ca", DEFAULT_PASSWORD));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c = citizenService.findByEmailAndPasswordAndActive(citizen.getEmail(), citizen.getPassword());
        assertNull(c);
    }


    @Test
    void registerChildrenWithoutTutor() throws Exception {
        citizen.setEmail("child2@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa22222222");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c2 = citizenService.findByEmailAndPasswordAndActive(citizen.getEmail(), citizen.getPassword());
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

        Citizen saveInactive = citizenService.add(adultInactive);

        citizen.setEmail("child3@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa33333333");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData(adultInactive.getEmail(), adultInactive.getPassword()));


        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c = citizenService.findByEmailAndPasswordAndActive(citizen.getEmail(), citizen.getPassword());
        assertNull(c);

        citizenService.delete(saveInactive);
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

        Citizen saveTooYoung = citizenService.add(tooYoung);

        citizen.setEmail("child3@bertsa.ca");
        citizen.setNoAssuranceMaladie("aaaa44444444");
        citizen.setBirth(DEFAULT_BIRTH_CHILD);
        citizen.setTutor(new LoginData(tooYoung.getEmail(), tooYoung.getPassword()));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);

        Citizen c = citizenService.findByEmailAndPasswordAndActive(citizen.getEmail(), citizen.getPassword());
        assertNull(c);

        citizenService.delete(saveTooYoung);
    }

    @Test
    void registerEmailAlreadyUsed() throws Exception {
        citizen.setEmail(adultActive.getEmail());
        citizen.setNoAssuranceMaladie("dadw123212214");
        citizen.setBirth(LocalDate.now().minusYears(35));

        assertNotNull(citizenService.findByEmail(citizen.getEmail()));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);

        assertNotNull(citizenService.findByEmail(adultActive.getEmail()));
    }

    @Test
    void registerEmailNotValid() throws Exception {
        citizen.setEmail("not an email");
        citizen.setNoAssuranceMaladie("dadw12321221");
        citizen.setBirth(LocalDate.now().minusYears(67));
        int sizeBefore = citizenService.getNbOfCitizen();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerNoAssuranceMaladieAlreadyUsed() throws Exception {
        citizen.setEmail("freeEmail@bertsa.ca");
        citizen.setNoAssuranceMaladie(adultActive.getNoAssuranceMaladie());
        citizen.setBirth(LocalDate.now().minusYears(59));

        assertNotNull(citizenService.findByNoAssuranceMaladie(citizen.getNoAssuranceMaladie()));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerNoAssuranceMaladieTooShort() throws Exception {
        citizen.setEmail("freeEmail2@bertsa.ca");
        citizen.setNoAssuranceMaladie("dadd0000111");
        citizen.setBirth(LocalDate.now().minusYears(58));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerNoAssuranceMaladieTooLong() throws Exception {
        citizen.setEmail("freeEmail2@bertsa.ca");
        citizen.setNoAssuranceMaladie("dadd000011112");
        citizen.setBirth(LocalDate.now().minusYears(58));

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(REGISTER_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

}