package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.validators.UniqueEmail;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.validators.UniqueNoAssuranceMaladie;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CitizenModel implements Serializable {
    @NotNull
    @Email
    @UniqueEmail
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String lastName;
    @NotNull
    private String firstName;
    @NotNull
    @UniqueNoAssuranceMaladie
    private String noAssuranceMaladie;
    @NotNull
    private LocalDate birth;
    @NotNull
    private Sex sex;
    @NotNull
    private String phone;
    @NotNull
    private AddressModel address;

    public CitizenModel(String email, String password, String lastName, String firstName, String noAssuranceMaladie, LocalDate birth, Sex sex, String phone, AddressModel address) {
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.noAssuranceMaladie = noAssuranceMaladie;
        this.birth = birth;
        this.sex = sex;
        this.phone = phone;
        this.address = address;
    }
}
