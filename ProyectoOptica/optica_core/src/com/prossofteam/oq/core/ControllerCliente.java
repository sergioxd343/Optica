/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 * Ivan
 */
package com.prossofteam.oq.core;

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Cliente;
import com.prossofteam.oq.model.Persona;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class ControllerCliente {

    public int insert(Cliente cliente) throws SQLException {

        String sql = "{CALL insertarCliente(?, ?, ?, ? ,? ,? ,?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?)}";

        int idPersonaGenerado = -1;
        int idClienteGenerado = -1;
        String numeroUnicoGenerado = "";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();  //Connection - Abre la conexion con la BD

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, cliente.getPersona().getNombre());
        cstmt.setString(2, cliente.getPersona().getApellidoPaterno());
        cstmt.setString(3, cliente.getPersona().getApellidoMaterno());
        cstmt.setString(4, cliente.getPersona().getGenero());
        cstmt.setString(5, cliente.getPersona().getFechaNacimiento());
        cstmt.setString(6, cliente.getPersona().getCalle());
        cstmt.setString(7, cliente.getPersona().getNumero());
        cstmt.setString(8, cliente.getPersona().getColonia());
        cstmt.setString(9, cliente.getPersona().getCp());
        cstmt.setString(10, cliente.getPersona().getCiudad());
        cstmt.setString(11, cliente.getPersona().getEstado());
        cstmt.setString(12, cliente.getPersona().getTelCasa());
        cstmt.setString(13, cliente.getPersona().getTelMovil());
        cstmt.setString(14, cliente.getPersona().getEmail());
        cstmt.setString(15, cliente.getPersona().getRfc());

        cstmt.registerOutParameter(16, Types.INTEGER);
        cstmt.registerOutParameter(17, Types.INTEGER);
        cstmt.registerOutParameter(18, Types.VARCHAR);

        cstmt.executeUpdate();

        idPersonaGenerado = cstmt.getInt(16);
        idClienteGenerado = cstmt.getInt(17);
        numeroUnicoGenerado = cstmt.getString(18);

        cliente.setIdCliente(idClienteGenerado);
        cliente.getPersona().setIdPersona(idPersonaGenerado);
        cliente.setNumeroUnico(numeroUnicoGenerado);

        cstmt.close();
        connMySQL.close();

        return idClienteGenerado;
    }

    public void update(Cliente cliente) throws SQLException {

        String sql = "{call actualizarCliente(?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, cliente.getPersona().getNombre());
        cstmt.setString(2, cliente.getPersona().getApellidoPaterno());
        cstmt.setString(3, cliente.getPersona().getApellidoMaterno());
        cstmt.setString(4, cliente.getPersona().getGenero());
        cstmt.setString(5, cliente.getPersona().getFechaNacimiento());
        cstmt.setString(6, cliente.getPersona().getCalle());
        cstmt.setString(7, cliente.getPersona().getNumero());
        cstmt.setString(8, cliente.getPersona().getColonia());
        cstmt.setString(9, cliente.getPersona().getCp());
        cstmt.setString(10, cliente.getPersona().getCiudad());
        cstmt.setString(11, cliente.getPersona().getEstado());
        cstmt.setString(12, cliente.getPersona().getTelCasa());
        cstmt.setString(13, cliente.getPersona().getTelMovil());
        cstmt.setString(14, cliente.getPersona().getEmail());
        cstmt.setString(15, cliente.getPersona().getRfc());
        cstmt.setInt(16, cliente.getPersona().getIdPersona());

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int idProducto) throws SQLException, Exception {
        //La consulta SQL a ejecutar:
        String sql = "UPDATE cliente SET estatus = 0 WHERE idCliente = ?";

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

    public String armarConsultaSQL(String filtro, boolean showDeleted) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_clientes ";
        String sqlWhere = "";
        // Revisamos que el filtro no sea nulo y no este vacío
        if (filtro != null && !filtro.trim().equals("")) {
            sqlWhere += "WHERE (CAST(idPersona AS CHAR) = ? OR "
                    + "nombre LIKE ? OR "
                    + "apellidoPaterno LIKE ? OR "
                    + "apellidoMaterno LIKE ? OR "
                    + "genero LIKE ? OR "
                    + "fechaNacimiento LIKE ? OR "
                    + "calle LIKE ? OR "
                    + "numero LIKE ? OR "
                    + "colonia LIKE ? OR "
                    + "cp LIKE ? OR "
                    + "ciudad LIKE ? OR "
                    + "estado LIKE ? OR "
                    + "telcasa LIKE ? OR "
                    + "telmovil LIKE ? OR "
                    + "email LIKE ? OR "
                    + "rfc LIKE ? OR "
                    + "CAST(idCliente AS CHAR) = ? OR "
                    + "numeroUnico LIKE ?)";
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

    public List<Cliente> getAll(String filtro, boolean showDeleted) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro, showDeleted);

        /*
        if (filtro.equals("") || filtro.equals("1")) {
            sql = "SELECT * FROM v_clientes WHERE estatus = 1";
        }else if (filtro.equals("0")){
            sql = "SELECT * FROM v_clientes WHERE estatus = 0";
        }
         */
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //PreparedStatement - Permite ejecutar consultas sql en el SGBD
        //insert, update, delete, drop, create, alter

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = null; //ResultSet - Contiene los resultados que devuelve la BD depsues de ejecutar una instruccion de SQL
        
        List<Cliente> clientes = new ArrayList<>();
        
        if (filtro != null && !filtro.trim().equals("")) {
            pstmt.setString(1, "%" + filtro + "%");
            pstmt.setString(2, "%" + filtro + "%");
            pstmt.setString(3, "%" + filtro + "%");
            pstmt.setString(4, "%" + filtro + "%");
            pstmt.setString(5, "%" + filtro + "%");
            pstmt.setString(6, "%" + filtro + "%");
            pstmt.setString(7, "%" + filtro + "%");
            pstmt.setString(8, "%" + filtro + "%");
            pstmt.setString(9, "%" + filtro + "%");
            pstmt.setString(10, "%" + filtro + "%");
            pstmt.setString(11, "%" + filtro + "%");
            pstmt.setString(12, "%" + filtro + "%");
            pstmt.setString(13, "%" + filtro + "%");
            pstmt.setString(14, "%" + filtro + "%");
            pstmt.setString(15, "%" + filtro + "%");
            pstmt.setString(16, "%" + filtro + "%");
            pstmt.setString(17, "%" + filtro + "%");
            pstmt.setString(18, "%" + filtro + "%");
        }
        
        rs = pstmt.executeQuery();
        
        while (rs.next()) {
            clientes.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return clientes;
    }

    public Cliente fill(ResultSet rs) throws Exception {
        Cliente cliente = new Cliente();
        Persona persona = new Persona();

        persona.setApellidoMaterno(rs.getString("apellidoMaterno"));
        persona.setApellidoPaterno(rs.getString("apellidoPaterno"));
        persona.setCalle(rs.getString("calle"));
        persona.setCiudad(rs.getString("ciudad"));
        persona.setColonia(rs.getString("colonia"));
        persona.setCp(rs.getString("cp"));
        persona.setEmail(rs.getString("email"));
        persona.setEstado(rs.getString("estado"));
        persona.setFechaNacimiento(rs.getString("fechaNacimiento"));
        persona.setGenero(rs.getString("genero"));
        persona.setIdPersona(rs.getInt("idPersona"));
        persona.setNombre(rs.getString("nombre"));
        persona.setNumero(rs.getString("numero"));
        persona.setTelCasa(rs.getString("telcasa"));
        persona.setTelMovil(rs.getString("telmovil"));
        persona.setRfc(rs.getString("rfc"));

        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNumeroUnico(rs.getString("numeroUnico"));
        cliente.setEstatus(rs.getInt("estatus"));

        cliente.setPersona(persona);

        return cliente;
    }
}
