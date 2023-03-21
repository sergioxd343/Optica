package org.utl.dsm.optica.dsm404optica.controlador;


import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.utl.dsm.optica.dsm404optica.modelo.Cliente;
import org.utl.dsm.optica.dsm404optica.modelo.Persona;


public class ControladorClientes {

    HttpResponse<JsonNode> apiResponse = null;
    Gson gson = null;

    public ObservableList<Cliente> getAll() {
        ObservableList<Cliente> clientes = null;
        try {
            gson = new Gson();
            clientes = FXCollections.observableArrayList();
            apiResponse = Unirest.get("http://localhost:8080/optica_web_v3/api/cliente/getAll").asJson();
            JsonParser parseJson = new JsonParser();
            JsonArray jsonArr = parseJson.parse(apiResponse.getBody().toString()).getAsJsonArray();

            for (JsonElement cjson : jsonArr) {

                JsonObject ob = cjson.getAsJsonObject();

                Cliente c = new Cliente();
                Persona p = new Persona();

                c.setIdCliente(ob.get("idCliente").getAsInt());
                c.setNumeroUnico(ob.get("numeroUnico").getAsString());
                c.setEstatus(ob.get("estatus").getAsInt());

                JsonObject pjson = ob.get("persona").getAsJsonObject();

                p.setIdPersona(pjson.get("idPersona").getAsInt());
                p.setNombre(pjson.get("nombre").getAsString());
                p.setApellidoPaterno(pjson.get("apellidoPaterno").getAsString());
                p.setApellidoMaterno(pjson.get("apellidoMaterno").getAsString());
                p.setGenero(pjson.get("genero").getAsString());
                p.setFechaNacimiento(pjson.get("fechaNacimiento").getAsString());
                p.setCalle(pjson.get("calle").getAsString());
                p.setNumero(pjson.get("numero").getAsString());
                p.setColonia(pjson.get("colonia").getAsString());
                p.setCp(pjson.get("cp").getAsString());
                p.setCiudad(pjson.get("ciudad").getAsString());
                p.setEstado(pjson.get("estado").getAsString());
                p.setTelMovil(pjson.get("telMovil").getAsString());
                p.setTelCasa(pjson.get("telCasa").getAsString());
                p.setEmail(pjson.get("email").getAsString());
                p.setRfc(pjson.get("rfc").getAsString());

                c.setPersona(p);

                clientes.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public void saveCliente(Cliente c){
        try {
            String datos = "";
            gson = new Gson();
            datos = gson.toJson(c);
            System.out.println(datos);
            apiResponse = Unirest.post("http://localhost:8080/optica_web_v3/api/cliente/save")
                    .field("datosCliente",datos).asJson();
            System.out.println(apiResponse.getBody().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteCliente(int id){
        try{
            apiResponse = Unirest.post("http://localhost:8080/optica_web_v3/api/cliente/delete")
                    .field("id",id).asJson();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
