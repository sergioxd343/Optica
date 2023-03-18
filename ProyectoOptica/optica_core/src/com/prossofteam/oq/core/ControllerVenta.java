/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.DetalleVP;
import com.prossofteam.oq.model.Producto;
import com.prossofteam.oq.model.VentaProducto;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ControllerVenta {

    public String armarConsultaSQL(String filtro) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM producto ";
        String sqlWhere = "";
        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += "WHERE nombre LIKE ?"
                    + " AND estatus = 1"
                    + " AND codigoBarras NOT LIKE 'OQ-P%-AR%'"
                    + " OR codigoBarras LIKE ?"
                    + " AND estatus = 1"
                    + " AND codigoBarras NOT LIKE 'OQ-P%-AR%'";
            // sqlWhere = (showDeleted ? "" : " WHERE estatus = 1");
        }
        else{
            sqlWhere += "WHERE codigoBarras NOT LIKE 'OQ-P%-AR%'";
        }
        sql = sql + sqlWhere;

        return sql;
    }

    public List<Producto> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro);

        /*
        if (filtro.equals("") || filtro.equals("1")) {
            //La consulta SQL a ejecutar:
            sql = "SELECT * FROM v_empleados WHERE estatus = 1";
        } else if (filtro.equals("0")) {
            sql = "SELECT * FROM v_empleados WHERE estatus = 0";
        }
         */
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

        List<Producto> productos = new ArrayList<>();

        System.out.println(sql);

        if (filtro != null && !filtro.trim().equals("")) {
            pstmt.setString(1, "%" + filtro + "%");
            pstmt.setString(2, "%" + filtro + "%");
        }
        rs = pstmt.executeQuery();

        while (rs.next()) {
            productos.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return productos;
    }

    public Producto fill(ResultSet rs) throws Exception {
        Producto p = new Producto();

        // Datos personales
        p.setIdProducto(rs.getInt("idProducto"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setCodigoBarras(rs.getString("codigoBarras"));
        p.setExistencias(rs.getInt("existencias"));
        p.setMarca(rs.getString("marca"));
        p.setEstatus(rs.getInt("estatus"));

        return p;
    }

    public boolean transaccionarVenta(DetalleVP dvp) throws Exception {
        boolean hecho = false;
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        ResultSet rs = null;
        Statement stmt = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            String query = "INSERT INTO venta(idEmpleado, clave) values("+dvp.getVenta().getEmpleado().getIdEmpleado()+","+
                    "CONCAT('OQ-VP-E',"+ dvp.getVenta().getEmpleado().getIdEmpleado()+", '-', CAST(UNIX_TIMESTAMP() AS CHAR)))";

            stmt.execute(query);
            String query2 = "SELECT LAST_INSERT_ID()";
            rs = stmt.executeQuery(query2);

            if (rs.next()) {
                dvp.getVenta().setIdVenta(rs.getInt(1));
            }

            for (VentaProducto vp : dvp.getListaProductos()) {
                String query3 = "INSERT INTO venta_producto(idVenta, idProducto, precioUnitario, cantidad, descuento) VALUES ("
                        + dvp.getVenta().getIdVenta() + ","
                        + vp.getProducto().getIdProducto() + ","
                        + vp.getPrecioUnitario() + ","
                        + vp.getCantidad() + ","
                        + vp.getDescuento() + ")";
                stmt.execute(query3);
            }
            conn.commit();
            conn.setAutoCommit(true);

            rs.close();
            stmt.close();
            conn.close();
            connMySQL.close();
            hecho = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();

                rs.close();
                stmt.close();
                conn.close();
                connMySQL.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return hecho;
    }

}
