package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Citizen extends User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String noAssuranceMaladie;
    @Column(nullable = false)
    private LocalDate birth;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(nullable = false)
    private String phone;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Address address;
    @OneToOne
    private License license;

    private LocalDate dateJoined;
    @ManyToOne
    private Citizen tutor;


    public Citizen() {
        super();
        dateJoined = LocalDate.now();
    }
}
