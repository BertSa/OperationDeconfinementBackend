package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AdminRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.AddressService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    CitizenRepository citizenRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AddressService addressService;
    @Autowired
    LicenseService licenseService;
    @Autowired
    SystemService systemService;

    @CrossOrigin
    @PostMapping("/login")
    public User login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {

        User user = adminRepository.findByEmailAndPasswordAndActive(email, password, true);
        if (user == null)
            user = citizenRepository.findByEmailIgnoreCaseAndPasswordAndActive(email, password, true);
        return user;
    }

    @CrossOrigin
    @PostMapping("/register/negative")
    public ResponseEntity<String> registerNegativeTest(@RequestBody @Valid CitizenData user) throws Exception {
        return createCitizen(user, TypeLicense.NegativeTest);
    }

    @CrossOrigin
    @PostMapping("/register/vaccine")
    public ResponseEntity<String> registerVaccine(@RequestBody @Valid CitizenData user) throws Exception {
        return createCitizen(user, TypeLicense.Vaccine);
    }

    private ResponseEntity<String> createCitizen(CitizenData user, TypeLicense typeLicense) throws Exception {
        Citizen userCreated = new Citizen();

        Address address = user.getAddress();
        Address save = addressService.createOrGetAddress(address.getZipCode(), address.getStreet(), address.getCity(), address.getProvince(), address.getApt());


        License licenseCreated = licenseService.createLicenseAtRegister(typeLicense, user.getBirth());
//        systemService.sendEmail(user.getEmail(), "CovidFreePass", "Here is your CovidFreePass", "id" + licenseCreated.getId());//TODO Dans un thread?

        userCreated.setEmail(user.getEmail());
        userCreated.setPassword(user.getPassword());
        userCreated.setLastName(user.getLastName());
        userCreated.setFirstName(user.getFirstName());
        userCreated.setNoAssuranceMaladie(user.getNoAssuranceMaladie());
        userCreated.setPhone(user.getPhone());
        userCreated.setBirth(user.getBirth());
        userCreated.setAddress(save);
        userCreated.setSex(user.getSex());
        userCreated.setLicense(licenseCreated);
        if (user.getTutor() != null)
            userCreated.setTutor(citizenRepository.findByEmailIgnoreCaseAndPassword(user.getTutor().getEmail(), user.getTutor().getPassword()));

        Citizen save1 = citizenRepository.save(userCreated);
        return ResponseEntity.ok((save1.getTutor()!=null)?"Children created":"Citizen created");
    }


}
