package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.NoAssuranceMaladie;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.annotations.UniqueEmail;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class CitizenData implements Serializable {
    @NotNull
    @Email
    @UniqueEmail
    private String email;
    @NotNull
    private String password;
    @NotNull
    @NoAssuranceMaladie
    private String noAssuranceMaladie;
    @NotNull
    private String phone;
}
