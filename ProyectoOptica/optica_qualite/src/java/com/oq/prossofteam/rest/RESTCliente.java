/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 * Ivan
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.model.Cliente;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.prossofteam.oq.core.ControllerCliente;
import com.prossofteam.oq.core.ControllerLogin;
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



@Path("cliente")
public class RESTCliente {
    
    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("filtro") @DefaultValue("") String filtro,
                           @FormParam("token") @DefaultValue("") String token,
                           @FormParam("showDeleted") @DefaultValue("false") boolean showDeleted) {
        String out = null;
        ControllerCliente cc = null;
        ControllerLogin cl = null;
        
        List<Cliente> clientes = null;
        try {
            cc = new ControllerCliente();
            cl = new ControllerLogin();
            
            if (cl.validarToken(token)) {
                clientes = cc.getAll(filtro, showDeleted);
                out = new Gson().toJson(clientes);
            }
            else
            {
                out = "{\"errorsec\":\"Error al validar el token.\"}";
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
    public Response save(@FormParam("datosCliente") @DefaultValue("") String datosCliente){
        
        String out = null;
        Gson gson = new Gson();
        Cliente cl = null;
        
        ControllerCliente cc = new ControllerCliente();
        
        try{
            cl = gson.fromJson(datosCliente, Cliente.class);
            if (cl.getIdCliente() == 0) {
                cc.insert(cl);
            } else {
                cc.update(cl);
            }
            out = gson.toJson(cl);
        }catch (JsonParseException jpe) {
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
        Cliente cl = null;
        ControllerCliente cc = new ControllerCliente();
        if (id != -1) {
            try {
                cc.delete(id);
                out = """
                  {"response" : "El cliente ha sido eliminado."}
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
