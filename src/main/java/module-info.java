module com.fileencryptor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.fileencryptor to javafx.fxml;
    exports com.fileencryptor;
}