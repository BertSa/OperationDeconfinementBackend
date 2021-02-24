package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class License implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private TypeLicense type;
    private CategoryLicence category;


//    @Temporal(TemporalType.DATE)
//    Date valid;
//
//
//    @Temporal(TemporalType.TIMESTAMP)
//    Date expiration;
//    @NonNull
//    @Temporal(TemporalType.TIMESTAMP)
//    Date creationDateTime;



}
