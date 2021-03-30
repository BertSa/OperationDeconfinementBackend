package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.AddressModel;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.ChildrenModel;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenModel;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class UserControllerTest {
    static AddressModel addressModel;
    static CitizenModel citizenModel;
    static ChildrenModel childrenModel;
    private static CitizenModel citizenModel2;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    static void beforeAll() {
        String emaill = "samuel.b.prog@gmail.com";
        addressModel = new AddressModel("h8s3dac2",
                "39fafaef0 rue William-Macdonald",
                "Lachinafsfeeae",
                "qafefsc",
                "13");
        citizenModel = new CitizenModel(emaill,
                "123dad",
                "addddbert",
                "Saasssam",
                "dadd22122213",
                LocalDate.of(1969, 12, 11),
                Sex.MALE,
                "514-662-2313",
                addressModel);
        citizenModel2 = new CitizenModel(emaill,
                "diff",
                "diff",
                "diff",
                "dadw123212214",
                LocalDate.of(1959, 12, 11),
                Sex.FEMALE,
                "diff",
                addressModel);
        childrenModel = new ChildrenModel("sam@be.com",
                "123",
                "bert",
                "Sam",
                "dasw123233214",
                LocalDate.of(2010, 10, 11),
                Sex.MALE,
                "514-662-2313",
                addressModel, citizenModel);

    }

    @Test
    @Order(1)
    void loginTest() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/login")
                        .param("email", "sam@bertsa.ca")
                        .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn();
        this.mockMvc.perform(
                post("/api/user/login")
                        .param("email", "sammmmm@bertsa.ca")
                        .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andReturn();
    }

    @Test
    @Order(2)
    void registerTest() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/register/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizenModel)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User created"))
                .andReturn();

    }


    @Test
    @Order(3)
    void registerEmailNotValidTest() throws Exception {

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/register/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizenModel2)))
                .andExpect(status().isBadRequest())
                .andReturn();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/register/negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizenModel2)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


}