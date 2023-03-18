/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerArmazon;
import com.prossofteam.oq.model.Armazon;
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


@Path("armazon")
public class RESTArmazon {

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro,
                           @FormParam("showDeleted") @DefaultValue("false") boolean showDeleted) {
        String out = null;
        ControllerArmazon ca = null;
        
        List<Armazon> armazones = null;
        try {
            ca = new ControllerArmazon();
            armazones = ca.getAll(filtro, showDeleted);
            out = new Gson().toJson(armazones);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosArmazon") @DefaultValue("") String datosArmazon) {

        String out = null;
        Gson gson = new Gson();
        Armazon arm = null;
        ControllerArmazon ca = new ControllerArmazon();
        try {
            arm = gson.fromJson(datosArmazon, Armazon.class);
            if (arm.getIdArmazon() == 0) {
                ca.insert(arm);
            } else {
                ca.update(arm);
            }
            out = gson.toJson(arm);
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
            out = String.format(out, e);
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("id") @DefaultValue("-1") int id) {
        String out = null;
        Gson gson = new Gson();
        Armazon arm = null;
        ControllerArmazon ca = new ControllerArmazon();
        if (id != -1) {
            try {
                ca.delete(id);
                out = """
                  {"response" : "El producto ha sido eliminado."}
                  """;
            } catch (Exception e) {
                e.printStackTrace();
                out = """
                  {"exception" : "%s"}
                  """;
                out = String.format(out, e);
            }
        }
        else
        {
            out = """
                  {"response" : "No se ha podido realizar la eliminacion : id no se mando correctamente"}
                  """;
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }
}

