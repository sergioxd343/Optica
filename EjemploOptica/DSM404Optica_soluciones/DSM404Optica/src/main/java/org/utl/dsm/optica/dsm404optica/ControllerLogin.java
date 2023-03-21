package org.utl.dsm.optica.dsm404optica;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.utl.dsm.optica.dsm404optica.controlador.ConexionMySQL;
import org.utl.dsm.optica.dsm404optica.modelo.Respuesta;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {
    @FXML
    private Button btnExit, btnLogin;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUsuario.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                txtPassword.requestFocus();
            }

            if (e.getCode() == KeyCode.ESCAPE) {
                cerrarAplicacion();
            }

            if (e.getCode() == KeyCode.TAB) {
                txtPassword.requestFocus();
            }
        });

        txtPassword.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    validarUsuario();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            if (e.getCode() == KeyCode.TAB) {
                txtUsuario.requestFocus();
            }

            if (e.getCode() == KeyCode.ESCAPE) {
                cerrarAplicacion();
            }
        });
    }

    public void cerrarAplicacion() {
        System.exit(0);
    }

    public void validarUsuario() throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();

        String usuarioCorrecto = null, contaseniaCorrecta = null;
        String user = txtUsuario.getText();
        String password = txtPassword.getText();

        Alert alerta;

        try {
            Connection conn = connMySQL.open();
            PreparedStatement pstmt = conn.prepareStatement("SELECT nombre, contrasenia FROM usuario");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuarioCorrecto = rs.getString(1);
                contaseniaCorrecta = rs.getString(2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (user.equals(usuarioCorrecto)) {
            if (password.equals(contaseniaCorrecta)) {
                Parent principal = FXMLLoader.load(this.getClass().getResource("Principal.fxml"));

                Scene scene = new Scene(principal);
                Stage window = new Stage();

                window.setScene(scene);
                window.setTitle("Optica Qualite");
                window.setMaximized(true);
                window.setMinHeight(650);
                window.setMinWidth(950);
                window.show();

                Stage loginWindow = (Stage) btnLogin.getScene().getWindow();
                loginWindow.close();
            } else {
                alerta = new Alert(Alert.AlertType.ERROR, "Contaseña incorrecta");
                alerta.show();
            }
        } else {
            alerta = new Alert(Alert.AlertType.ERROR, "Nombre de usuario incorrecto");
            alerta.show();
        }
    }

    public void probarRest() {
        HttpResponse apiResponse = null;
        Alert alerta = null;

        try {
            apiResponse = Unirest.get("http://localhost:8080/holarest/api/saludo/saludar").asJson();
            Respuesta respuesta = new Gson().fromJson(apiResponse.getBody().toString(), Respuesta.class);

            alerta = new Alert(Alert.AlertType.INFORMATION, "Mensaje: " +respuesta.getResult());
            alerta.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
