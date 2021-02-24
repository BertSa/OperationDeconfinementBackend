package ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
//    public User findByLoginAndPassword(String input1, String input2);

    public User findById(int id);
}
