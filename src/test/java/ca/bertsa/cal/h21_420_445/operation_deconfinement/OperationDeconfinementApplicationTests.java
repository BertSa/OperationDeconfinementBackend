package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Admin;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AdminRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OperationDeconfinementApplicationTests {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CitizenRepository citizenRepository;

    @Test
    void findAdminTest() {
        Admin admin = adminRepository.findByEmailAndPasswordAndActive("admin@bertsa.ca", "admin", true);
        Admin admin1 = adminRepository.findByEmailAndPasswordAndActive("admin@bertsa.ca", "cisco", true);
        assertNull(admin);
        assertNotNull(admin1);
    }

}
