package com.prossofteam.oq.core; // Líneas = 206

import com.prossofteam.oq.bd.ConexionMySQL;
import com.prossofteam.oq.model.Empleado;
import com.prossofteam.oq.model.Persona;
import com.prossofteam.oq.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;


public class ControllerEmpleado {

    public static boolean isAdmin(Empleado empleado) {
        if (empleado == null || empleado.getUsuario() == null || empleado.getUsuario().getNombre() == null) {
            return false;
        } else {
            return empleado.getUsuario().getRol().trim().toLowerCase().equals("administrador");
        }
    }

    public int insert(Empleado empleado) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{CALL insertarEmpleado(?, ?, ?, ?, ?, ?, ?, " // Datos Personales
                + "?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, " // Datos de Seguridad (Usuario)
                + "?, ?, ?, ?, ?)}";  // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        // Banderas que nos permiten saber si se ha registrado los datos. Si no cambia su valor hay un problema con la generación de los ID
        int idPersonaGenerado = -1;
        int idEmpleadoGenerado = -1;
        int idUsuarioGenerado = -1;
        String numeroUnicoGenerado = "";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Registramos datos personales
        cstmt.setString(1, empleado.getPersona().getNombre());
        cstmt.setString(2, empleado.getPersona().getApellidoPaterno());
        cstmt.setString(3, empleado.getPersona().getApellidoMaterno());
        cstmt.setString(4, empleado.getPersona().getGenero());
        cstmt.setString(5, empleado.getPersona().getFechaNacimiento());
        cstmt.setString(6, empleado.getPersona().getCalle());
        cstmt.setString(7, empleado.getPersona().getNumero());
        cstmt.setString(8, empleado.getPersona().getColonia());
        cstmt.setString(9, empleado.getPersona().getCp());
        cstmt.setString(10, empleado.getPersona().getCiudad());
        cstmt.setString(11, empleado.getPersona().getEstado());
        cstmt.setString(12, empleado.getPersona().getTelCasa());
        cstmt.setString(13, empleado.getPersona().getTelMovil());
        cstmt.setString(14, empleado.getPersona().getEmail());
        cstmt.setString(15, empleado.getPersona().getRfc());

        // Registramos parámetros de datos de seguridad:
        cstmt.setString(16, empleado.getUsuario().getNombre());
        cstmt.setString(17, empleado.getUsuario().getContrasenia());
        cstmt.setString(18, empleado.getUsuario().getRol());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(19, Types.INTEGER); //idPersona
        cstmt.registerOutParameter(20, Types.INTEGER); //idUsuario
        cstmt.registerOutParameter(21, Types.INTEGER); //idEmpleado
        cstmt.registerOutParameter(22, Types.VARCHAR); //numeroUnicoGenerado
        cstmt.registerOutParameter(23, Types.VARCHAR); // Token para implementar la seguridad

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idPersonaGenerado = cstmt.getInt(19);      // idPersona
        idUsuarioGenerado = cstmt.getInt(20);      // idUsuario
        idEmpleadoGenerado = cstmt.getInt(21);     // idEmpleado        
        numeroUnicoGenerado = cstmt.getString(22); // numeroUnicoGenerado

        // Asignamos los id´s generados al objeto Empleado
        empleado.setIdEmpleado(idEmpleadoGenerado);
        empleado.getPersona().setIdPersona(idPersonaGenerado);
        empleado.getUsuario().setIdUsuario(idUsuarioGenerado);
        empleado.setNumeroUnico(numeroUnicoGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idEmpleadoGenerado;
    }

    public void update(Empleado empleado) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarEmpleado(  ?, ?, ?, ?, ?, ?, ?, " //Datos Personales
                + "?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, " // Datos de Seguridad
                + "?, ?, ?)}"; // IDs

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        // CallableStatement: permite invocar procedimientos almacenados
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        // Datos personales
        cstmt.setString(1, empleado.getPersona().getNombre());
        cstmt.setString(2, empleado.getPersona().getApellidoPaterno());
        cstmt.setString(3, empleado.getPersona().getApellidoMaterno());
        cstmt.setString(4, empleado.getPersona().getGenero());
        cstmt.setString(5, empleado.getPersona().getFechaNacimiento());
        cstmt.setString(6, empleado.getPersona().getCalle());
        cstmt.setString(7, empleado.getPersona().getNumero());
        cstmt.setString(8, empleado.getPersona().getColonia());
        cstmt.setString(9, empleado.getPersona().getCp());
        cstmt.setString(10, empleado.getPersona().getCiudad());
        cstmt.setString(11, empleado.getPersona().getEstado());
        cstmt.setString(12, empleado.getPersona().getTelCasa());
        cstmt.setString(13, empleado.getPersona().getTelMovil());
        cstmt.setString(14, empleado.getPersona().getEmail());
        cstmt.setString(15, empleado.getPersona().getRfc());

        // Datos de usuario
        cstmt.setString(16, empleado.getUsuario().getNombre());
        cstmt.setString(17, empleado.getUsuario().getContrasenia());
        cstmt.setString(18, empleado.getUsuario().getRol());

        // ID's
        cstmt.setInt(19, empleado.getPersona().getIdPersona());
        cstmt.setInt(20, empleado.getUsuario().getIdUsuario());
        cstmt.setInt(21, empleado.getIdEmpleado());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int idEmpleado) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "UPDATE empleado SET estatus = 0 WHERE idEmpleado = ?";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la operacion:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        pstmt.setInt(1, idEmpleado);

        //Ejecutamos la operacion:
        pstmt.executeUpdate();

        pstmt.close();
        connMySQL.close();
    }

    public String armarConsultaSQL(String filtro, boolean showDeleted) {
        // String = utlizar LIKE
        String sql = "SELECT * FROM v_empleados ";
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
                    + "CAST(idEmpleado AS CHAR) = ? OR "
                    + "numeroUnico LIKE ? OR "
                    + "CAST(idUsuario AS CHAR) = ? OR "
                    + "nombreUsuario LIKE ? OR "
                    + "contrasenia LIKE ? OR "
                    + "rol LIKE ?)";
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

    public List<Empleado> getAll(String filtro, boolean showDeleted) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = armarConsultaSQL(filtro, showDeleted);

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

        List<Empleado> empleados = new ArrayList<>();

        System.out.println(sql);

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
            pstmt.setString(19, "%" + filtro + "%");
            pstmt.setString(20, "%" + filtro + "%");
            pstmt.setString(21, "%" + filtro + "%");
            pstmt.setString(22, "%" + filtro + "%");
        }
        rs = pstmt.executeQuery();

        while (rs.next()) {
            empleados.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return empleados;
    }

    public Empleado fill(ResultSet rs) throws Exception {
        Empleado e = new Empleado();
        Persona p = new Persona();

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
        e.getUsuario().setLastToken(rs.getString("lastToken"));
        e.getUsuario().setDateLastToken(rs.getString("dateLastToken"));

        // Asignamos la Persona a Empleado
        e.setPersona(p);

        return e;
    }
}
