/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.prossofteam.rest;

import com.prossofteam.oq.core.ControllerLogin;
import com.prossofteam.oq.model.Empleado;
import com.prossofteam.oq.model.Usuario;
import com.google.gson.Gson;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashSet;


@Path("log")
public class RESTLogin {
    
    @POST
    @Path("in")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("usuario") @DefaultValue("") String usuario,
                            @FormParam("password") @DefaultValue("") String password) throws Exception{
        String out = null;
        Gson gson = new Gson();
        Usuario usu = null;
        Empleado empConsultado = null;
        ControllerLogin cl = new ControllerLogin();
        try {
            
            empConsultado = cl.login(usuario, password);
            if (empConsultado != null) {
                cl.guardarToken(empConsultado);
                out = new Gson().toJson(empConsultado);
            }
            else{
                out = "{\"error\": 'Usuario y/o contraseña incorrectos'}";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("out")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@FormParam("empleado") @DefaultValue("") String empleado) throws Exception{
        String out = null;
        Gson gson = new Gson();
        Empleado empConsultado = null;
        ControllerLogin cl = new ControllerLogin();
        try {
            empConsultado = gson.fromJson(empleado, Empleado.class);
          
            if (cl.eliminarToken(empConsultado)) {
                out = "{\"response\":\"Se cerro la sesion correctamente.\"}";
                System.out.println(out);
            }
            else{
                out = "{\"exception\":\"No se pudo cerrar sesion correctamente\"}";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("verify")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarToken(@FormParam("token") @DefaultValue("") String token) throws Exception{
        String out = null;
        ControllerLogin cl = new ControllerLogin();
        try {
            if (cl.validarToken(token)) {
                out = "{\"success\":\"El token es correcto.\"}";
                System.out.println(out);
            }
            else{
                out = "{\"errorsec\":\"El token es incorrecto\"}";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
