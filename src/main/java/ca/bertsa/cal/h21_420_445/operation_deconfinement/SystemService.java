package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.entities.License;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.LicenseRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Order(1)
public class SystemService {
    private final String directory = "licenses/";
    private final String qrFilename = "/qr.png";
    private final String pdfFilename = "/document.pdf";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CitizenRepository userRepository;

    @Autowired
    private LicenseRepository permisRepository;


    public boolean login(String str1, String str2) {
        return userRepository.findUserByEmailIgnoreCaseAndPassword(str1, str2) != null;
    }

    public boolean isLoginExist(String str) {
        return userRepository.findUserByEmailIgnoreCase(str) != null;
    }

    public List<License> AllPermis() {
        return permisRepository.findAll();
    }

    public void generateQR(String data, String subDirectory) throws Exception {
        createDirectoriesIfDontExists(subDirectory);
        Path path = FileSystems.getDefault().getPath(directory + subDirectory + qrFilename);
        QRCodeWriter qr = new QRCodeWriter();
        MatrixToImageWriter.writeToPath(qr.encode(data, BarcodeFormat.QR_CODE, 300, 300), "PNG", path);
    }

    private void createDirectoriesIfDontExists(String subDirectory) throws IOException {
        if (!Files.isDirectory(Path.of(directory + subDirectory))) {
            if (!Files.isDirectory(Path.of(directory))) {
                Files.createDirectory(Path.of(directory));
            }
            Files.createDirectory(Path.of(directory + subDirectory));
        }
    }

    public void generatePDF(String subDirectory) throws Exception {
        if (!Files.exists(Path.of(directory + subDirectory + qrFilename))) return;

        PdfWriter writer = new PdfWriter(directory + subDirectory + pdfFilename);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);


        Image image = new Image(ImageDataFactory.create(directory + subDirectory + qrFilename));

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
        helper.addAttachment("QR CODE", new File((directory + subDirectory + qrFilename)));
        helper.addAttachment("QR PDF", new File(directory + subDirectory + pdfFilename));

        mailSender.send(message);

    }

    public boolean isNoAssuranceMaladieExist(String value) {
        return userRepository.findUserByNoAssuranceMaladie(value) != null;
    }
}
