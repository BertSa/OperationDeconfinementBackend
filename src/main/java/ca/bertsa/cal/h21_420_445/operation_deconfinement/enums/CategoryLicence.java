package ca.bertsa.cal.h21_420_445.operation_deconfinement.enums;

import java.time.LocalDate;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.ServerConst.*;

public enum CategoryLicence {
    Children, YoungAdult, Adult, Senior;

    @Override
    public String toString() {
        return super.name();
    }

    public static CategoryLicence getCategoryFromBirth(LocalDate birth) {
        if (birth.isBefore(LocalDate.now().minusYears(MIN_AGE_SENIOR).plusDays(1))) return CategoryLicence.Senior;
        if (birth.isBefore(LocalDate.now().minusYears(MIN_AGE_ADULT).plusDays(1))) return CategoryLicence.Adult;
        if (birth.isBefore(LocalDate.now().minusYears(MIN_AGE_YOUNG_ADULT).plusDays(1))) return CategoryLicence.YoungAdult;
        if (birth.isBefore(LocalDate.now())) return CategoryLicence.Children;

        throw new RuntimeException("Birth invalid!");
    }
}
