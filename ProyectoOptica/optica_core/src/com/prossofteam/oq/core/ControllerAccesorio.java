package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Accesorio;
import com.prossofteam.oq.model.Producto;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;


public class ControllerAccesorio {

    public int insert(Accesorio accesorio) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL insertarAccesorio(?, ?, ?, ?, ?, ?, " // Datos Personales
                + " ?, ?, ?)}";  // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        // Banderas que nos permiten saber si se ha registrado los datos. Si no cambia su valor hay un problema con la generación de los ID
        int idProductoGenerado = -1;
        int idAccesorioGenerado = -1;
        String codigoBarrasOut = null;
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Registramos datos personales
        cstmt.setString(1, accesorio.getProducto().getCodigoBarras());
        cstmt.setString(2, accesorio.getProducto().getNombre());
        cstmt.setString(3, accesorio.getProducto().getMarca());
        cstmt.setDouble(4, accesorio.getProducto().getPrecioCompra());
        cstmt.setDouble(5, accesorio.getProducto().getPrecioVenta());
        cstmt.setInt(6, accesorio.getProducto().getExistencias());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(7, Types.INTEGER); //idProducto
        cstmt.registerOutParameter(8, Types.INTEGER); //idAccesorio
        cstmt.registerOutParameter(9, Types.VARCHAR); //codigoBarras

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idProductoGenerado = cstmt.getInt(7);      // idProducto
        idAccesorioGenerado = cstmt.getInt(8);      // idAccesorio
        codigoBarrasOut = cstmt.getString(9);     // codigoBarras      

        // Asignamos los id´s generados al objeto Accesorio
        accesorio.setIdAccesorio(idAccesorioGenerado);
        accesorio.getProducto().setIdProducto(idProductoGenerado);
        accesorio.getProducto().setCodigoBarras(codigoBarrasOut);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idAccesorioGenerado;
    }

    public void update(Accesorio accesorio) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarAccesorio(  ?, ?, ?, ?, ?, ?, ?)}"; // IDs

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        // CallableStatement: permite invocar procedimientos almacenados
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Datos personales
        cstmt.setString(1, accesorio.getProducto().getCodigoBarras());
        cstmt.setString(2, accesorio.getProducto().getNombre());
        cstmt.setString(3, accesorio.getProducto().getMarca());
        cstmt.setDouble(4, accesorio.getProducto().getPrecioCompra());
        cstmt.setDouble(5, accesorio.getProducto().getPrecioVenta());
        cstmt.setDouble(6, accesorio.getProducto().getExistencias());
        cstmt.setInt(7, accesorio.getProducto().getIdProducto());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int idProducto) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "UPDATE producto SET estatus = 0 WHERE idProducto = ?";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la operacion:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Establecemos el parametro del id del producto
        pstmt.setInt(1, idProducto);

        //Ejecutamos la operacion:
        pstmt.executeUpdate();

        pstmt.close();
        connMySQL.close();

    }

    private String armarConsultaSQL(String filtro, boolean showDeleted) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_accesorios ";
        String sqlWhere = "";
        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += "WHERE (CAST(idAccesorio AS CHAR) = ? OR "
                    + "CAST(idProducto AS CHAR) = ? OR "
                    + "codigoBarras LIKE ? OR "
                    + "nombre LIKE ? OR "
                    + "marca LIKE ? OR "
                    + "CAST(precioCompra AS CHAR) = ? OR "
                    + "CAST(precioVenta AS CHAR) = ? OR "
                    + "CAST(existencias AS CHAR) = ?)";
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

    public List<Accesorio> getAll(String filtro, boolean showDeleted) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro, showDeleted);
        
        /*
        if (filtro.equals("") || filtro.equals("1")) {
            sql = "SELECT * FROM v_accesorios WHERE estatus = 1";
        } else if (filtro.equals("0")) {
            sql = "SELECT * FROM v_accesorios WHERE estatus = 0";
        }
        */

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        // Connection: gestiona la conexión con la BD. Prepara la base de datos para recibir llamadas a StoreProcedures, consultas, cerrar la conexion, abrir la conexion
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        // PreparedStatement: permite ejecutar cualquier instrucción (insert, update, drop, delete, create, alter, etc.) sql en el SGBD
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        // ResultSet: contiene los resultados que devuelve la base de datos después de una instrucción sql
        ResultSet rs = null;

        List<Accesorio> accesorios = new ArrayList<>();
        
        System.out.println(sql);
        
        if (filtro != null && !filtro.trim().equals("")) {
            pstmt.setString(1, "%"+filtro+"%");
            pstmt.setString(2, "%"+filtro+"%");
            pstmt.setString(3, "%"+filtro+"%");
            pstmt.setString(4, "%"+filtro+"%");
            pstmt.setString(5, "%"+filtro+"%");
            pstmt.setString(6, "%"+filtro+"%");
            pstmt.setString(7, "%"+filtro+"%");
            pstmt.setString(8, "%"+filtro+"%");
        }
        System.out.println(sql);
        rs = pstmt.executeQuery();
        System.out.println(pstmt.toString());
        while (rs.next()) {
            accesorios.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return accesorios;
    }

    private Accesorio fill(ResultSet rs) throws Exception {
        Accesorio a = new Accesorio();
        Producto p = new Producto();

        // Datos de accoesorio
        p.setIdProducto(rs.getInt("idProducto"));
        p.setCodigoBarras(rs.getString("codigoBarras"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setExistencias(rs.getInt("existencias"));
        p.setEstatus(rs.getInt("estatus"));

        a.setIdAccesorio(rs.getInt("idAccesorio"));

        // Asignamos el Producto a Accesorio
        a.setProducto(p);

        return a;
    }
}
