package org.utl.dsm.optica.dsm404optica.controlador;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {
    public static Connection conn;

    public Connection open() {
        String user = "Daniel Lopez", password = "Logd031211";
        String url = "jdbc:mysql://127.0.0.1:3306/optiqalumnos?useSSL=false&useUnicode=true&characterEncoding=utf-8";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, user, password);

            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if(conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("Exception controlada");
            }
        }
    }
}
