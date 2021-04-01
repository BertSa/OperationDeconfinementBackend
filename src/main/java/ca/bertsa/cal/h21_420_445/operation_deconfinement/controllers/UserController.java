package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class UserController {

    @Autowired
    private SystemService systemService;

    @CrossOrigin
    @PostMapping("/login")
    public User login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {//TODO CHANGE FOR LoginData?
        return systemService.login(email, password);
    }

    @CrossOrigin
    @PostMapping("/register/negative")
    public ResponseEntity<String> registerNegativeTest(@RequestBody @Valid CitizenData user) throws Exception {
        return systemService.registerCitizen(user, TypeLicense.NegativeTest);
    }

    @CrossOrigin
    @PostMapping("/register/vaccine")
    public ResponseEntity<String> registerVaccine(@RequestBody @Valid CitizenData user) throws Exception {
        return systemService.registerCitizen(user, TypeLicense.Vaccine);
    }


}
