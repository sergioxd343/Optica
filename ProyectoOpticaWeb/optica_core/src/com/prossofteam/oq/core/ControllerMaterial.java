/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Material;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class ControllerMaterial {

    public int insert(Material m) throws Exception {

        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL insertarMaterial(?, ?, ?, ?)}";  // Valores de Material

        int idMaterialGenerado = -1;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos de Material en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Registramos datos de Material
        cstmt.setString(1, m.getNombre());
        cstmt.setDouble(2, m.getPrecioCompra());
        cstmt.setDouble(3, m.getPrecioVenta());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(4, Types.INTEGER);

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idMaterialGenerado = cstmt.getInt(4);

        // Asignamos los id´s generados al objeto Material
        m.setIdMaterial(idMaterialGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idMaterialGenerado;
    }

    public void update(Material m) throws Exception {

        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL actualizarMaterial(?, ?, ?, ?)}"; // Datos de Material

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        // CallableStatement: permite invocar procedimientos almacenados
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos del Material en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Datos de Material
        cstmt.setInt(1, m.getIdMaterial());
        cstmt.setString(2, m.getNombre());
        cstmt.setDouble(3, m.getPrecioCompra());
        cstmt.setDouble(4, m.getPrecioVenta());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public String armarConsultaSQL(String filtro) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_materiales";
        String sqlWhere = "";

        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += " WHERE (CAST(idMaterial AS CHAR) = ? OR "
                    + "nombre LIKE ? OR "
                    + "CAST(precioCompra AS CHAR) = ? OR "
                    + "CAST(precioVenta AS CHAR) = ?)";
//            if (!showDeleted) {
//                sqlWhere = sqlWhere
//                        + " AND estatus = 1";
//            } else {
//                sqlWhere = sqlWhere
//                        + " AND estatus = 0";
//            }
        } else {
//            if (!showDeleted) {
//                sqlWhere = " WHERE estatus = 1";
//            } else {
//                sqlWhere = " WHERE estatus = 0";
//            }
            // sqlWhere = (showDeleted ? "" : " WHERE estatus = 1");
        }
        sql = sql + sqlWhere;

        return sql;
    }

    public List<Material> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro);

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

        List<Material> materiales = new ArrayList<>();

        System.out.println(sql);

        if (filtro != null && !filtro.trim().equals("")) {
            pstmt.setString(1, "%" + filtro + "%");
            pstmt.setString(2, "%" + filtro + "%");
            pstmt.setString(3, "%" + filtro + "%");
            pstmt.setString(4, "%" + filtro + "%");
        }

        rs = pstmt.executeQuery();

        while (rs.next()) {
            materiales.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return materiales;
    }

    public Material fill(ResultSet rs) throws Exception {
        Material m = new Material();

        // Asignar datos a Material
        m.setIdMaterial(rs.getInt("idMaterial"));
        m.setNombre(rs.getString("nombre"));
        m.setPrecioCompra(rs.getDouble("precioCompra"));
        m.setPrecioVenta(rs.getDouble("precioVenta"));

        return m;
    }
}
