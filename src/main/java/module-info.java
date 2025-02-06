module org.example.mibocadillofx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.mibocadillofx to javafx.fxml;
    exports org.example.mibocadillofx;
}