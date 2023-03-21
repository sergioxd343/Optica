/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Empleado;
import com.prossofteam.oq.model.Persona;
import com.prossofteam.oq.model.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ControllerLogin {

    public boolean isAdmin(String rol) {
        if (rol == null || rol.trim().toLowerCase().equals("empleado")) {
            return false;
        } else {
            return rol.trim().toLowerCase().equals("administrador");
        }
    }

    // GET
    public Empleado validarSesion(String usuario, String contrasenia) throws SQLException {
        // Definimos la consulta SQL que busca a un determinado Empleado por sus credenciales.
        String sql = "SELECT * FROM v_empleados "
                + "WHERE nombreUsuario = " + usuario + " AND contrasenia = " + contrasenia;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la operacion:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        // ResultSet: contiene los resultados 
        // que devuelve la base de datos después de una instrucción sql
        ResultSet rs = pstmt.executeQuery();

        // Definimos objeto de Empleado 
        Empleado e = null;
        Persona p = null;
        while (rs.next()) {
            e = new Empleado();
            p = new Persona();

            // Datos personales
            p.setIdPersona(rs.getInt("idPersona"));
            p.setNombre(rs.getString("nombre"));
            p.setApellidoMaterno(rs.getString("apellidoMaterno"));
            p.setApellidoPaterno(rs.getString("apellidoPaterno"));
            p.setGenero(rs.getString("genero"));
            p.setFechaNacimiento(rs.getString("fechaNacimiento"));
            p.setCalle(rs.getString("calle"));
            p.setNumero(rs.getString("numero"));
            p.setColonia(rs.getString("colonia"));
            p.setCp(rs.getString("cp"));
            p.setCiudad(rs.getString("ciudad"));
            p.setEstado(rs.getString("estado"));
            p.setTelCasa(rs.getString("telcasa"));
            p.setTelMovil(rs.getString("telmovil"));
            p.setEmail(rs.getString("email"));
            p.setRfc(rs.getString("rfc"));

            // Datos de empleado
            e.setIdEmpleado(rs.getInt("idEmpleado"));
            e.setNumeroUnico(rs.getString("numeroUnico"));
            e.setEstatus(rs.getInt("estatus"));

            // Datos de usuario
            e.setUsuario(new Usuario());
            e.getUsuario().setIdUsuario(rs.getInt("idUsuario"));
            e.getUsuario().setNombre(rs.getString("nombreUsuario"));
            e.getUsuario().setContrasenia(rs.getString("contrasenia"));
            e.getUsuario().setRol(rs.getString("rol"));

            // Asignamos la Persona a Empleado
            e.setPersona(p);
        }
        return e;
    }

    // POST
    public Empleado login(String usuario, String contrasenia) throws Exception {
        Empleado e = null;
        Persona p = null;
        String sql = "SELECT * FROM v_empleados WHERE nombreUsuario = ? AND contrasenia = ?";

        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la operacion:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, usuario);
        pstmt.setString(2, contrasenia);

        //Ejecutamos la operacion:
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            // Datos personales
            p = new Persona();
            e = new Empleado();
            p.setIdPersona(rs.getInt("idPersona"));
            p.setNombre(rs.getString("nombre"));
            p.setApellidoMaterno(rs.getString("apellidoMaterno"));
            p.setApellidoPaterno(rs.getString("apellidoPaterno"));
            p.setGenero(rs.getString("genero"));
            p.setFechaNacimiento(rs.getString("fechaNacimiento"));
            p.setCalle(rs.getString("calle"));
            p.setNumero(rs.getString("numero"));
            p.setColonia(rs.getString("colonia"));
            p.setCp(rs.getString("cp"));
            p.setCiudad(rs.getString("ciudad"));
            p.setEstado(rs.getString("estado"));
            p.setTelCasa(rs.getString("telcasa"));
            p.setTelMovil(rs.getString("telmovil"));
            p.setEmail(rs.getString("email"));
            p.setRfc(rs.getString("rfc"));

            // Datos de empleado
            e.setIdEmpleado(rs.getInt("idEmpleado"));
            e.setNumeroUnico(rs.getString("numeroUnico"));
            e.setEstatus(rs.getInt("estatus"));

            // Datos de usuario
            e.setUsuario(new Usuario());
            e.getUsuario().setIdUsuario(rs.getInt("idUsuario"));
            e.getUsuario().setNombre(rs.getString("nombreUsuario"));
            e.getUsuario().setContrasenia(rs.getString("contrasenia"));
            e.getUsuario().setRol(rs.getString("rol"));
            e.getUsuario().setLastToken(rs.getString("lastToken"));
            e.getUsuario().setDateLastToken(rs.getString("dateLastToken"));

            // Asignamos la Persona a Empleado
            e.setPersona(p);
        }

        pstmt.close();
        connMySQL.close();

        return e;
    }

    public void guardarToken(Empleado emp) throws Exception {
        String lastToken = "";
        String dateLastToken = "";
        String query = "{CALL generarNuevoTokenEmpleado(?, ? ,?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion con la base de datos
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(query);

        cstmt.setInt(1, emp.getUsuario().getIdUsuario());

        cstmt.executeUpdate();

        lastToken = cstmt.getString(2);      // idPersona
        dateLastToken = cstmt.getString(3);      // idUsuario

        emp.getUsuario().setLastToken(lastToken);
        emp.getUsuario().setDateLastToken(dateLastToken);
        conn.close();
        connMySQL.close();
    }

    public boolean eliminarToken(Empleado empleado) throws Exception {
        boolean eliminado;
        String query = "UPDATE usuario SET lastToken = '' WHERE nombre = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion con la base de datos
        Connection conn = connMySQL.open();

        PreparedStatement cstmt = conn.prepareStatement(query);
        cstmt.setString(1, empleado.getUsuario().getNombre());

        int update = cstmt.executeUpdate();
        eliminado = update == 1;
        conn.close();
        connMySQL.close();
        return eliminado;
    }

    public boolean validarToken(String token) throws Exception {
        boolean tokenCorrecto;
        String sql = "SELECT * FROM usuario WHERE lastToken = ?";

        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la operacion:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, token);
        //Ejecutamos la operacion:
        ResultSet rs = pstmt.executeQuery();

        tokenCorrecto = rs.next();
        pstmt.close();
        connMySQL.close();

        return tokenCorrecto;
    }
}
