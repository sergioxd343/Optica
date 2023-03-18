 package com.prossofteam.oq.bd; // Líneas = 29

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Paulo
 */
public class ConexionMySQL {

    Connection conn;

    public Connection open() {
        String user = "root";//se pone el usuario que definimos en MySQL Workbench
        String password = "10012003";//Se establece la contraseña que asigné
        String url = "jdbc:mysql://127.0.0.1:3306/optiqalumnos?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf-8";//se añade la URL

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//Se añade el driver de MySQL
            conn = DriverManager.getConnection(url, user, password);//Y le añadimos nuestras variables previas
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception controlada");
            }
        }
    }
}