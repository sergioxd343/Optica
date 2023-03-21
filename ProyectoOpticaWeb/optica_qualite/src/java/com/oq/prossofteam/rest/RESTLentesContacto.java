package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerLenteContacto;
import com.prossofteam.oq.core.ControllerLogin;
import com.prossofteam.oq.model.LenteContacto;
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

@Path("lentescontacto")
public class RESTLentesContacto {

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro,
            @FormParam("showDeleted") @DefaultValue("false") boolean showDeleted,
            @FormParam("token") @DefaultValue("") String token) {
        String out = null;
        ControllerLenteContacto clc = null;
        ControllerLogin cl = null;

        List<LenteContacto> lcs = null;
        try {
            cl = new ControllerLogin();
            if (cl.validarToken(token)) {
                clc = new ControllerLenteContacto();
                lcs = clc.getAll(filtro,showDeleted);
                out = new Gson().toJson(lcs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"exception":"Error interno del servidor."}
                  """;
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datos") @DefaultValue("") String datos) {
        String out = null;
        Gson gson = new Gson();
        LenteContacto lente = null;
        ControllerLenteContacto clc = new ControllerLenteContacto();

        try {
            lente = gson.fromJson(datos, LenteContacto.class);
            if (lente.getIdLenteContacto() == 0) {
                clc.insert(lente);
            } else {
                clc.update(lente);
            }
            out = gson.toJson(lente);
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"exception":"Formato de datos incorrecto"}
                  """;
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"exception":"%s}
                  """;
            out = String.format(out, e.toString());
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("id") @DefaultValue("-1") int id) {
        String out = null;
        Gson gson = new Gson();
        LenteContacto lc = null;
        ControllerLenteContacto clc = new ControllerLenteContacto();
        if (id != -1) {
            try {
                clc.delete(id);
                out = """
                  {"response":"Lente de contacto eliminado exitosamente"}
                  """;
            } catch (Exception e) {
                e.printStackTrace();
                out = """
                  {"exception":"%s"}
                  """;
                out = String.format(out, e.toString());
            }
        } else {
            out = """
                  {"response" : "No se ha podido realizar la eliminacion : id no se mando correctamente"}
                  """;
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }
}
