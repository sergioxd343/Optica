/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerMaterial;
import com.prossofteam.oq.model.Material;
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


@Path("material")
public class RESTMaterial {

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro) {
        String out = null;
        ControllerMaterial cm = null;
        List<Material> materiales = null;
        try {
            cm = new ControllerMaterial();
            materiales = cm.getAll(filtro);
            out = new Gson().toJson(materiales);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosMaterial") @DefaultValue("") String datosMaterial) {
        String out = null;
        Gson gson = new Gson();
        Material m = null;
        ControllerMaterial cm = new ControllerMaterial();

        try {
            m = gson.fromJson(datosMaterial, Material.class);
            if (m.getIdMaterial() == 0) {
                cm.insert(m);
            } else {
                cm.update(m);
            }
            out = gson.toJson(m);
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
    
    
}