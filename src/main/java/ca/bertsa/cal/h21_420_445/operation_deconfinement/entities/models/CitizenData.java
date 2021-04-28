package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

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
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String noAssuranceMaladie;
    @NotNull
    private String phone;
}
