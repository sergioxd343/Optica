module org.utl.dsm.optica.dsm404optica {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    requires gson;
    requires unirest.java;
    requires optica.model;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


    opens org.utl.dsm.optica.dsm404optica.modelo to gson;

    opens org.utl.dsm.optica.dsm404optica to  javafx.fxml;
    exports org.utl.dsm.optica.dsm404optica;
}