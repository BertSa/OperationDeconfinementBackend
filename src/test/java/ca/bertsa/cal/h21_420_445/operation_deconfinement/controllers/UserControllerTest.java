package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.AddressService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.CitizenService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.LicenseService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.SystemService;
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

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence.Children;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense.NEGATIVETEST;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense.VACCINE;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.MessagesError.*;
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
    private Citizen negativeValid;

    private Address address;
    private CitizenData citizen;

    @BeforeAll
    void beforeAll() {
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
        citizenData.setNoAssuranceMaladie("QTWT14911550");
        vaccineValid = systemService.registerCitizen(citizenData, VACCINE).getBody();


        citizenData.setEmail("negativeValid@bertsa.ca");
        citizenData.setNoAssuranceMaladie("QVXJ99808420");
        negativeValid = systemService.registerCitizen(citizenData, NEGATIVETEST).getBody();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value(MESSAGE_ERROR_LOGIN))
                .andDo(print());

    }

    @Test
    void register() throws Exception {
        citizen.setEmail("u3@bertsa.ca");
        citizen.setNoAssuranceMaladie("eeee11112222");

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL + "/" + NEGATIVETEST)
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
        citizen.setNoAssuranceMaladie("PZZR66096308");

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL + "/" + VACCINE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value(MESSAGE_ERROR_EMAIL));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerBadNassmAlreadyExist() throws Exception {
        citizen.setEmail("u5@bertsa.ca");
        citizen.setNoAssuranceMaladie(adultActive.getNoAssuranceMaladie());

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL + "/" + VACCINE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value(MESSAGE_ERROR_NASSM_REGISTERED));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerBadNassmNotInMinistere() throws Exception {
        citizen.setEmail("u6@bertsa.ca");
        citizen.setNoAssuranceMaladie("dddd11113333");

        int sizeBefore = citizenService.getNbOfCitizen();

        this.mockMvc.perform(
                post(REGISTER_URL + "/" + VACCINE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value(MESSAGE_ERROR_NASSM_DOESNT_EXIST));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void registerChild() throws Exception {
        citizen.setEmail("c1@bertsa.ca");
        citizen.setNoAssuranceMaladie("QRGB91973077");

        int sizeBefore = citizenService.getNbOfCitizen();

        MvcResult mvcResult1 = this.mockMvc.perform(
                post(REGISTER_URL + "/" + NEGATIVETEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        Citizen child = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(), Citizen.class);

        child.setAddress(address);
        child.setTutor(adultActive);
        this.mockMvc.perform(
                post(COMPLETE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(child)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.license.id").exists())
                .andExpect(jsonPath("$.license.type").value(NEGATIVETEST.toString()))
                .andExpect(jsonPath("$.license.category").value(Children.toString()))
        ;

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    void registerChildWithoutAdult() throws Exception {
        citizen.setEmail("c1@bertsa.ca");
        citizen.setNoAssuranceMaladie("QRGB91973077");

        int sizeBefore = citizenService.getNbOfCitizen();

        MvcResult mvcResult1 = this.mockMvc.perform(
                post(REGISTER_URL + "/" + NEGATIVETEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        Citizen child = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(), Citizen.class);

        child.setAddress(address);
        this.mockMvc.perform(
                post(COMPLETE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(child)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value(MESSAGE_ERROR_TUTOR));

        int sizeAfter = citizenService.getNbOfCitizen();
        assertEquals(sizeBefore + 1, sizeAfter);
    }


    @Test
    void completeValidNegative() throws Exception {
        CitizenData citizenData = new CitizenData();
        citizenData.setPassword(DEFAULT_PASSWORD);
        citizenData.setPhone("313-312-3212");
        citizenData.setEmail("u45@bertsa.ca");
        citizenData.setNoAssuranceMaladie("ODXD45401716");
        Citizen negative = systemService.registerCitizen(citizenData, NEGATIVETEST).getBody();

        assertNotNull(negative);

        negative.setAddress(address);

        this.mockMvc.perform(
                post(COMPLETE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negative)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.license").exists())
                .andExpect(jsonPath("$.license.type").value(NEGATIVETEST.toString()));
    }

    @Test
    void completeValidVaccine() throws Exception {
        CitizenData citizenData = new CitizenData();
        citizenData.setPassword(DEFAULT_PASSWORD);
        citizenData.setPhone("313-312-3212");
        citizenData.setEmail("u46@bertsa.ca");
        citizenData.setNoAssuranceMaladie("NOFR47914719");
        Citizen vaccine = systemService.registerCitizen(citizenData, VACCINE).getBody();

        assertNotNull(vaccine);

        vaccine.setAddress(address);

        this.mockMvc.perform(
                post(COMPLETE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vaccine)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.license").exists())
                .andExpect(jsonPath("$.license.type").value(VACCINE.toString()));
    }

    @Test
    void completeValidNegativeButNotValidAddress() throws Exception {

        negativeValid.setAddress(null);

        this.mockMvc.perform(
                post(COMPLETE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negativeValid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value(MESSAGE_ERROR_ADDRESS));
    }

    @Test
    void completeValidNegativeButAlreadyCompleted() throws Exception {
        CitizenData citizenData = new CitizenData();
        citizenData.setPassword(DEFAULT_PASSWORD);
        citizenData.setPhone("313-312-3212");
        citizenData.setEmail("u45@bertsa.ca");
        citizenData.setNoAssuranceMaladie("NShR53841129");
        Citizen negative = systemService.registerCitizen(citizenData, NEGATIVETEST).getBody();

        assertNotNull(negative);

        negative.setAddress(address);

        this.mockMvc.perform(
                post(COMPLETE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negative)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.license").exists())
                .andExpect(jsonPath("$.license.type").value(NEGATIVETEST.toString()));

        this.mockMvc.perform(
                post(COMPLETE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(negative)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value(MESSAGE_ERROR_ALREADY_COMPLETED));
    }


}