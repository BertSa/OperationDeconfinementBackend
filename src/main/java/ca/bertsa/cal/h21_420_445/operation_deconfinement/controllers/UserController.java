package ca.bertsa.cal.h21_420_445.operation_deconfinement.controllers;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.LoginData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.FileNotFoundException;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private SystemService systemService;

    @CrossOrigin
    @PostMapping("/login")
    public User login(@RequestBody @Valid LoginData data) {
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

    @CrossOrigin
    @PostMapping("/update/{field}")
    public ResponseEntity<Citizen> update(@PathVariable String field, @RequestBody @Valid Citizen user) {
        if (field.equalsIgnoreCase("phone")) return systemService.updatePhone(user);
        else if (field.equalsIgnoreCase("address")) return systemService.updateAddress(user);
        else if (field.equalsIgnoreCase("password")) return systemService.updatePassword(user);

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @CrossOrigin
    @PostMapping("/renew/{type}")
    public ResponseEntity<Citizen> renew(@PathVariable TypeLicense type, @RequestBody @Valid Citizen user) throws Exception {
        return systemService.renew(type, user);
    }

    @CrossOrigin
    @PostMapping("/sendCopy")
    public ResponseEntity<Boolean> sendCopy(@RequestBody @Valid Citizen user) throws Exception {
        return systemService.sendLicenseCopy(user);
    }

    @CrossOrigin
    @PostMapping("/forgotPassword")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody String email) throws Exception {
        return systemService.forgotPassword(email);
    }

    @CrossOrigin
    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<Citizen> resetPassword(@PathVariable String token, @RequestBody String password) {
        return systemService.resetPassword(token, password);
    }

    @CrossOrigin
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody Citizen user) {
        return systemService.delete(user);
    }

    @GetMapping(value = "/{email}/qr.png")
    @CrossOrigin
    public ResponseEntity<byte[]> pdf(@Email @PathVariable String email) throws Exception {
        return systemService.qr(email);
    }

}
