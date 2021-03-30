package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.validators.UniqueEmail;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class User implements Serializable {
    private boolean active;
    @Column(nullable = false)
//    @UniqueEmail //TODO MAKE A SQL SCRIPT
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String firstName;

}
