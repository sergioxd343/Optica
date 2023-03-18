/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Material;
import com.prossofteam.oq.model.TipoMica;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class ControllerTipoMica {

    public int insert(TipoMica tm) throws Exception {

        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL insertarTipoMica(?, ?, ?, ?)}";  // Valores de Tipo de mica

        int idTipoMicaGenerado = -1;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos de Tipo de mica en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Registramos datos de Tipo de mica
        cstmt.setString(1, tm.getNombre());
        cstmt.setDouble(2, tm.getPrecioCompra());
        cstmt.setDouble(3, tm.getPrecioVenta());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(4, Types.INTEGER);

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idTipoMicaGenerado = cstmt.getInt(4);

        // Asignamos los id´s generados al objeto Material
        tm.setIdTipoMica(idTipoMicaGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idTipoMicaGenerado;
    }

    public void update(TipoMica tm) throws Exception {

        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL actualizarTipoMica(?, ?, ?, ?)}"; // Datos de Material

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        // CallableStatement: permite invocar procedimientos almacenados
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos del Tipo de mica en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Datos de Tipo de mica
        cstmt.setInt(1, tm.getIdTipoMica());
        cstmt.setString(2, tm.getNombre());
        cstmt.setDouble(3, tm.getPrecioCompra());
        cstmt.setDouble(4, tm.getPrecioVenta());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public String armarConsultaSQL(String filtro) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM tipo_mica";
        

        return sql;
    }

    public List<TipoMica> getAll(String filtro) throws Exception {
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

        List<TipoMica> tiposMicas = new ArrayList<>();
        
        System.out.println(sql);
        
        
        
        rs = pstmt.executeQuery(sql);

        while (rs.next()) {
            tiposMicas.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return tiposMicas;
    }

    public TipoMica fill(ResultSet rs) throws Exception {
        TipoMica tm = new TipoMica();

        // Asignar datos a TipoMica
        tm.setIdTipoMica(rs.getInt("idTipoMica"));
        tm.setNombre(rs.getString("nombre"));
        tm.setPrecioCompra(rs.getDouble("precioCompra"));
        tm.setPrecioVenta(rs.getDouble("precioVenta"));

        return tm;
    }
}
