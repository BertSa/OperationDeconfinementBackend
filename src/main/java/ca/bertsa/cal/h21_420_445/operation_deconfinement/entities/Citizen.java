package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Citizen extends User implements Serializable {
    @Column(unique = true, nullable = false)
    private int noAssuranceMaladie;
    @Column(nullable = false)
    private Date birth;
    @Column(nullable = false)
    private Sex sex;
    @Column(nullable = false)
    private String phone;
    @ManyToOne
    @Column(nullable = false)
    private Address address;
    @ManyToOne(fetch = FetchType.LAZY)
    private Citizen tutor;
    @OneToOne
    @Column(nullable = false)
    private License license;

    public Citizen() {
    }
}
