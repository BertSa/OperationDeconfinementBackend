package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class LoginData {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}
