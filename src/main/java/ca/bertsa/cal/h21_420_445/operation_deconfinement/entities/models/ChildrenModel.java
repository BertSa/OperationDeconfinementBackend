package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
public class ChildrenModel extends CitizenModel implements Serializable {


    private CitizenModel tutor;

    public ChildrenModel(String email, String password, String lastName, String firstName, String noAssuranceMaladie, LocalDate birth, Sex sex, String phone, AddressModel address, CitizenModel tutor) {
        super(email, password, lastName, firstName, noAssuranceMaladie, birth, sex, phone, address);
        this.tutor = tutor;
    }
}
