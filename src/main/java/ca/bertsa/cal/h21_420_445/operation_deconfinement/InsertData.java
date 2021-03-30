package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.*;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AdminRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@Order(4)
public class InsertData implements CommandLineRunner {

    @Resource
    private LicenseRepository licenseRepository;

    @Resource
    private AdminRepository adminRepository;
    @Resource
    private CitizenRepository citizenRepository;

    @Resource
    private AddressRepository addressRepository;

    private void insert() {

        License license = new License();
        license.setCategory(LocalDate.now().minusYears(17));
        license.setType(TypeLicense.Vaccine);
        License license2 = new License();
        license2.setCategory(LocalDate.now().minusYears(77));
        license2.setType(TypeLicense.NegativeTest);
        License license3 = new License();
        license3.setCategory(LocalDate.now().minusYears(6));
        license3.setType(TypeLicense.Vaccine);

        List<License> licensesData = Arrays.asList(license, license2, license3);
        licenseRepository.saveAll(licensesData);
        Address add = new Address("h8s3c2", "390 rue William-Macdonald", "Lachine", "qc", "3");
        Address add2 = new Address("h8s3dac2", "39fafaef0 rue William-Macdonald", "Lachinafsfeeae", "qafefsc", "13");

        List<Address> addressesData = Arrays.asList(add, add2);
        this.addressRepository.saveAll(addressesData);


        Admin admin = new Admin();
        admin.setActive(true);
        admin.setEmail("admin@admin.admin");
        admin.setPassword("cisco");
        admin.setFirstName("Samsam");
        admin.setLastName("Bertrand");


        Citizen user = new Citizen();
        user.setNoAssuranceMaladie("dlwi123141241");
        user.setFirstName("Sam");
        user.setLastName("Bertrand");
        user.setAddress(add);
        user.setPassword("admin");
        user.setPhone("+1-555-555-5555");
        user.setSex(Sex.MALE);
        user.setEmail("sam@bertsa.ca");
        user.setBirth(LocalDate.now().minusYears(17));
        user.setLicense(license);
        user.setActive(true);


        Citizen user2 = new Citizen();
        user2.setNoAssuranceMaladie("dawt312312312");
        user2.setFirstName("Samdad");
        user2.setLastName("Bertrandadadd");
        user2.setAddress(add2);
        user2.setPassword("admin");
        user2.setPhone("+1-555-555-5da555");
        user2.setSex(Sex.FEMALE);
        user2.setEmail("admin@admind.admin");
        user2.setBirth(LocalDate.now().minusYears(77));
        user2.setLicense(license2);
        user2.setActive(true);


        Children user3 = new Children();
        user3.setNoAssuranceMaladie("dawe312222332");
        user3.setFirstName("George");
        user3.setLastName("lol");
        user3.setAddress(add);
        user3.setPassword("pwd");
        user3.setPhone("+1-55541324145");
        user3.setSex(Sex.FEMALE);
        user3.setEmail("admin@lol.admin");
        user3.setBirth(LocalDate.now().minusYears(6));
        user3.setLicense(license3);
        user3.setTutor(user);
        user3.setActive(true);


        List<Admin> userData = Arrays.asList(admin);
        List<Citizen> citizensData = Arrays.asList(user, user2, user3);
        this.adminRepository.saveAll(userData);
        this.citizenRepository.saveAll(citizensData);

    }

    private void cleanData() {
        this.licenseRepository.deleteAll();
        this.citizenRepository.deleteAll();
    }


    @Override
    public void run(String... args) throws Exception {
        cleanData();
        insert();

    }
}