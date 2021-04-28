package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;


    public Address createOrGetAddress(Address address) {
        Address addressFromDB = addressRepository.findAddressByZipCodeIgnoreCaseAndStreetIgnoreCaseAndCityIgnoreCaseAndProvinceIgnoreCaseAndApt(address.getZipCode(), address.getStreet(), address.getCity(), address.getProvince(), address.getApt());
        if (addressFromDB != null) {
            return addressFromDB;
        }
        addressFromDB = new Address(address.getZipCode(), address.getStreet(), address.getCity(), address.getProvince(), address.getApt());
        return addressRepository.save(addressFromDB);
    }

    public Address createOrGetAddress(String zipCode, String street, String city, String province, @Nullable String apt) {
        return createOrGetAddress(new Address(zipCode, street, city, province, apt));
    }
}
