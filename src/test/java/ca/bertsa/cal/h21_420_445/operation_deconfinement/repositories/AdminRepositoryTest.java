package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(adminRepository);
    }

    @Test
    void findByEmailAndPasswordAndActive() {
        Admin admin = adminRepository.findByEmailAndPasswordAndActive("admin@bertsa.ca", "admin", true);
        Admin admin1 = adminRepository.findByEmailAndPasswordAndActive("admin@bertsa.ca", "cisco", true);
        assertNull(admin);
        assertNotNull(admin1);
    }
}