package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/license")
public class LicenseController {
    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private CitizenRepository citizenRepository;

}
