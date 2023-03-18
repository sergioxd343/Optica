/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Tratamiento;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class ControllerTratamiento {

    public int insert(Tratamiento t) throws Exception {

        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL insertarTratamiento(?, ?, ?, ?)}";  // Valores de Tratamiento

        int idTratamientoGenerado = -1;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos de Tratamiento en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Registramos datos de Tratamiento
        cstmt.setString(1, t.getNombre());
        cstmt.setDouble(2, t.getPrecioCompra());
        cstmt.setDouble(3, t.getPrecioVenta());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(4, Types.INTEGER);

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idTratamientoGenerado = cstmt.getInt(4);

        // Asignamos los id´s generados al objeto Empleado
        t.setIdTratamiento(idTratamientoGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idTratamientoGenerado;
    }

    public void update(Tratamiento t) throws Exception {

        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL actualizarTratamiento(?, ?, ?, ?)}"; // Datos de Tratamiento

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        // CallableStatement: permite invocar procedimientos almacenados
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos del Tratamiento en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Datos de Tratamiento
        cstmt.setInt(1, t.getIdTratamiento());
        cstmt.setString(2, t.getNombre());
        cstmt.setDouble(3, t.getPrecioCompra());
        cstmt.setDouble(4, t.getPrecioVenta());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "UPDATE tratamiento SET estatus = 0 WHERE idTratamiento = ?";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la operacion:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        pstmt.setInt(1, id);

        //Ejecutamos la operacion:
        pstmt.executeUpdate();

        pstmt.close();
        connMySQL.close();
    }

    public String armarConsultaSQL(String filtro, boolean showDeleted) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_tratamientos";
        String sqlWhere = "";
        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += " WHERE (CAST(idTratamiento AS CHAR) = ? OR "
                    + "nombre LIKE ? OR "
                    + "CAST(precioCompra AS CHAR) = ? OR "
                    + "CAST(precioVenta AS CHAR) = ?)";
            if (!showDeleted) {
                sqlWhere = sqlWhere
                        + " AND estatus = 1";
            } else {
                sqlWhere = sqlWhere
                        + " AND estatus = 0";
            }
        } else {
            if (!showDeleted) {
                sqlWhere = " WHERE estatus = 1";
            } else {
                sqlWhere = " WHERE estatus = 0";
            }
            // sqlWhere = (showDeleted ? "" : " WHERE estatus = 1");
        }
        sql = sql + sqlWhere;

        return sql;
    }

    public List<Tratamiento> getAll(String filtro, boolean showDeleted) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro, showDeleted);
//        if (filtro.equals("") || filtro.equals("1")) {
//            //La consulta SQL a ejecutar:
//            sql = "SELECT * FROM v_tratamientos WHERE estatus = 1";
//        } else if (filtro.equals("0")) {
//            sql = "SELECT * FROM v_tratamientos WHERE estatus = 0";
//        }

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        // Connection: gestiona la conexión con la BD. 
        // Prepara la base de datos para recibir llamadas a 
        // StoreProcedures, consultas, cerrar la conexion, 
        // abrir la conexion
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        // PreparedStatement: permite ejecutar cualquier 
        // instrucción (insert, update, drop, delete, 
        // create, alter, etc.) sql en el SGBD
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        // ResultSet: contiene los resultados 
        // que devuelve la base de datos después de una instrucción sql
        ResultSet rs = null;

        List<Tratamiento> tratamientos = new ArrayList<>();
        
        System.out.println(sql);
        
        if (filtro != null && !filtro.trim().equals("")) {
            pstmt.setString(1, "%"+filtro+"%");
            pstmt.setString(2, "%"+filtro+"%");
            pstmt.setString(3, "%"+filtro+"%");
            pstmt.setString(4, "%"+filtro+"%");
        }
        rs = pstmt.executeQuery();

        while (rs.next()) {
            tratamientos.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return tratamientos;
    }

    public Tratamiento fill(ResultSet rs) throws Exception {
        Tratamiento t = new Tratamiento();

        // Asignar datos a Tratamiento
        t.setIdTratamiento(rs.getInt("idTratamiento"));
        t.setNombre(rs.getString("nombre"));
        t.setPrecioCompra(rs.getDouble("precioCompra"));
        t.setPrecioVenta(rs.getDouble("precioVenta"));
        t.setEstatus(rs.getInt("estatus"));

        return t;
    }
}
