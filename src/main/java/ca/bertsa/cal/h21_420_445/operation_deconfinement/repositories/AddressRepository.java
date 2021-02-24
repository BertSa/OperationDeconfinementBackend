package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    public Address findById(int id);
}