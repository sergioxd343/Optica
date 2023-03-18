/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Cliente;
import com.prossofteam.oq.model.Empleado;
import com.prossofteam.oq.model.ExamenVista;
import com.prossofteam.oq.model.Graduacion;
import com.prossofteam.oq.model.Persona;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class ControllerExamenVista {
    public String armarConsultaSQL(String filtro) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_examenvista_cliente ";
        String sqlWhere = "";
        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += "WHERE CAST(idCliente AS CHAR) = ?";
        }
        sql = sql + sqlWhere;

        return sql;
    }

    public List<ExamenVista> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro);

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

        List<ExamenVista> examenesVista = new ArrayList<>();

        System.out.println(sql);

        if (filtro != null && !filtro.trim().equals("")) {
            pstmt.setString(1, filtro);
        }

        rs = pstmt.executeQuery();
        while (rs.next()) {
            examenesVista.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        cmsql.close();

        return examenesVista;
    }

    public ExamenVista fill(ResultSet rs) throws Exception {
        ExamenVista ev = new ExamenVista();
        Empleado e = new Empleado();
        Cliente c = new Cliente();
        Graduacion g = new Graduacion();
        Persona p = new Persona();

        // Empleado
        e.setIdEmpleado(rs.getInt("idEmpleado"));
        e.setNumeroUnico(rs.getString("numeroUnicoEmpleado"));
        p.setNombre(rs.getString("nombreEmpleado"));
        p.setApellidoPaterno(rs.getString("apellidoPaternoEmpleado"));
        p.setApellidoMaterno(rs.getString("apellidoMaternoEmpleado"));
        e.setPersona(p);
        p = new Persona();
        // Lente de contacto
        c.setIdCliente(rs.getInt("idCliente"));
        c.setNumeroUnico(rs.getString("numeroUnicoCliente"));
        p.setNombre(rs.getString("nombreCliente"));
        p.setApellidoPaterno(rs.getString("apellidoPaternoCliente"));
        p.setApellidoMaterno(rs.getString("apellidoMaternoCliente"));
        c.setPersona(p);

        g.setEsferaod(rs.getDouble("esferaod"));
        g.setEsferaoi(rs.getDouble("esferaoi"));
        g.setCilindrood(rs.getInt("cilindrood"));
        g.setCilindrooi(rs.getInt("cilindrooi"));
        g.setEjeod(rs.getInt("ejeod"));
        g.setEjeoi(rs.getInt("ejeoi"));
        g.setDip(rs.getString("dip"));
        
        ev.setIdExamenVista(rs.getInt("idExamenVista"));
        ev.setClave(rs.getString("clave"));
        ev.setFecha(rs.getString("fecha"));
        ev.setGraduacion(g);
        ev.setCliente(c);
        ev.setEmpleado(e);
        
        return ev;
    }
    
    public int insert(ExamenVista ev) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call insertarExamenVista(?, ?, ?, ?, ?, ?, ?," // Valores de ingreso
                + "?, ?," //Id empleado y id cliente
                + "?, ?, ?)}";  // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        // Banderas que nos permiten saber si se ha registrado los datos. Si no cambia su valor hay un problema con la generación de los ID
        int idExamenVistaGenerado = -1;
        int idGraduacionGenerado = -1;
        String claveGenerada = "";
        String fechaGenerada = "";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Registramos los datos de entrada
        cstmt.setDouble(1, ev.getGraduacion().getEsferaod());
        cstmt.setDouble(2, ev.getGraduacion().getEsferaoi());
        cstmt.setInt(3, ev.getGraduacion().getCilindrood());
        cstmt.setInt(4, ev.getGraduacion().getCilindrooi());
        cstmt.setInt(5, ev.getGraduacion().getEjeod());
        cstmt.setInt(6, ev.getGraduacion().getEjeoi());
        cstmt.setString(7, ev.getGraduacion().getDip());
        cstmt.setInt(8, ev.getEmpleado().getIdEmpleado());
        cstmt.setInt(9, ev.getCliente().getIdCliente());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(10, Types.INTEGER); //idExamenVista
        cstmt.registerOutParameter(11, Types.INTEGER); //idGraduacion
        cstmt.registerOutParameter(12, Types.VARCHAR); //clave
        cstmt.registerOutParameter(13, Types.VARCHAR); //fecha

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idExamenVistaGenerado = cstmt.getInt(10);      // idProducto
        idGraduacionGenerado = cstmt.getInt(11);     // idSolucion        
        claveGenerada = cstmt.getString(12); // codigoBarrasGenerado
        fechaGenerada = cstmt.getString(13); // codigoBarrasGenerado

        // Asignamos los id´s generados al objeto ExamenVista
        ev.setIdExamenVista(idExamenVistaGenerado);
        ev.getGraduacion().setIdGraduacion(idGraduacionGenerado);
        ev.setClave(claveGenerada);
        ev.setFecha(fechaGenerada);
        

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idExamenVistaGenerado;
    }
}
