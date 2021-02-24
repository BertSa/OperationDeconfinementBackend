package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.*;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.CategoryLicence;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.Sex;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.AddressRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Order(1)
public class InsertData implements CommandLineRunner {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private void insert() {


        License license = new License();
        license.setCategory(CategoryLicence.Adult);
        license.setType(TypeLicense.Vaccine);
        License license2 = new License();
        license2.setCategory(CategoryLicence.YoungAdult);
        license2.setType(TypeLicense.NegativeTest);
        License license3 = new License();
        license3.setCategory(CategoryLicence.Children);
        license3.setType(TypeLicense.Vaccine);

        List<License> licensesData = Arrays.asList(license, license2, license3);
        this.licenseRepository.saveAll(licensesData);

        Address add = new Address("h8s3c2", "390 rue William-Macdonald", "Lachine", "qc", "3");
        Address add2 = new Address("h8s3dac2", "39fafaef0 rue William-Macdonald", "Lachinafsfeeae", "qafefsc", "13");

        List<Address> addressesData = Arrays.asList(add, add2);
        this.addressRepository.saveAll(addressesData);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 10);
        cal.set(Calendar.DATE, 11);
        cal.set(Calendar.YEAR, 1999);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.MONTH, 11);
        cal2.set(Calendar.DATE, 10);
        cal2.set(Calendar.YEAR, 1969);
        cal2.set(Calendar.HOUR, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);


        Admin admin = new Admin();
        admin.setActive(true);
        admin.setEmail("admin.admin@admin.admin");
        admin.setPassword("cisco");
        admin.setFirstName("Samsam");
        admin.setLastName("Bertrand");


        Citizen user = new Citizen();
        user.setNoAssuranceMaladie(123141241);
        user.setFirstName("Sam");
        user.setLastName("Bertrand");
        user.setAddress(add);
        user.setPassword("admin");
        user.setPhone("+1-555-555-5555");
        user.setSex(Sex.MALE);
        user.setEmail("admin@admin.admin");
        user.setBirth(cal.getTime());
        user.setLicense(license);
        user.setActive(true);


        Citizen user2 = new Citizen();
        user2.setNoAssuranceMaladie(312312312);
        user2.setFirstName("Samdad");
        user2.setLastName("Bertrandadadd");
        user2.setAddress(add2);
        user2.setPassword("admin");
        user2.setPhone("+1-555-555-5da555");
        user2.setSex(Sex.FEMALE);
        user2.setEmail("admin@admind.admin");
        user2.setBirth(cal2.getTime());
        user2.setLicense(license2);
        user2.setActive(true);


        Citizen user3 = new Citizen();
        user3.setNoAssuranceMaladie(312222332);
        user3.setFirstName("George");
        user3.setLastName("lol");
        user3.setAddress(add);
        user3.setPassword("pwd");
        user3.setPhone("+1-55541324145");
        user3.setSex(Sex.FEMALE);
        user3.setEmail("admin@lol.admin");
        user3.setBirth(new Date(System.currentTimeMillis()));
        user3.setLicense(license3);
        user3.setTutor(user);
        user3.setActive(true);


        List<User> userData = Arrays.asList(admin, user, user2, user3);
        this.userRepository.saveAll(userData);

    }

    private void cleanData() {
        this.licenseRepository.deleteAll();
        this.userRepository.deleteAll();
    }


    @Override
    public void run(String... args) throws Exception {
        cleanData();
        insert();

    }
}