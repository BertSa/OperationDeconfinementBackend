package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SystemServiceTest {
    @Autowired
    private SystemService systemService;

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(systemService);
    }

    @Test
    void createQrCodeTest() throws Exception {
        Files.deleteIfExists(Path.of("licenses/testQr/qr.png"));
        systemService.generateQR("data", "testQr");
        assertTrue(Files.exists(Path.of("licenses/testQr/qr.png")));
        systemService.generateQR("data", "testQr");
        assertTrue(Files.exists(Path.of("licenses/testQr/qr.png")));
    }

    @Test
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