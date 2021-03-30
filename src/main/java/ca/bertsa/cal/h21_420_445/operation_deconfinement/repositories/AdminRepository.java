package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Admin;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmailAndPasswordAndActive(String email, String password, boolean active);
}
