/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.PresupuestoLentesdeContacto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControllerPresupuesto {
    public boolean generarPresupuestoLC(PresupuestoLentesdeContacto plc)
    {
        boolean r = false;
        try {
            
            //Con este objeto nos vamos a conectar a la Base de Datos:
            ConexionMySQL connMySQL = new ConexionMySQL();
            
            //Abrimos la conexión con la Base de Datos:
            Connection conn = connMySQL.open();
            Statement stmt = conn.createStatement();
            
            String query = "INSERT INTO presupuesto_lentescontacto (idExamenVista, idLenteContacto, clave) VALUES ("
                    + plc.getExamenVista().getIdExamenVista() +","
                    + plc.getLenteContacto().getIdLenteContacto() + "'"
                    + plc.getClave() +"')";
            
            stmt.execute(query);
            r = true;
            stmt.close();
            conn.close();
            connMySQL.close();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPresupuesto.class.getName()).log(Level.SEVERE, null, ex);
            r = false;
        }
        return r;
    }
}
