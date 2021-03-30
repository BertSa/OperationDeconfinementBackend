package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.Services.LicenseService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.AddressModel;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenModel;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AdminRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    CitizenRepository citizenRepository;
    @Resource
    AdminRepository adminRepository;
    @Resource
    AddressRepository addressRepository;
    @Resource
    LicenseService licenseService;

    @Resource
    SystemService systemService;

    @CrossOrigin
    @PostMapping("/login")
    public User login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {

        User user = adminRepository.findByEmailAndPasswordAndActive(email, password, true);
        if (user == null)
            user = citizenRepository.findByEmailAndPasswordAndActive(email, password, true);
        return user;
    }

    @CrossOrigin
    @PostMapping("/register/negative")
    public ResponseEntity<String> registerNegativeTest(@RequestBody @Valid CitizenModel user) throws Exception {
        AddressModel address = user.getAddress();
        Address save = createOrGetAddress(address.getZipCode(), address.getStreet(), address.getCity(), address.getProvince(), address.getApt());
//        if (user instanceof ChildrenModel) {
//            ChildrenModel children = (ChildrenModel) user;
//            if (children.getBirth().isAfter(LocalDate.now().minusYears(16))) {
//                if (children.getTutor() != null) {
//                   return null;//TODO CHILDREN
//                } else {
//                    return null;//TODO NoActiveTutorFound
//                }
//            }
//        }


        License licenseCreated = licenseService.createLicenseAtRegister(TypeLicense.NegativeTest, user.getBirth());
        systemService.sendEmail(user.getEmail(), "CovidFreePass","Here is your CovidFreePass","id"+licenseCreated.getId());

        Citizen userCreated = new Citizen();
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

        citizenRepository.save(userCreated);
        return ResponseEntity.ok("User created");
    }

    Address createOrGetAddress(String zipCode, String street, String city, String province, @Nullable String apt) {
        Address address = addressRepository.findAddressByZipCodeAndStreetAndCityAndProvinceAndApt(zipCode, street, city, province, apt);
        if (address.getId() != null) {
            return address;
        }
        address = new Address(zipCode, street, city, province, apt);
        return addressRepository.save(address);
    }


}
