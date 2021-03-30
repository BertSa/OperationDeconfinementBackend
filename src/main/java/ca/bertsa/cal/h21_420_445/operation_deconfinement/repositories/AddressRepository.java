package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findAddressByZipCodeAndStreetAndCityAndProvinceAndApt(String zipCode, String street, String city, String province, String apt);
}