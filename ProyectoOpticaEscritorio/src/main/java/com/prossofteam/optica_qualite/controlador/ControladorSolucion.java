package com.prossofteam.optica_qualite.controlador;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.prossofteam.optica_qualite.modelo.Solucion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControladorSolucion {
    HttpResponse<JsonNode> apiResponse = null;
    Gson gson = null;

    public int saveSolucion(Solucion s) throws UnirestException {
        try {
            String datos = "";
            gson = new Gson();
            datos = gson.toJson(s);
            System.out.println(datos);
            apiResponse = Unirest.post("http://localhost:8080/opticaQualite/api/solucion/save")
                    .field("datosSolucion",datos).asJson();
            System.out.println(apiResponse.getBody().toString());
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


}
