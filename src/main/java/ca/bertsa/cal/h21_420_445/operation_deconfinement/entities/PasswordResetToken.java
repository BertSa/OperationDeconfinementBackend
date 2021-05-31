package ca.bertsa.cal.h21_420_445.operation_deconfinement.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    private long id;
    @ManyToOne
    private Citizen user;
    private String token;
    private LocalDate expiryDate;

    public PasswordResetToken(Citizen user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        expiryDate = LocalDate.now().plusDays(7);
    }
}
