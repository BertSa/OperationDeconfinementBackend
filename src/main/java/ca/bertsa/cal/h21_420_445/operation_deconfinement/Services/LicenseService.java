package ca.bertsa.cal.h21_420_445.operation_deconfinement.Services;


import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

@Service
@Order(1)
public class LicenseService {
    @Resource
    SystemService systemService;
    @Resource
    LicenseRepository licenseRepository;

    public License createLicenseAtRegister(TypeLicense typeLicense, LocalDate birth) throws Exception {

        License license = new License();
        license.setType(typeLicense);
        license.setCategory(birth);

        License save = licenseRepository.save(license);
        systemService.generateQR(save.toString(), "id" + save.getId());
        systemService.generatePDF("id" + save.getId());

        return save;
    }

    public void deleteAll() {
        licenseRepository.deleteAll();
    }
}
