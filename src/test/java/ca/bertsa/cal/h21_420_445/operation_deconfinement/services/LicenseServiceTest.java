package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.env.ServerConst;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LicenseServiceTest {
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private LicenseRepository licenseRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(licenseService);
        assertNotNull(licenseRepository);
    }

    @Test
    void createLicenseAtRegisterTest() throws Exception {
        int sizeBefore = licenseRepository.findAll().size();
        License license1 = licenseService.createLicense(TypeLicense.NEGATIVETEST, LocalDate.now().minusYears(16));
        License license2 = licenseService.createLicense(TypeLicense.NEGATIVETEST, LocalDate.now().minusYears(15));
        License license3 = licenseService.createLicense(TypeLicense.NEGATIVETEST, LocalDate.now().minusYears(25));
        License license4 = licenseService.createLicense(TypeLicense.VACCINE, LocalDate.now().minusYears(65));
        int sizeAfter = licenseRepository.findAll().size();

        assertEquals(CategoryLicence.YoungAdult, license1.getCategory());
        assertEquals(CategoryLicence.Children, license2.getCategory());
        assertEquals(CategoryLicence.Adult, license3.getCategory());
        assertEquals(CategoryLicence.Senior, license4.getCategory());

        assertEquals(TypeLicense.NEGATIVETEST, license1.getType());
        assertEquals(TypeLicense.VACCINE, license4.getType());

        assertNotNull(license1.getDateExpire());
        assertEquals(LocalDate.now().plusDays(ServerConst.NEGATIVE_TEST_DURATION), license1.getDateExpire());
        assertNull(license4.getDateExpire());
        assertEquals(LocalDate.now(), license1.getDateCreation());

        assertEquals(sizeBefore + 4, sizeAfter);


    }

}