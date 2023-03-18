/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerTipoMica;
import com.prossofteam.oq.model.TipoMica;
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


@Path("mica")
public class RESTTipoMica {

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro) {
        String out = null;
        ControllerTipoMica ctm = null;
        List<TipoMica> tiposMicas = null;
        try {
            ctm = new ControllerTipoMica();
            tiposMicas = ctm.getAll(filtro);
            out = new Gson().toJson(tiposMicas);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosTipoMica") @DefaultValue("") String datosTipoMica) {
        String out = null;
        Gson gson = new Gson();
        TipoMica tm = null;
        ControllerTipoMica ctm = new ControllerTipoMica();

        try {
            tm = gson.fromJson(datosTipoMica, TipoMica.class);
            if (tm.getIdTipoMica() == 0) {
                ctm.insert(tm);
            } else {
                ctm.update(tm);
            }
            out = gson.toJson(tm);
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
