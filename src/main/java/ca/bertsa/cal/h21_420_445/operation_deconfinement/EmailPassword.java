package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import lombok.Getter;

@Getter
public class EmailPassword {
    private final String email;
    private final String password;

    public EmailPassword(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
