package com.prossofteam.oq.bd;

/**
 *
 * @author Paulo
 */
public class PruebaConexion {

    public static void main(String[] args) {
        ConexionMySQL connMySQL = new ConexionMySQL();
        ConexionMySQL_CP connMySQL_CP = new ConexionMySQL_CP();

        try {
            connMySQL.open();
            connMySQL_CP.open();
            System.out.println("Conexión estable");

            connMySQL.close();
            connMySQL_CP.close();
            System.out.println("Se ha cerrado la conexion con MySQL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
