package com.prossofteam.optica_qualite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorLogin {
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasenia;

    @FXML
    private Button btnSalir;

    @FXML
    private Button btnIngresar;

    public void validar() throws IOException {
        String user, password;
        user = txtUsuario.getText();
        password = txtContrasenia.getText();
        if (user.equals("admin")) {
            if (password.equals("1234")) {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(this.getClass().getResource("/com/prossofteam/optica_qualite/Principal.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Optica Qualite");
                stage.show();
                stage = (Stage) btnIngresar.getScene().getWindow();
                stage.close();
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR,
                        "Contraseña Incorrecta");
                alerta.show();
            }
        }else {
            Alert alerta = new Alert(Alert.AlertType.ERROR,
                    "Usuario Incorrecto");
            alerta.show();
        }
    }
public void salir(){
    Stage stage=new Stage();
    stage = (Stage) btnSalir.getScene().getWindow();
    stage.close();
}
}
