package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.SystemService;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.exceptions.CustomExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.HandlesTypes;
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
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid CitizenData user) {
        return systemService.registerCitizen(user);
    }
    @CrossOrigin
    @PostMapping("/complete/vaccine")
    public ResponseEntity<Object> completeInformationVaccine(@RequestBody @Valid Citizen user) throws Exception {
        return systemService.completeCitizen(user, TypeLicense.Vaccine);
    }
    @CrossOrigin
    @PostMapping("/complete/negative")
    public ResponseEntity<Object> completeInformationNegative(@RequestBody @Valid Citizen user) throws Exception {
        return systemService.completeCitizen(user, TypeLicense.Negative_Test);
    }


}
