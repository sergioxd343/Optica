/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerAccesorio;
import com.prossofteam.oq.model.Accesorio;
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

@Path("accesorio")
public class RESTAccesorio {

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro,
                           @FormParam("showDeleted") @DefaultValue("false") boolean showDeleted) {
        String out = null;
        ControllerAccesorio ca = null;
        
        List<Accesorio> accesorio = null;
        try {
            ca = new ControllerAccesorio();
            accesorio = ca.getAll(filtro, showDeleted);
            out = new Gson().toJson(accesorio);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosAccesorio") @DefaultValue("") String datosAccesorio) throws Exception {

        String out = null;
        Gson gson = new Gson();
        Accesorio acc = null;
        ControllerAccesorio ca = new ControllerAccesorio();

        try {
            acc = gson.fromJson(datosAccesorio, Accesorio.class);
            if (acc.getIdAccesorio() == 0) {
                ca.insert(acc);
            } else {
                ca.update(acc);
            }
            out = gson.toJson(acc);
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
    public Response delete(@FormParam("id") @DefaultValue("-1") int id) {

        String out = null;
        Gson gson = new Gson();
        Accesorio acc = null;
        ControllerAccesorio ca = new ControllerAccesorio();
        if (id != -1) {
            try {
                ca.delete(id);
                out = """
                  {"response" : "El Accesorio ha sido eliminado."}
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

        return Response.status(Response.Status.OK).entity(out).build();
    }

}
