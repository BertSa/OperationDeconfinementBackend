package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

import lombok.Getter;

@Getter
public class AddressModel {
    private String zipCode;
    private String street;
    private String city;
    private String province;
    private String apt;

    public AddressModel(String zipCode, String street, String city, String province, String apt) {
        this.zipCode = zipCode;
        this.street = street;
        this.city = city;
        this.province = province;
        this.apt = apt;
    }
}
