package com.prossofteam.optica_qualite;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.prossofteam.optica_qualite.controlador.ControladorSoluciones;
import  com.prossofteam.optica_qualite.modelo.Solucion;
import com.prossofteam.optica_qualite.modelo.Producto;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerSolucion implements Initializable {

    @FXML
    private TableView<Solucion> tblSolucion;

    @FXML
    private TableColumn<Solucion, Integer> colIdSolucion;

    @FXML
    private TableColumn<Solucion, String> colCodigoBarras;

    @FXML
    private TableColumn<Solucion, String> colNombre;

    @FXML
    private TableColumn<Solucion, String> colMarca;

    @FXML
    private TableColumn<Solucion, Double> colPrecioCompra;

    @FXML
    private TableColumn<Solucion, Double> colPrecioVenta;

    @FXML
    private TableColumn<Solucion, Integer> colExistencias;

    @FXML
    private TableColumn<Solucion, Integer> colEstatus;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtPrecioVenta;

    @FXML
    private TextField txtPrecioCompra;

    @FXML
    private TextField txtIdProducto;

    @FXML
    private TextField txtIdSolucion;

    @FXML
    private TextField txtCodigoBarras;

    @FXML
    private TextField txtExistencias;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Label lblValidarNombre;

    @FXML
    private Label lblValidarMarca;

    @FXML
    private Label lblValidarPrecioCompra;

    @FXML
    private Label lblValidarPrecioVenta;

    @FXML
    private Label lblValidarExistencias;

    public ObservableList<Solucion> listaSoluciones;
    ControladorSoluciones cs;

    Solucion solucionSelected;

    public void refrescarTabla() {
        try {
            listaSoluciones = FXCollections.observableArrayList(cs.getAllSoluciones());
            tblSolucion.setItems(listaSoluciones);
            tblSolucion.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cs = new ControladorSoluciones();
        lblValidarNombre.setText("");
        lblValidarMarca.setText("");
        lblValidarPrecioCompra.setText("");
        lblValidarPrecioVenta.setText("");
        lblValidarExistencias.setText("");

        colIdSolucion.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getIdSolucion()));
        colCodigoBarras.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getCodigoBarras()));
        colNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getNombre()));
        colMarca.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getMarca()));
        colPrecioCompra.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioCompra()));
        colPrecioVenta.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioVenta()));
        colExistencias.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getExistencias()));
        colEstatus.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getEstatus()));

        refrescarTabla();
        btnEliminar.setDisable(true);
    }

    public void save() {
        if (validarVacios()) {
            if (validarNumeros()) {
                if (validarPrecios()) {
                    if (validarExistencias()) {
                        Solucion solucion = new Solucion();
                        Producto producto = new Producto();
                        if (txtIdSolucion.getText().trim().length() < 1) {
                            solucion.setIdSolucion(0);
                            producto.setIdProducto(0);
                            producto.setCodigoBarras("");
                        } else {
                            solucion.setIdSolucion(Integer.parseInt(txtIdSolucion.getText()));
                            producto.setIdProducto(Integer.parseInt(txtIdProducto.getText()));
                            producto.setCodigoBarras(txtCodigoBarras.getText());
                        }

                        producto.setNombre(txtNombre.getText());
                        producto.setMarca(txtMarca.getText());
                        producto.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
                        producto.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
                        producto.setExistencias(Integer.parseInt(txtExistencias.getText()));
                        producto.setEstatus(1);

                        solucion.setProducto(producto);
                        Solucion nueva = cs.save(solucion);

                        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Producto insertado");
                        alerta.show();
                        refrescarTabla();
                        txtIdSolucion.setText(String.valueOf(nueva.getIdSolucion()));
                        txtIdProducto.setText(String.valueOf(nueva.getProducto().getIdProducto()));
                        txtCodigoBarras.setText(nueva.getProducto().getCodigoBarras());
                        btnEliminar.setDisable(false);
                    }
                }
            }
        }

    }

    public void clean() {
        txtIdSolucion.setText("");
        txtIdProducto.setText("");
        txtCodigoBarras.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        txtExistencias.setText("");

        cleanValidate();

        btnEliminar.setDisable(true);
    }

    public void select() {
        solucionSelected = tblSolucion.getSelectionModel().getSelectedItem();

        txtIdProducto.setText(String.valueOf(solucionSelected.getProducto().getIdProducto()));
        txtIdSolucion.setText(String.valueOf(solucionSelected.getIdSolucion()));
        txtCodigoBarras.setText(solucionSelected.getProducto().getCodigoBarras());
        txtNombre.setText(solucionSelected.getProducto().getNombre());
        txtMarca.setText(solucionSelected.getProducto().getMarca());
        txtPrecioCompra.setText(String.valueOf(solucionSelected.getProducto().getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(solucionSelected.getProducto().getPrecioVenta()));
        txtExistencias.setText(String.valueOf(solucionSelected.getProducto().getExistencias()));
    }

    public void delete() {
        solucionSelected = tblSolucion.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de confirmar la eliminación?");
        Optional<ButtonType> action = alert.showAndWait();

        // Si hemos pulsado en aceptar
        if (action.get() == ButtonType.OK) {
            int id = solucionSelected.getIdSolucion();
            try {
                cs.delete(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            refrescarTabla();
            clean();

            btnEliminar.setDisable(true);
        }
    }

    public void cleanValidate() {
        lblValidarNombre.setText("");
        lblValidarMarca.setText("");
        lblValidarPrecioCompra.setText("");
        lblValidarPrecioVenta.setText("");
        lblValidarExistencias.setText("");
    }

    public boolean validarVacios() {
        cleanValidate();
        boolean valido = true;
        if (txtNombre.getText().equals("")) {
            lblValidarNombre.setText("Ingrese un nombre");
            valido = false;
        }
        if (txtMarca.getText().equals("")) {
            lblValidarMarca.setText("Ingresa una marca");
            valido = false;
        }
        if (txtPrecioCompra.getText().equals("")) {
            lblValidarPrecioCompra.setText("Ingresa un precio de compra valido");
            valido = false;
        }
        if (txtPrecioVenta.getText().equals("")) {
            lblValidarPrecioVenta.setText("Ingresa un precio de venta valido");
            valido = false;
        }
        if (txtExistencias.getText().equals("")) {
            lblValidarExistencias.setText("Ingresa una existencia valida");
            valido = false;
        }

        return valido;
    }

    public boolean validarNumeros() {
        cleanValidate();
        boolean validado = true;
        if (!validarDecimal(txtPrecioCompra.getText())) {
            lblValidarPrecioCompra.setText("Ingrese un precio de compra valido");
            validado = false;
        }
        if (!validarDecimal(txtPrecioVenta.getText())) {
            lblValidarPrecioVenta.setText("Ingrese un precio de venta valido");
            validado = false;
        }
        if (!validarEntero(txtExistencias.getText())) {
            lblValidarExistencias.setText("Ingrese una cantidad valida");
            validado = false;
        }
        return validado;
    }

    public boolean validarPrecios() {
        cleanValidate();
        double precioVenta = Double.parseDouble(txtPrecioVenta.getText()), precioCompra = Double.parseDouble(txtPrecioCompra.getText());
        boolean validado = true;
        if (precioVenta <= 0 || precioCompra <= 0) {
            if (precioCompra <= 0) {
                lblValidarPrecioCompra.setText("Ingrese un precio de compra valido, mayor a cero");
                validado = false;
            }
            if (precioVenta <= 0) {
                lblValidarPrecioVenta.setText("Ingrese un precio de venta valido, mayor a cero");
            }
        } else {
            if (precioCompra >= precioVenta) {
                lblValidarPrecioCompra.setText("Precio de compra es mayor o igual al precio de venta, revisar");
                lblValidarPrecioVenta.setText("Precio de venta es meno o igual al precio de compra, revisar");
                validado = false;
            }
        }
        return validado;
    }

    public boolean validarExistencias() {
        cleanValidate();
        boolean validado = true;
        if (Double.parseDouble(txtExistencias.getText()) <= 0) {
            lblValidarExistencias.setText("Ingrese una cantidad valida, debe ser mayor a cero");
            validado = false;
        }
        return validado;
    }

    public boolean validarEntero(String s) {
        if (s == null || s.equals("")) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public boolean validarDecimal(String s) {
        if (s == null || s.equals("")) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < '0' || c > '9') && c != '.') {
                return false;
            }
        }
        return true;
    }

}
