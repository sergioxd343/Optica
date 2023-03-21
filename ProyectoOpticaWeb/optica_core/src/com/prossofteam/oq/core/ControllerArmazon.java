/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.model.Armazon;
import com.prossofteam.oq.model.Producto;
import com.prossofteam.oq.bd.ConexionMySQL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControllerArmazon {

    public int insert(Armazon armazon) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL insertarArmazon(?, ?, ?, ?, ?, "
                + // Datos Producto
                "?, ?, ?, ?, ?, "
                + // Datos de Armazon
                "?, ?, ?)}";       // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        int idProductoGenerado = -1;
        int idArmazonGenerado = -1;
        String codigoBarras = "";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros del producto en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, armazon.getProducto().getNombre());
        cstmt.setString(2, armazon.getProducto().getMarca());
        cstmt.setDouble(3, armazon.getProducto().getPrecioCompra());
        cstmt.setDouble(4, armazon.getProducto().getPrecioVenta());
        cstmt.setInt(5, armazon.getProducto().getExistencias());

        // Registramos parámetros del Armazon:
        cstmt.setString(6, armazon.getModelo());
        cstmt.setString(7, armazon.getColor());
        cstmt.setString(8, armazon.getDimensiones());
        cstmt.setString(9, armazon.getDescripcion());
        cstmt.setString(10, armazon.getFotografia());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(11, Types.INTEGER);
        cstmt.registerOutParameter(12, Types.INTEGER);
        cstmt.registerOutParameter(13, Types.VARCHAR);

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idProductoGenerado = cstmt.getInt(11);
        idArmazonGenerado = cstmt.getInt(12);
        codigoBarras = cstmt.getString(13);

        armazon.getProducto().setIdProducto(idProductoGenerado);
        armazon.setIdArmazon(idArmazonGenerado);
        armazon.getProducto().setCodigoBarras(codigoBarras);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idArmazonGenerado;
    }

    public void update(Armazon armazon) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL actualizarArmazon(?, ?, ?, ?, ?, "
                + //Datos Producto
                "?, ?, ?, ?, ?, "
                + // Datos Armazon
                "?, ?)}";           // IDs

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, armazon.getProducto().getNombre());
        cstmt.setString(2, armazon.getProducto().getMarca());
        cstmt.setDouble(3, armazon.getProducto().getPrecioCompra());
        cstmt.setDouble(4, armazon.getProducto().getPrecioVenta());
        cstmt.setInt(5, armazon.getProducto().getExistencias());

        cstmt.setString(6, armazon.getModelo());
        cstmt.setString(7, armazon.getColor());
        cstmt.setString(8, armazon.getDimensiones());
        cstmt.setString(9, armazon.getDescripcion());
        cstmt.setString(10, armazon.getFotografia());

        cstmt.setInt(11, armazon.getProducto().getIdProducto());
        cstmt.setInt(12, armazon.getIdArmazon());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int idProducto) throws Exception {
        String sql = "UPDATE producto SET estatus = 0 WHERE idProducto = ?";           // IDs

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, idProducto);
        pstmt.executeUpdate();
        pstmt.close();

        connMySQL.close();
    }

    public String armarConsultaSQL(String filtro, boolean showDeleted) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_armazones ";
        String sqlWhere = "";
        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += "WHERE (CAST(idArmazon AS CHAR) = ? OR "
                    + "modelo lIKE ? OR "
                    + "color LIKE ? OR "
                    + "dimensiones LIKE ? OR "
                    + "descripcion LIKE ? OR "
                    + "fotografia LIKE ? OR "
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

    public List<Armazon> getAll(String filtro, boolean showDeleted) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro, showDeleted);
        
        /*
        if (filtro.equals("") || filtro.equals("1")) {
            sql = "SELECT * FROM v_armazones WHERE estatus = 1";
        } else if (filtro.equals("0")) {
            sql = "SELECT * FROM v_armazones WHERE estatus = 0";
        } 
        */

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = null;

        List<Armazon> armazones = new ArrayList<>();
        
        if (filtro != null && !filtro.trim().equals("")) {
            pstmt.setString(1, "%"+filtro+"%");
            pstmt.setString(2, "%"+filtro+"%");
            pstmt.setString(3, "%"+filtro+"%");
            pstmt.setString(4, "%"+filtro+"%");
            pstmt.setString(5, "%"+filtro+"%");
            pstmt.setString(6, "%"+filtro+"%");
            pstmt.setString(7, "%"+filtro+"%");
            pstmt.setString(8, "%"+filtro+"%");
            pstmt.setString(9, "%"+filtro+"%");
            pstmt.setString(10, "%"+filtro+"%");
            pstmt.setString(11, "%"+filtro+"%");
            pstmt.setString(12, "%"+filtro+"%");
            pstmt.setString(13, "%"+filtro+"%");
        }
        
        rs = pstmt.executeQuery();

        while (rs.next()) {
            armazones.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return armazones;
    }

    private Armazon fill(ResultSet rs) throws Exception {
        Armazon armazon = new Armazon();
        Producto producto = new Producto();

        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setCodigoBarras(rs.getString("codigoBarras"));
        producto.setNombre(rs.getString("nombre"));
        producto.setMarca(rs.getString("marca"));
        producto.setPrecioCompra(rs.getDouble("precioCompra"));
        producto.setPrecioVenta(rs.getDouble("precioVenta"));
        producto.setExistencias(rs.getInt("existencias"));
        producto.setEstatus(rs.getInt("estatus"));

        armazon.setIdArmazon(rs.getInt("idArmazon"));
        armazon.setModelo(rs.getString("modelo"));
        armazon.setColor(rs.getString("color"));
        armazon.setDimensiones(rs.getString("dimensiones"));
        armazon.setDescripcion(rs.getString("descripcion"));
        armazon.setFotografia(rs.getString("fotografia"));

        armazon.setProducto(producto);
        return armazon;
    }
}
