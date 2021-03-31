package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import ca.bertsa.cal.h21_420_445.operation_deconfinement.repositories.CitizenRepository;
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

import static ca.bertsa.cal.h21_420_445.operation_deconfinement.Consts.*;


@Service
@Order(1)
public class SystemService {
    private static final String IMAGE_FORMAT = "PNG";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CitizenRepository citizenRepository;

    public boolean login(String str1, String str2) {
        return citizenRepository.findByEmailIgnoreCaseAndPassword(str1, str2) != null;
    }

    public boolean isLoginExist(String str) {
        return citizenRepository.findByEmailIgnoreCase(str) != null;
    }


    public void generateQR(String data, String subDirectory) throws Exception {
        createDirectoriesIfDontExists(subDirectory);
        Path path = FileSystems.getDefault().getPath(DIRECTORY_LICENSES + subDirectory + QR_FILENAME);
        QRCodeWriter qr = new QRCodeWriter();
        MatrixToImageWriter.writeToPath(qr.encode(data, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT), IMAGE_FORMAT, path);
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

    public boolean isNoAssuranceMaladieExist(String value) {
        return citizenRepository.findByNoAssuranceMaladieIgnoreCase(value) != null;
    }
}
