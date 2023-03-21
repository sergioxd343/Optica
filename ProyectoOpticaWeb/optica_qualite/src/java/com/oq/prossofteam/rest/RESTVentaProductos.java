/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerLogin;
import com.prossofteam.oq.core.ControllerVenta;
import com.prossofteam.oq.model.Producto;
import com.prossofteam.oq.model.DetalleVP;
import com.google.gson.Gson;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import jakarta.ws.rs.POST;


@Path("venta_producto")
public class RESTVentaProductos {
    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro,
            @FormParam("token") @DefaultValue("") String token) {
        String out = null;
        ControllerVenta cvp = null;
        ControllerLogin cl = null;
        List<Producto> productos = null;

        try {
            cl = new ControllerLogin();
            if (cl.validarToken(token)) {
                cvp = new ControllerVenta();
                productos = cvp.getAll(filtro);
                out = new Gson().toJson(productos);
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
    @Path("createVentaP")
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearVentaP(@FormParam ("datosVP") @DefaultValue("") String datosVP,
                                @FormParam ("token") @DefaultValue("") String token)
    {
        String out = null;
        DetalleVP dvp = null;
        Gson gson = new Gson();
        ControllerLogin cl = null;
        ControllerVenta cv = null;
        boolean realizado;
        
        try {
            cl = new ControllerLogin();
            if (cl.validarToken(token)) {
                cv = new ControllerVenta();
                dvp = gson.fromJson(datosVP, DetalleVP.class);
                realizado = cv.transaccionarVenta(dvp);
                if (realizado) {
                    out = """
                          {"response": "Se ha realizado la venta"}
                          """;
                }else
                {
                    out = """
                          {"error": "Hubo un error"}
                          """;
                }
            }
            else
            {
                out = """
                      {"errorsec": "Error al validar el token"}
                      """;
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
