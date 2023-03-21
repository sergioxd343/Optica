/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerDireccion;
import com.prossofteam.oq.model.Direccion;
import com.google.gson.Gson;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("direccion")
public class RESTDireccion {
    
    @GET
    @Path("getDireccion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDireccion(@QueryParam("cp") @DefaultValue("0") int cp){
        String out = "";
        ControllerDireccion cd = null;
        Direccion direcciones = null;
        try {
            cd = new ControllerDireccion();
            direcciones = cd.buscarMunEst(cp);
            out = new Gson().toJson(direcciones);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
}
