package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.LenteContacto;
import com.prossofteam.oq.model.Producto;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ControllerLenteContacto {

    public int insert(LenteContacto lc) throws Exception {
        String sql = "{CALL insertarLenteContacto(?, ?, ?, ?, ?, "
                + //Datos del Producto
                "?, ?, "
                + //Datos del Lente de contacto
                "?, ?, ?)}"; // Valores de Retorno

        int idLenteContactoGenerado = -1;
        int idProductoGenerado = -1;
        String codigoBarrasGenerado = "";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        // Producto
        cstmt.setString(1, lc.getProducto().getNombre());
        cstmt.setString(2, lc.getProducto().getMarca());
        cstmt.setDouble(3, lc.getProducto().getPrecioCompra());
        cstmt.setDouble(4, lc.getProducto().getPrecioVenta());
        cstmt.setInt(5, lc.getProducto().getExistencias());
        // Lente de contacto
        cstmt.setInt(6, lc.getKeratometria());
        cstmt.setString(7, lc.getFotografia());
        // Valores de salida
        cstmt.registerOutParameter(8, Types.INTEGER);
        cstmt.registerOutParameter(9, Types.INTEGER);
        cstmt.registerOutParameter(10, Types.VARCHAR);

        cstmt.executeUpdate();

        idProductoGenerado = cstmt.getInt(8);
        idLenteContactoGenerado = cstmt.getInt(9);
        codigoBarrasGenerado = cstmt.getString(10);

        lc.getProducto().setIdProducto(idProductoGenerado);
        lc.setIdLenteContacto(idLenteContactoGenerado);
        lc.getProducto().setCodigoBarras(codigoBarrasGenerado);

        cstmt.close();
        connMySQL.close();

        return idLenteContactoGenerado;
    }

    public void update(LenteContacto lc) throws Exception {
        String sql = "{CALL actualizarLenteContacto(?, ?, ?, ?, ?, "
                + //Datos del producto
                "?, ?, "
                + //Datos del lente de contacto
                "?, ?)}"; // IDs de producto y lente de contacto

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        // Producto
        cstmt.setString(1, lc.getProducto().getNombre());
        cstmt.setString(2, lc.getProducto().getMarca());
        cstmt.setDouble(3, lc.getProducto().getPrecioCompra());
        cstmt.setDouble(4, lc.getProducto().getPrecioVenta());
        cstmt.setInt(5, lc.getProducto().getExistencias());
        // Lente de contacto
        cstmt.setInt(6, lc.getKeratometria());
        cstmt.setString(7, lc.getFotografia());

        cstmt.setInt(8, lc.getProducto().getIdProducto());
        cstmt.setInt(9, lc.getIdLenteContacto());

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {
        String sql = "UPDATE producto SET estatus = 0 WHERE idProducto = ?";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        pstmt.close();

        connMySQL.close();
    }

    public String armarConsultaSQL(String filtro, boolean showDeleted) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_lentes_contacto ";
        String sqlWhere = "";
        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += "WHERE (CAST(idLenteContacto AS CHAR) = ? OR "
                    + "CAST(keratometria AS CHAR) = ? OR "
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

    public List<LenteContacto> getAll(String filtro, boolean showDeleted) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro, showDeleted);

        /*
        if (filtro.equals("") || filtro.equals("1")) {
            sql = "SELECT * FROM v_lentes_contacto WHERE estatus = 1";
        } else {
            sql = "SELECT * FROM v_lentes_contacto WHERE estatus = 0";
        }
         */
        ConexionMySQL cmsql = new ConexionMySQL();

        Connection conn = cmsql.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = null;

        List<LenteContacto> lcs = new ArrayList<>();

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
            pstmt.setString(9, "%"+filtro+"%");
            pstmt.setString(10, "%"+filtro+"%");
        }

        rs = pstmt.executeQuery();
        while (rs.next()) {
            lcs.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        cmsql.close();

        return lcs;
    }

    public LenteContacto fill(ResultSet rs) throws Exception {
        LenteContacto lc = new LenteContacto();
        Producto p = new Producto();

        // Producto
        p.setIdProducto(rs.getInt("idProducto"));
        p.setCodigoBarras(rs.getString("codigoBarras"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setExistencias(rs.getInt("existencias"));
        p.setEstatus(rs.getInt("estatus"));
        // Lente de contacto
        lc.setIdLenteContacto(rs.getInt("idLenteContacto"));
        lc.setKeratometria(rs.getInt("keratometria"));
        lc.setFotografia(rs.getString("fotografia"));

        lc.setProducto(p);

        return lc;
    }
}
