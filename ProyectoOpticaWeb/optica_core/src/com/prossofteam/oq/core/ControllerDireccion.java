/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL_CP;
import com.prossofteam.oq.model.Colonia;
import com.prossofteam.oq.model.Direccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ControllerDireccion {

    public Direccion buscarMunEst(int cp) throws Exception {
        // Definimos la consulta que se comunica con la BD
        String sql = "{CALL ubicarDireccion(?)}";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL_CP connMySQL_CP = new ConexionMySQL_CP();
        //Abrimos la conexión con la Base de Datos:
        // Connection: gestiona la conexión con la BD. Prepara la base de datos para recibir llamadas a StoreProcedures, consultas, cerrar la conexion, abrir la conexion
        Connection conn = connMySQL_CP.open();
        //Con este objeto ejecutaremos la consulta:
        // PreparedStatement: permite ejecutar cualquier instrucción (insert, update, drop, delete, create, alter, etc.) sql en el SGBD
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Variable tipo Direccion que ocuparemos para guardar resultados
        Direccion direccion = new Direccion();
        //Aquí guardaremos los resultados de la consulta:
        // ResultSet: contiene los resultados que devuelve la base de datos después de una instrucción sql
        ResultSet rs = null;
        // Añadimos los parametros a la consulta, en este caso al StoreProcedure
        pstmt.setInt(1, cp);
        // Ejecutamos la consulta
        rs = pstmt.executeQuery();
        // Extraemos los datos de la consulta y los almacenamos
        while (rs.next()) {
            direccion.setEstado(rs.getString("nombreEstado"));
            direccion.setMunicipio(rs.getString("nombreMunicipio"));
        }

        String[] colonias = buscarColonias(cp);
        direccion.setColonias(colonias);

        rs.close();
        pstmt.close();
        connMySQL_CP.close();

        return direccion;
    }

    public String [] buscarColonias(int cp) throws Exception {
        // Definimos la consulta que se comunica con la BD
        String sql = "{CALL readCpByCP(?)}";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL_CP connMySQL_CP = new ConexionMySQL_CP();
        //Abrimos la conexión con la Base de Datos:
        // Connection: gestiona la conexión con la BD. Prepara la base de datos para recibir llamadas a StoreProcedures, consultas, cerrar la conexion, abrir la conexion
        Connection conn = connMySQL_CP.open();
        //Con este objeto ejecutaremos la consulta:
        // PreparedStatement: permite ejecutar cualquier instrucción (insert, update, drop, delete, create, alter, etc.) sql en el SGBD
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Variable tipo Direccion que ocuparemos para guardar resultados
        String [] colonias = null;
        String colonia = "";
        int i = 0;
        //Aquí guardaremos los resultados de la consulta:
        // ResultSet: contiene los resultados que devuelve la base de datos después de una instrucción sql
        ResultSet rs = null;
        // Añadimos los parametros a la consulta, en este caso al StoreProcedure
        pstmt.setInt(1, cp);
        // Ejecutamos la consulta
        rs = pstmt.executeQuery();
        // Extraemos los datos de la consulta y los almacenamos
        while (rs.next()) {
            i++;
        }
        colonias = new String[i];
        i = 0;
        rs = null;
        rs = pstmt.executeQuery();
        while(rs.next()){
            // Datos de Colonia
            colonia = rs.getString("nombreColonia");
            colonias[i] = colonia;
            i++;
        }

        rs.close();
        pstmt.close();
        connMySQL_CP.close();

        return colonias;
    }
}
