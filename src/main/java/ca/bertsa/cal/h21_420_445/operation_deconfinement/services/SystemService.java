package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Address;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.PasswordResetToken;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.User;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.models.CitizenData;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.enums.TypeLicense;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.exceptions.BertsaException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.MessagesError.*;
import static ca.bertsa.cal.h21_420_445.operation_deconfinement.env.ServerConst.*;
import static com.google.zxing.BarcodeFormat.QR_CODE;
import static org.springframework.http.ResponseEntity.ok;


@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SystemService {

    private static final String IMAGE_FORMAT = "PNG";

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CitizenService citizenService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    public User login(String email, String password) {
        Citizen citizen = citizenService.findByEmailAndPasswordAndActive(email, password);
        if (citizen == null)
            throw new BertsaException(MESSAGE_ERROR_LOGIN);
        return citizen;
    }

    public boolean isLoginExist(String email) {
        return citizenService.findByEmail(email) != null || adminService.findByEmail(email) != null;
    }

    public ResponseEntity<Citizen> registerCitizen(CitizenData user, TypeLicense type) {
        if (citizenService.isNotEligibleForLicense(type, user.getNoAssuranceMaladie()))
            throw new BertsaException(MESSAGE_ERROR_NOT_ELIGIBLE_FOR_LICENSE + type);
        Citizen citizenInfo = citizenService.getCitizenInfo(user.getNoAssuranceMaladie());
        if (citizenInfo == null)
            throw new BertsaException(MESSAGE_ERROR_OTHER);
        return ok(citizenService.register(user, citizenInfo));
    }

    public ResponseEntity<Citizen> completeCitizen(Citizen data) throws Exception {
        TypeLicense type = citizenService.getUserTypeValid(data.getNoAssuranceMaladie());
        Citizen user = citizenService.findByEmailAndPassword(data.getEmail(), data.getPassword());
        if (user.isProfileCompleted())
            throw new BertsaException(MESSAGE_ERROR_ALREADY_COMPLETED);
        if (data.getAddress() == null)
            throw new BertsaException(MESSAGE_ERROR_ADDRESS);
        if (licenseService.doesCitizenNeedTutor(user.getBirth())) {
            if (data.getTutor() == null)
                throw new BertsaException(MESSAGE_ERROR_TUTOR);
            Citizen tutor = citizenService.findByEmail(data.getTutor().getEmail());
            if (tutor == null) {
                throw new BertsaException(MESSAGE_ERROR_TUTOR);
            } else {
                user.setTutor(tutor);
            }
        }

        user.setAddress(addressService.createOrGetAddress(data.getAddress()));
        user.setLicense(licenseService.createLicense(type, user.getBirth()));
        user.setProfileCompleted(true);
        sendEmail(user.getEmail(), "CovidFreePass", "Here is your CovidFreePass", "id" + user.getLicense().getId());//TODO Dans un thread?
        return ok(citizenService.addOrUpdate(user));
    }

    public void generateQR(String data, String subDirectory) throws Exception {
        createDirectoriesIfDontExists(subDirectory);
        Path path = FileSystems.getDefault().getPath(DIRECTORY_LICENSES + subDirectory + QR_FILENAME);
        QRCodeWriter qr = new QRCodeWriter();
        MatrixToImageWriter.writeToPath(qr.encode(data, QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT), IMAGE_FORMAT, path);
    }

    private void createDirectoriesIfDontExists(String subDirectory) throws IOException {
        if (!Files.isDirectory(Path.of(DIRECTORY_LICENSES + subDirectory))) {
            if (!Files.isDirectory(Path.of(DIRECTORY_LICENSES))) {
                Files.createDirectory(Path.of(DIRECTORY_LICENSES));
            }
            Files.createDirectory(Path.of(DIRECTORY_LICENSES + subDirectory));
        }
    }

    public void generatePDF(String subDirectory) throws Exception {
        if (!Files.exists(Path.of(DIRECTORY_LICENSES + subDirectory + QR_FILENAME))) return;

        PdfWriter writer = new PdfWriter(DIRECTORY_LICENSES + subDirectory + PDF_FILENAME);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Image image = new Image(ImageDataFactory.create(DIRECTORY_LICENSES + subDirectory + QR_FILENAME));

        Paragraph p = new Paragraph("Bonjour Toi\n")
                .add(" Voici ton code permis de santé")
                .add(image);
        document.add(p);

        document.close();
    }

    private void sendEmail(String mailTo, String subject, String body) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mailTo);
        helper.setSubject(subject);
        helper.setText(body);

        mailSender.send(message);
    }

    public void sendEmail(String mailTo, String subject, String body, String subDirectory) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mailTo);
        helper.setSubject(subject);
        helper.setText(body);
        helper.addAttachment("QR CODE", new File((DIRECTORY_LICENSES + subDirectory + QR_FILENAME)));
        helper.addAttachment("QR PDF", new File(DIRECTORY_LICENSES + subDirectory + PDF_FILENAME));
        mailSender.send(message);

    }

    public ResponseEntity<Citizen> updatePhone(Citizen data) {
        Citizen user = citizenService.findByEmailAndPassword(data.getEmail(), data.getPassword());
        user.setPhone(data.getPhone());
        return ok(citizenService.addOrUpdate(user));
    }

    public ResponseEntity<Citizen> updateAddress(Citizen data) {
        Address address = addressService.createOrGetAddress(data.getAddress());
        Citizen user = citizenService.findByEmailAndPassword(data.getEmail(), data.getPassword());
        user.setAddress(address);
        return ok(citizenService.addOrUpdate(user));
    }

    public ResponseEntity<Citizen> updatePassword(Citizen data) {
        Citizen user = citizenService.findByEmail(data.getEmail());
        user.setPassword(data.getPassword());
        return ok(citizenService.addOrUpdate(user));
    }

    public ResponseEntity<Citizen> renew(TypeLicense type, Citizen data) throws Exception {
        Citizen user = citizenService.findByEmailAndPassword(data.getEmail(), data.getPassword());
        if (user == null) throw new BertsaException(MESSAGE_ERROR_LOGIN);
        if (citizenService.isNotEligibleForLicense(type, user.getNoAssuranceMaladie()))
            throw new BertsaException(MESSAGE_ERROR_NOT_ELIGIBLE_FOR_LICENSE + type);
        user.setLicense(licenseService.createLicense(type, user.getBirth()));
        return ok(citizenService.addOrUpdate(user));
    }

    public ResponseEntity<Boolean> sendLicenseCopy(Citizen user) throws Exception {
        Citizen data = this.citizenService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (data==null) throw new BertsaException("UserNotFound");
        if (data.getLicense().getDateExpire().isBefore(LocalDate.now())) throw new BertsaException("License Expired!");
        sendEmail(data.getEmail(), "CovidFreePass", "Here is your CovidFreePass", "id" + data.getLicense().getId());//TODO Dans un thread?

        return ok(true);
    }


    public ResponseEntity<Boolean> forgotPassword(@Valid @Email String email) throws Exception {
        Citizen data = this.citizenService.findByEmail(email);
        if (data == null) throw new BertsaException(MESSAGE_ERROR_EMAIL);

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(data);

        sendEmail(data.getEmail(), "Change Password", "Go to " + changePasswordUrl + passwordResetToken.getToken() + " to reset your password");//TODO Dans un thread?
        return ok(true);
    }

    public ResponseEntity<Citizen> resetPassword(String token, String password) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.validatePasswordResetToken(token);
        Citizen user = passwordResetToken.getUser();
        user.setPassword(password);
        return ok(citizenService.addOrUpdate(user));
    }

    public ResponseEntity<Boolean> delete(Citizen user) {
        Citizen citizen = citizenService.findByEmailAndPasswordAndActive(user.getEmail(), user.getPassword());
        if (citizen == null) throw new BertsaException(MESSAGE_ERROR_DELETION);
        citizen.setActive(false);
        return ok(!citizenService.addOrUpdate(citizen).isActive());
    }

    public boolean isActive(String email) {
        return citizenService.findByEmail(email).isActive();
    }

//    public void pdff(){
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//    }
}
