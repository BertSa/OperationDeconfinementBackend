package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;

import static org.junit.jupiter.api.Assertions.*;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AddressServiceTest {
    @Autowired
    private AddressService addressService;
    @Autowired
    AddressRepository addressRepository;

    @Test
    void createOrGetAddress() {
        int sizeBefore = addressRepository.findAll().size();
        Address address1 = addressService.createOrGetAddress("a1b2c3", "decari", "Montreal", "qc", null);
        Address address2 = addressService.createOrGetAddress("a1b2c3", "decari", "Montreal", "qc", null);
        Address address3 = addressService.createOrGetAddress("a1b2c3", "decari", "Montreal", "qc", "1");
        int sizeAfter = addressRepository.findAll().size();


        assertNotNull(address1);
        assertNotNull(address2);
        assertNotNull(address3);
        assertEquals(address1, address2);
        assertNotEquals(address1, address3);
        assertEquals(sizeBefore + 2, sizeAfter);


        addressRepository.deleteAll();
    }
}