package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LicenseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Disabled
    @Test
    void ddTest() throws Exception {
        this.mockMvc.perform(
                post("/api/license/request")
                        .param("email", "sammmmm@bertsa.ca")
                        .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andReturn();
    }
}