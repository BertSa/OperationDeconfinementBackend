package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;


import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.env.ServerConst;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence.Children;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence.getCategoryFromBirth;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.ServerConst.licenceEndPoint;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class LicenseService {
    @Autowired
    private SystemService systemService;
    @Autowired
    private LicenseRepository licenseRepository;

    public License createLicense(TypeLicense typeLicense, LocalDate birth) throws Exception {

        License license = new License();
        license.setType(typeLicense);
        license.setCategory(birth);

        License save = licenseRepository.save(license);
        systemService.generateQR(licenceEndPoint+save.getId(), "id" + save.getId());
        systemService.generatePDF("id" + save.getId());

        return save;
    }

    public boolean doesCitizenNeedTutor(LocalDate birth) {
        return getCategoryFromBirth(birth).equals(Children);
    }
}
