package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Children extends Citizen {
    @ManyToOne
    private Citizen tutor;
}
