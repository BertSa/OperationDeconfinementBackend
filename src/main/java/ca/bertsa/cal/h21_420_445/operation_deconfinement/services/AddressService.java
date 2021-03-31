package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;


    public Address createOrGetAddress(String zipCode, String street, String city, String province, @Nullable String apt) {
        Address address = addressRepository.findAddressByZipCodeIgnoreCaseAndStreetIgnoreCaseAndCityIgnoreCaseAndProvinceIgnoreCaseAndApt(zipCode, street, city, province, apt);
        if (address != null) {
            return address;
        }
        address = new Address(zipCode, street, city, province, apt);
        return addressRepository.save(address);
    }

}
