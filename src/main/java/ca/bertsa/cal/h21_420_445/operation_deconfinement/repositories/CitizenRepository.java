package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Citizen findByEmailAndPasswordAndActive(String email, String password,Boolean active);

    Citizen findUserByEmailIgnoreCaseAndPassword(String email, String password);

    Citizen findUserByEmailIgnoreCase(String email);

    Citizen findUserByNoAssuranceMaladie(String value);
}
