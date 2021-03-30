package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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
        if (birth.isBefore(LocalDate.now().minusYears(65))) {
            category = CategoryLicence.Senior;
        } else if (birth.isBefore(LocalDate.now().minusYears(25))) {
            category = CategoryLicence.Adult;
        } else if (birth.isBefore(LocalDate.now().minusYears(16))) {
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
