package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence.getCategoryFromBirth;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.ServerConst.NEGATIVE_TEST_DURATION;

@Getter
@Entity
public class License implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeLicense type;
    @Enumerated(EnumType.STRING)
    private CategoryLicence category;
    private final LocalDate dateCreation;
    private LocalDate dateExpire;
    @ManyToOne
    private Citizen tutor;


    public License() {
        dateCreation = LocalDate.now();
    }

    public void setCategory(LocalDate birth) {
        category = getCategoryFromBirth(birth);
    }


    public void setType(TypeLicense type) {
        this.type = Objects.requireNonNull(type);
        if (type == TypeLicense.NEGATIVETEST) {
            dateExpire = LocalDate.now().plusDays(NEGATIVE_TEST_DURATION);
        }
    }


}
