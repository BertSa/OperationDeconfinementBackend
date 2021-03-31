package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Citizen findByEmailIgnoreCaseAndPasswordAndActive(String email, String password, Boolean active);

    Citizen findByEmailIgnoreCaseAndPasswordAndActiveAndBirthBefore(String email, String password, boolean active, LocalDate birth);

    Citizen findByEmailIgnoreCaseAndPassword(String email, String password);

    Citizen findByEmailIgnoreCase(String email);

    Citizen findByNoAssuranceMaladieIgnoreCase(String value);

}
