package com.prossofteam.optica_qualite.controlador;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.prossofteam.optica_qualite.modelo.Producto;
import com.prossofteam.optica_qualite.modelo.Solucion;

public class ControladorSoluciones {
    HttpResponse<JsonNode> apiResponse = null;
    Gson gson = null;

    public ObservableList<Solucion> getAllSoluciones() {
        ObservableList<Solucion> soluciones = null;
        try {
            gson = new Gson();
            soluciones = FXCollections.observableArrayList();
            apiResponse = Unirest.get("http://localhost:8080/optica_web_v3/api/solucion/getAll").asJson();
            JsonParser parseJson = new JsonParser();
            JsonArray jsonArr = parseJson.parse(apiResponse.getBody().toString()).getAsJsonArray();

            for (JsonElement cjson : jsonArr) {

                JsonObject ob = cjson.getAsJsonObject();
                Solucion solucion = new Solucion();
                Producto producto = new Producto();

                solucion.setIdSolucion(ob.get("idSolucion").getAsInt());

                JsonObject pjson = ob.get("producto").getAsJsonObject();
                producto.setIdProducto(pjson.get("idProducto").getAsInt());
                producto.setCodigoBarras(pjson.get("codigoBarras").getAsString());
                producto.setNombre(pjson.get("nombre").getAsString());
                producto.setMarca(pjson.get("marca").getAsString());
                producto.setPrecioCompra(pjson.get("precioCompra").getAsDouble());
                producto.setPrecioVenta(pjson.get("precioVenta").getAsDouble());
                producto.setExistencias(pjson.get("existencias").getAsInt());
                producto.setEstatus(pjson.get("estatus").getAsInt());

                solucion.setProducto(producto);
                soluciones.add(solucion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return soluciones;
    }

    public Solucion save(Solucion solucion){
        try {
            String datos = "";
            gson = new Gson();
            datos = gson.toJson(solucion);
            apiResponse = Unirest.post("http://localhost:8080/optica_web_v3/api/solucion/save")
                    .field("datosSolucion",datos).asJson();
            System.out.println(apiResponse.getBody().toString());
            Solucion solucionNueva = gson.fromJson(apiResponse.getBody().toString(), Solucion.class);
            System.out.println(solucionNueva);
            return solucionNueva;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void delete(int id){
        try{
            apiResponse = Unirest.post("http://localhost:8080/optica_web_v3/api/solucion/delete")
                    .field("id",id).asJson();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
