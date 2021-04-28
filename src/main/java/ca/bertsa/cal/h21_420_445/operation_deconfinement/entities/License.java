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
    @ManyToOne
    private Citizen tutor;


    public License() {
        dateCreation = LocalDate.now();
    }

    public void setCategory(LocalDate birth) {
        category = getCategoryFromBirth(birth);
    }

    public static CategoryLicence getCategoryFromBirth(LocalDate birth) {
        if (birth.isBefore(LocalDate.now().minusYears(SENIOR_AGE).plusDays(1))) return CategoryLicence.Senior;
        if (birth.isBefore(LocalDate.now().minusYears(ADULT_AGE).plusDays(1))) return CategoryLicence.Adult;
        if (birth.isBefore(LocalDate.now().minusYears(YOUNG_ADULT_AGE).plusDays(1))) return CategoryLicence.YoungAdult;
        if (birth.isBefore(LocalDate.now())) return CategoryLicence.Children;

        throw new RuntimeException("Birth invalid!");
    }

    public void setType(TypeLicense type) {
        this.type = Objects.requireNonNull(type);
        if (type == TypeLicense.Negative_Test) {
            dateExpire = LocalDate.now().plusDays(NEGATIVE_TEST_DURATION);
        }

    }


}
