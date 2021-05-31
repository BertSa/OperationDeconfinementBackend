package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.PasswordResetToken;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.exceptions.BertsaException;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.PasswordResetTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepo repo;

    public PasswordResetToken createPasswordResetToken(Citizen data) {
        PasswordResetToken token = new PasswordResetToken(data);
        return repo.save(token);
    }
    public PasswordResetToken validatePasswordResetToken(String token) {
        PasswordResetToken passToken = repo.findByToken(token);
        if(!isTokenFound(passToken)) throw new BertsaException("TokenNotFound");
        if(isTokenExpired(passToken)) throw new BertsaException("TokenExpired");
        return passToken;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return passToken.getExpiryDate().isBefore(LocalDate.now());
    }
}
