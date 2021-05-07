package ca.bertsa.cal.h21_420_445.operation_deconfinement.services;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.Citizen;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public User login(String email, String password) {
        return citizenService.findByEmailAndPasswordAndActive(email, password);
    }

    public boolean isLoginExist(String email) {
        return citizenService.findByEmail(email) != null || adminService.findByEmail(email) != null;
    }

    public ResponseEntity<Object> registerCitizen(CitizenData user) {
        Citizen citizenInfo = citizenService.getCitizenInfo(user.getNoAssuranceMaladie());
        if (citizenInfo == null)
            throw new BertsaException(MESSAGE_ERROR_OTHER);


        return ok(citizenService.register(user, citizenInfo));
    }

    public ResponseEntity<Object> completeCitizen(Citizen data, TypeLicense type) throws Exception {
        Citizen user = citizenService.findByEmailAndPassword(data.getEmail(), data.getPassword());
        if (user.isProfileCompleted())
            throw new BertsaException(MESSAGE_ERROR_ALREADY_COMPLETED);
        if (data.getAddress() == null)
            throw new BertsaException(MESSAGE_ERROR_ADDRESS);
        if (citizenService.isNotEligibleForLicense(type, user.getNoAssuranceMaladie()))
            throw new BertsaException(MESSAGE_ERROR_NOT_ELIGIBLE_FOR_LICENSE + type);
        if (licenseService.doesCitizenNeedTutor(user.getBirth()))
            throw new BertsaException(MESSAGE_ERROR_TUTOR);

        user.setAddress(addressService.createOrGetAddress(data.getAddress()));
        user.setLicense(licenseService.createLicenseAtRegister(type, user.getBirth()));
        user.setProfileCompleted(true);
//            sendEmail(user.getEmail(), "CovidFreePass", "Here is your CovidFreePass", "id" + user.getLicense().getId());//TODO Dans un thread?
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

//    public void pdff(){
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//    }
}
