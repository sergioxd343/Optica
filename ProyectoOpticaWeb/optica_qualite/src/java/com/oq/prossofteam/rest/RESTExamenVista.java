/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerExamenVista;
import com.prossofteam.oq.core.ControllerLenteContacto;
import com.prossofteam.oq.core.ControllerLogin;
import com.prossofteam.oq.model.ExamenVista;
import com.prossofteam.oq.model.LenteContacto;
import com.google.gson.Gson;
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


@Path("examenvista")
public class RESTExamenVista {

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro,
            @FormParam("token") @DefaultValue("") String token) {
        String out = null;
        ControllerExamenVista cev = null;
        ControllerLogin cl = null;

        List<ExamenVista> lev = null;
        try {
            cl = new ControllerLogin();
            if (cl.validarToken(token)) {
                cev = new ControllerExamenVista();
                lev = cev.getAll(filtro);
                out = new Gson().toJson(lev);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"exception":"Error interno del servidor."}
                  """;
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosEV") @DefaultValue("") String datosEV,
            @FormParam("token") @DefaultValue("") String token) {
        String out = null;
        ExamenVista ev = null;
        Gson gson = new Gson();
        ControllerExamenVista cev = null;
        ControllerLogin cl = null;

        List<ExamenVista> lev = null;
        try {
            if (cl.validarToken(token)) {
                ev = gson.fromJson(datosEV, ExamenVista.class);
                
                cev = new ControllerExamenVista();
                cev.insert(ev);
                out = new Gson().toJson(ev);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"exception":"Error interno del servidor."}
                  """;
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }
}
