package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/api/user")
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

//    @GetMapping(value = "<ton url>/{email}", produces = MediaType.APPLICATION_PDF_VALUE)
//    @CrossOrigin
//    public ResponseEntity<Resource> pdf(@NotBlank @PathVariable String email) throws FileNotFoundException {
//        return systemService.pdff(email);
//    }

}
