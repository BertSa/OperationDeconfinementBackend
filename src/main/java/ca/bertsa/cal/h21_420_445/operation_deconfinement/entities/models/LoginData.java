package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class LoginData {
    @NotNull
    @Email
    private final String email;
    @NotNull
    private final String password;


    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
