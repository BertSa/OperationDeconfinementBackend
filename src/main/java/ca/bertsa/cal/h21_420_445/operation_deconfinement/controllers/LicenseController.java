package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Children;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Optional;

@RequestMapping("/api/license")
public class LicenseController {
    @Resource
    private LicenseRepository licenseRepository;

    @Resource
    private CitizenRepository citizenRepository;

    @CrossOrigin
    @PostMapping("/children/request/negative")
    public Citizen requestChildrenNegativeTest(@RequestParam("user") Children user) {
        Optional<Citizen> byId = citizenRepository.findById(user.getId());
        if (byId.isPresent()) {
            if (!byId.get().equals(user)) {
                return null;//TODO UserNotTheSame
            }
        } else {
            return null;//TODO UserNotFound
        }
        if (user.getBirth().isBefore(LocalDate.now().minusYears(16))) {
            return null;//TODO BECOME A YOUNG_ADULT
        }
        if (!user.getTutor().isActive()) {
            return null;//TODO TUTOR INACTIVE
        }
        License license = new License();
        license.setType(TypeLicense.NegativeTest);
        license.setCategory(user.getBirth());
        user.setLicense(license);
        licenseRepository.save(license);
        citizenRepository.save(user);
        return user;
    }
}
