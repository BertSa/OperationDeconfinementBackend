package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.Columns;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String zipCode;
    private String street;
    private String city;
    private String province;
    private String apt;

    public Address() {

    }

    /**
     * @param zipCode  Postal code
     * @param apt      Appartement number (When applicable)
     * @param street   Street name
     * @param city     City
     * @param province Province
     */
    public Address(@NonNull String zipCode, @NonNull String street, @NonNull String city, @NonNull String province, String apt) {
        this.zipCode = zipCode;
        this.apt = apt;
        this.street = street;
        this.city = city;
        this.province = province;
    }
}
