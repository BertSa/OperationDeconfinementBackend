package ca.bertsa.cal.h21_420_445.operation_deconfinement.converter;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TypeLicenseConverter implements Converter<String, TypeLicense> {

    @Override
    public TypeLicense convert(String value) {
        return TypeLicense.valueOf(value.toUpperCase());
    }

}
