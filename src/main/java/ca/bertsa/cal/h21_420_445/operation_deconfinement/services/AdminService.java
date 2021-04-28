package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Admin;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin findByEmail(String email) {
        return adminRepository.findByEmailIgnoreCase(email);
    }
}
