package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Admin;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AdminRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OperationDeconfinementApplicationTests {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CitizenRepository citizenRepository;

    //    @Order(1)
    @Test
    void findUserTest() {
        Admin admin = adminRepository.findByEmailAndPasswordAndActive("admin@bertsa.ca", "admin", true);
        Admin admin1 = adminRepository.findByEmailAndPasswordAndActive("admin@admin.admin", "cisco", true);

        Citizen citizen1 = citizenRepository.findByEmailAndPasswordAndActive("samdddada@bertsa.ca", "aadadadmin", true);
        Citizen citizen2 = citizenRepository.findByEmailAndPasswordAndActive("sam@bertsa.ca", "admin", true);

        assertNull(admin);
        assertNotNull(admin1);

        assertNull(citizen1);
        assertNotNull(citizen2);
    }

}
