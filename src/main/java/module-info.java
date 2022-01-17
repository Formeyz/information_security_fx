module com.example.information_security_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.information_security_fx to javafx.fxml;
    exports com.example.information_security_fx;
}