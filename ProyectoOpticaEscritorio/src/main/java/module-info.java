module com.prossofteam.optica_qualite {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;
    requires unirest.java;
    requires java.sql;
    requires optik.model;
    requires javax.ws.rs.api;


    opens com.prossofteam.optica_qualite to javafx.fxml;
    opens com.prossofteam.optica_qualite.modelo to gson;
    exports com.prossofteam.optica_qualite;
}