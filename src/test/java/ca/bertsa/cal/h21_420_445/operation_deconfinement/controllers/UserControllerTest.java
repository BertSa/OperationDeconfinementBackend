package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.EnvironmentServer;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.AddressService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.CitizenService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.LicenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense.Negative_Test;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense.Vaccine;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String API_USER = "/api/user";
    private static final String REGISTER_URL = API_USER + "/register";
    private static final String COMPLETE_URL = API_USER + "/complete";

    @Autowired
    private EnvironmentServer env;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SystemService systemService;
    @Autowired
    private CitizenService citizenService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private LicenseService licenseService;


    private Citizen adultActive;
    private Citizen vaccineValid;
    private Citizen vaccineNotValid;
    private Citizen negativeValid;
    private Citizen negativeNotValid;

    private Address address;
    private CitizenData citizen;

    @BeforeAll
    void beforeAll() {
        assertNotNull(env);
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(systemService);
        assertNotNull(citizenService);
        assertNotNull(addressService);
        assertNotNull(licenseService);

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

        citizenService.addOrUpdate(adultActive);

        CitizenData citizenData = new CitizenData();
        citizenData.setPassword(DEFAULT_PASSWORD);
        citizenData.setPhone("313-312-3212");

        citizenData.setEmail("vaccineValid@bertsa.ca");
        citizenData.setNoAssuranceMaladie("eeee11112235");
        vaccineValid = (Citizen) systemService.registerCitizen(citizenData).getBody();

        citizenData.setEmail("vaccineNotValid@bertsa.ca");
        citizenData.setNoAssuranceMaladie("eeee11112221");
        vaccineNotValid = (Citizen) systemService.registerCitizen(citizenData).getBody();

        citizenData.setEmail("negativeValid@bertsa.ca");
        citizenData.setNoAssuranceMaladie("eeee11112232");
        negativeValid = (Citizen) systemService.registerCitizen(citizenData).getBody();

        citizenData.setEmail("negativeNotValid@bertsa.ca");
        citizenData.setNoAssuranceMaladie("eeee11112233");
        negativeNotValid = (Citizen) systemService.registerCitizen(citizenData).getBody();

    }


    @BeforeEach
    void setUp() {
        citizen = new CitizenData();
        citizen.setPassword("password");
        citizen.setPhone("412-131-3131");
    }

    @Test
    void loginTest() throws Exception {
        assertNotNull(citizenService.findByEmailAndPassword(adultActive.getEmail(), adultActive.getPassword()));

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(API_USER + "/login")
                        .param(PARAM_EMAIL, adultActive.getEmail())
                        .param(PARAM_PASSWORD, adultActive.getPassword()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
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
                .andExpect(jsonPath("$.id").doesNotExist())
                .andDo(print());

    }

    @Test
    void register() throws Exception {
        citizen.setEmail("u3@bertsa.ca");
        citizen.setNoAssuranceMaladie("eeee11112222");

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").exists());

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    void registerBadEmail() throws Exception {
        citizen.setEmail(adultActive.getEmail());
        citizen.setNoAssuranceMaladie("eeee11112220");

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorEmail));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerBadNassmAlreadyExist() throws Exception {
        citizen.setEmail("u5@bertsa.ca");
        citizen.setNoAssuranceMaladie(adultActive.getNoAssuranceMaladie());

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorNassm));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerBadNassmNotInMinistere() throws Exception {
        citizen.setEmail("u6@bertsa.ca");
        citizen.setNoAssuranceMaladie("dddd11113333");

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorNassm));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerChildOnAdultRegister() throws Exception {
        citizen.setEmail("c1@bertsa.ca");
        citizen.setNoAssuranceMaladie("eeee11112231");

        int sizeBefore = citizenService.getNbOfCitizen();

        MvcResult mvcResult1 = this.mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        Citizen child = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(), Citizen.class);

        child.setAddress(address);
        this.mockMvc.perform(
                post(COMPLETE_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(child)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorTutor));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore + 1, sizeAfter);
    }


    @Test
    void completeValidNegative() throws Exception {
        CitizenData citizenData = new CitizenData();
        citizenData.setPassword(DEFAULT_PASSWORD);
        citizenData.setPhone("313-312-3212");
        citizenData.setEmail("u45@bertsa.ca");
        citizenData.setNoAssuranceMaladie("eeee11112226");
        Citizen negative = (Citizen) systemService.registerCitizen(citizenData).getBody();

        assertNotNull(negative);

        negative.setAddress(address);

        this.mockMvc.perform(
                post(COMPLETE_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negative)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.license").exists())
                .andExpect(jsonPath("$.license.type").value(Negative_Test.toString()));
    }

    @Test
    void completeValidVaccine() throws Exception {
        CitizenData citizenData = new CitizenData();
        citizenData.setPassword(DEFAULT_PASSWORD);
        citizenData.setPhone("313-312-3212");
        citizenData.setEmail("u46@bertsa.ca");
        citizenData.setNoAssuranceMaladie("eeee11112227");
        Citizen vaccine = (Citizen) systemService.registerCitizen(citizenData).getBody();

        assertNotNull(vaccine);

        vaccine.setAddress(address);

        this.mockMvc.perform(
                post(COMPLETE_URL + "/vaccine")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vaccine)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.license").exists())
                .andExpect(jsonPath("$.license.type").value(Vaccine.toString()));
    }

    @Test
    void completeValidNegativeButNotValidAddress() throws Exception {

        negativeValid.setAddress(null);

        this.mockMvc.perform(
                post(COMPLETE_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negativeValid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorAddress));
    }

    @Test
    void completeValidNegativeButAlreadyCompleted() throws Exception {
        CitizenData citizenData = new CitizenData();
        citizenData.setPassword(DEFAULT_PASSWORD);
        citizenData.setPhone("313-312-3212");
        citizenData.setEmail("u45@bertsa.ca");
        citizenData.setNoAssuranceMaladie("eeee11112229");
        Citizen negative = (Citizen) systemService.registerCitizen(citizenData).getBody();

        assertNotNull(negative);

        negative.setAddress(address);

        this.mockMvc.perform(
                post(COMPLETE_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negative)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.license").exists())
                .andExpect(jsonPath("$.license.type").value(Negative_Test.toString()));

        this.mockMvc.perform(
                post(COMPLETE_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negative)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorAlreadyCompleted));
    }

    @Test
    void completeButNotValid() throws Exception {

        negativeNotValid.setAddress(address);
        vaccineNotValid.setAddress(address);
        vaccineValid.setAddress(address);
        negativeValid.setAddress(address);

        this.mockMvc.perform(
                post(COMPLETE_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negativeNotValid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorNotEligibleForLicense + Negative_Test));
        this.mockMvc.perform(
                post(COMPLETE_URL + "/vaccine")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vaccineNotValid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorNotEligibleForLicense + Vaccine));

        this.mockMvc.perform(
                post(COMPLETE_URL + "/negative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vaccineValid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorNotEligibleForLicense + Negative_Test));

        this.mockMvc.perform(
                post(COMPLETE_URL + "/vaccine")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negativeValid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(env.messageErrorNotEligibleForLicense + Vaccine));
    }

}