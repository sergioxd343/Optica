/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest; // Líneas = 95

import com.prossofteam.oq.core.ControllerEmpleado;
import com.prossofteam.oq.core.ControllerLogin;
import com.prossofteam.oq.model.Empleado;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("empleado")
public class RESTEmpleado {

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro,
            @FormParam("showDeleted") @DefaultValue("false") boolean showDeleted,
            @FormParam("token") @DefaultValue("") String token,
            @FormParam("rol") @DefaultValue("") String rol) {
        String out = null;
        ControllerEmpleado ce = null;
        ControllerLogin cl = null;
        List<Empleado> empleados = null;

        try {
            cl = new ControllerLogin();
            if (cl.validarToken(token)) {
                ce = new ControllerEmpleado();
                empleados = ce.getAll(filtro, showDeleted);
                out = new Gson().toJson(empleados);
            } else {
                out = "{\"errorsec\":\"El token es incorrecto\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosEmpleado") @DefaultValue("") String datosEmpleado,
            @FormParam("token") @DefaultValue("") String token,
            @FormParam("rol") @DefaultValue("") String rol) throws Exception {

        String out = null;
        Gson gson = new Gson();
        Empleado emp = null;
        ControllerEmpleado ce = new ControllerEmpleado();
        ControllerLogin cl = new ControllerLogin();

        try {
            if (cl.validarToken(token)) {

                if (cl.isAdmin(rol)) {
                    emp = gson.fromJson(datosEmpleado, Empleado.class);
                    if (emp.getIdEmpleado() == 0) {
                        ce.insert(emp);
                    } else {
                        ce.update(emp);
                    }
                    out = gson.toJson(emp);
                } else {
                    out = """
                          {"errorperm" : "No tienes permiso para realizar esta accion"}
                          """;
                }
            } else {
                out = "{\"errorsec\":\"El token es incorrecto\"}";
            }

        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"exception" : "Formato JSON de Datos Incorrecto"}
                  """;
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"exception" : "%s"}
                  """;
            out = String.format(out, e.toString());
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("id") @DefaultValue("-1") int id,
            @FormParam("token") @DefaultValue("") String token,
            @FormParam("rol") @DefaultValue("") String rol) {

        String out = null;
        Gson gson = new Gson();
        Empleado emp = null;
        ControllerEmpleado ce = new ControllerEmpleado();
        ControllerLogin cl = new ControllerLogin();
        System.out.println(token);
        System.out.println(rol);
        try {
            if (cl.validarToken(token)) {
                if (cl.isAdmin(rol)) {
                    if (id != -1) {
                        try {
                            ce.delete(id);
                            out = """
                                    {"response" : "El empleado ha sido eliminado."}
                                    """;
                        } catch (Exception e) {
                            e.printStackTrace();
                            out = """
                                    {"exception" : "%s"}
                                    """;
                            out = String.format(out, e);
                        }
                    } else {
                        out = """
                                {"response" : "No se ha podido realizar la eliminacion : id no se mando correctamente"}
                                """;
                    }
                } else {
                    out = "{\"errorperm\":\"No tiene permiso para realizar esta operacion.\"}";
                }
            } else {
                out = "{\"errorsec\":\"El token es incorrecto\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"exception" : "%s"}
                  """;
            out = String.format(out, e);
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }
}
