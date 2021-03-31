package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.*;

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


    public License() {
        dateCreation = LocalDate.now();
    }

    public void setCategory(LocalDate birth) {
        if (birth.isBefore(LocalDate.now().minusYears(SENIOR_AGE).plusDays(1))) {
            category = CategoryLicence.Senior;
        } else if (birth.isBefore(LocalDate.now().minusYears(ADULT_AGE).plusDays(1))) {
            category = CategoryLicence.Adult;
        } else if (birth.isBefore(LocalDate.now().minusYears(YOUNG_ADULT_AGE).plusDays(1))) {
            category = CategoryLicence.YoungAdult;
        } else {
            category = CategoryLicence.Children;
        }
    }

    public void setType(TypeLicense type) {
        this.type = Objects.requireNonNull(type);
        if (type == TypeLicense.NegativeTest) {
            dateExpire = LocalDate.now().plusDays(14);
        }

    }
}
