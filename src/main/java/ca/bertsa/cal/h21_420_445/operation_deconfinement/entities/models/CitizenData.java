package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.TutorNeededOrNot;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.UniqueEmail;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.UniqueNoAssuranceMaladie;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@TutorNeededOrNot(birth = "birth", tutor = "tutor")
@NoArgsConstructor
public class CitizenData implements Serializable {
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
    private Address address;
    private LoginData tutor;
}
