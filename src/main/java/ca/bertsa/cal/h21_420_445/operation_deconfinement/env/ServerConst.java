package ca.bertsa.cal.h21_420_445.operation_deconfinement.env;

public interface ServerConst {
    byte MIN_AGE_SENIOR = 65;
    byte MIN_AGE_ADULT = 25;
    byte MIN_AGE_YOUNG_ADULT = 16;

    byte MIN_AGE_TUTOR = 18;
    byte NEGATIVE_TEST_DURATION = 14;

    String DIRECTORY_LICENSES = "licenses/";
    String PDF_FILENAME = "/document.pdf";
    String QR_FILENAME = "/qr.png";
    short QR_CODE_WIDTH = 300;
    short QR_CODE_HEIGHT = 300;

    String changePasswordUrl = "http://localhost:4200/pwd/change/";
}
