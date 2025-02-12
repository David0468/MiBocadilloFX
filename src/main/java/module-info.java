module org.example.mibocadillofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;



    opens org.example.mibocadillofx to javafx.fxml;
    exports org.example.mibocadillofx;
    exports org.example.mibocadillofx.controller;
    opens org.example.mibocadillofx.controller to javafx.fxml;
    opens org.example.mibocadillofx.model to org.hibernate.orm.core, javafx.fxml;
}