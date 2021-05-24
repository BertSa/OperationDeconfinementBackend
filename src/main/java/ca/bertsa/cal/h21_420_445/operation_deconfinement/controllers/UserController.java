package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.LoginData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private SystemService systemService;

    @CrossOrigin
    @PostMapping("/login")
    public User login(@RequestBody @Valid LoginData data) {//TODO CHANGE FOR LoginData?
        return systemService.login(data.getEmail(), data.getPassword());
    }

    @CrossOrigin
    @PostMapping("/register/{type}")
    public ResponseEntity<Citizen> register(@PathVariable TypeLicense type, @RequestBody @Valid CitizenData user) {
        return systemService.registerCitizen(user, type);
    }

    @CrossOrigin
    @PostMapping("/complete")
    public ResponseEntity<Citizen> completeInformationVaccine(@RequestBody @Valid Citizen user) throws Exception {
        return systemService.completeCitizen(user);
    }

//    @GetMapping(value = "<ton url>/{email}", produces = MediaType.APPLICATION_PDF_VALUE)
//    @CrossOrigin
//    public ResponseEntity<Resource> pdf(@NotBlank @PathVariable String email) throws FileNotFoundException {
//        return systemService.pdff(email);
//    }

}
