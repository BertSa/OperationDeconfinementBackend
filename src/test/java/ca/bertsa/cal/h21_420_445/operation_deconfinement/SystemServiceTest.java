package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import org.apache.tomcat.jni.Directory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SystemServiceTest {
    @Autowired
    SystemService systemService;

    @Test
    @Order(1)
    void createQrCodeTest() throws Exception {
        Files.deleteIfExists(Path.of("licenses/testQr/qr.png"));
        systemService.generateQR("data", "testQr");
        assertTrue(Files.exists(Path.of("licenses/testQr/qr.png")));
        systemService.generateQR("data", "testQr");
        assertTrue(Files.exists(Path.of("licenses/testQr/qr.png")));
    }

    @Test
    @Order(2)
    void createPdfTest() throws Exception {
        Files.deleteIfExists(Path.of("licenses/testQr/document.pdf"));
        systemService.generatePDF("testQr");

        assertTrue(Files.exists(Path.of("licenses/testQr/document.pdf")));

        Files.deleteIfExists(Path.of("licenses/testQr/document.pdf"));
        Files.deleteIfExists(Path.of("licenses/testQr/qr.png"));
        systemService.generatePDF("testQr");

        assertFalse(Files.exists(Path.of("licenses/testQr/document.pdf")));
    }

}