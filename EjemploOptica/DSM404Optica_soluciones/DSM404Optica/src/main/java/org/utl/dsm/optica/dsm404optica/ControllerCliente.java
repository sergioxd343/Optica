package org.utl.dsm.optica.dsm404optica;


import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.utl.dsm.optica.dsm404optica.controlador.ControladorClientes;
import org.utl.dsm.optica.dsm404optica.modelo.Cliente;
import org.utl.dsm.optica.dsm404optica.modelo.Persona;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerCliente implements Initializable {

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TableColumn<Cliente, Integer> colIdCliente;

    @FXML
    private TableColumn<Cliente, String> colNombreCliente;

    @FXML
    private TableColumn<Cliente, String> colApellidoPaternoCliente;

    @FXML
    private TableColumn<Cliente, String> colApellidoMaternoCliente;

    @FXML
    private TableColumn<Cliente, String> colGeneroCliente;

    @FXML
    private TableColumn<Cliente, String> colRfcCliente;

    @FXML
    private TableColumn<Cliente, String> colTelefonoCasaCliente;

    @FXML
    private TableColumn<Cliente, String> colTelefonoMovilCliente;

    @FXML
    private TableColumn<Cliente, String> colCorreoElectronicoCliente;

    @FXML
    private TableColumn<Cliente, Integer> colEstatusCliente;

    @FXML
    private TextField txtNombreCliente;

    @FXML
    private TextField txtApellidoPaternoCliente;

    @FXML
    private TextField txtApellidoMaternoCliente;

    @FXML
    private ComboBox<String> cmbGeneroCliente;

    @FXML
    private TextField txtRfcCliente;

    @FXML
    private TextField txtFechaNacCliente;

    @FXML
    private TextField txtCalleCliente;

    @FXML
    private TextField txtNumDireccionCliente;

    @FXML
    private TextField txtColoniaCliente;

    @FXML
    private TextField txtCodigoPostalCliente;

    @FXML
    private TextField txtCiudadCliente;

    @FXML
    private ComboBox<String> cmbEstadoCliente;

    @FXML
    private TextField txtTelefonoMovilCliente;

    @FXML
    private TextField txtTelefonoCasaCliente;

    @FXML
    private TextField txtCorreoElectronicoCliente;

    @FXML
    private TextField txtIdPersonaClientr;

    @FXML
    private TextField txtIdCliente;

    @FXML
    private TextField txtNuc;

    @FXML
    private Button btnGuardarCliente;

    @FXML
    private Button btnLimpiarCliente;

    @FXML
    private Button btnEliminarCliente;

    @FXML
    private Label lblIdCliente;

    @FXML
    private Label lblIdPersona;

    @FXML
    private Label lblNumUnico;

    public ObservableList<Cliente> clientesdb;


    ControladorClientes cc;

    ObservableList<String> listaGeneros;
    public ObservableList<String> listaEstados;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cc = new ControladorClientes();
        listaGeneros = FXCollections.observableArrayList();
        listaGeneros.addAll("Masculino","Femenino","Otro");
        listaEstados = FXCollections.observableArrayList();
        listaEstados.addAll("Aguascalientes","Baja California","Baja California Sur","Campeche","Chiapas","Chihuahua","Coahuila de Zaragoza","Colima","Ciudad de México","Durango","Guanajuato","Guerrero","Hidalgo","Jalisco","Estado de Mexico","Michoacan de Ocampo","Morelos","Nayarit","Nuevo Leon","Oaxaca","Puebla","Queretaro","Quintana Roo","San Luis Potosi","Sinaloa","Sonora","Tabasco","Tamaulipas","Tlaxcala","Veracruz","Yucatan","Zacatecas");
        cmbGeneroCliente.setItems(listaGeneros);
        cmbEstadoCliente.setItems(listaEstados);
        lblIdPersona.setText("0");
        lblIdCliente.setText("0");
        lblNumUnico.setText("");

        colIdCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getIdCliente()));
        colNombreCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getNombre()));
        colApellidoPaternoCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getApellidoPaterno()));
        colApellidoMaternoCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getApellidoMaterno()));
        colGeneroCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getGenero()));
        colRfcCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getRfc()));
        colTelefonoCasaCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getTelCasa()));
        colTelefonoMovilCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getTelMovil()));
        colCorreoElectronicoCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getEmail()));
        colEstatusCliente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getEstatus()));

        clientesdb = FXCollections.observableArrayList();


        try{
            clientesdb = cc.getAll();
        }catch (Exception e){
            e.printStackTrace();
        }

        tblClientes.setItems(clientesdb);
    }

    public void save(){
        Cliente c = new Cliente();
        Persona p = new Persona();

        p.setIdPersona(Integer.parseInt(lblIdPersona.getText()));
        p.setNombre(txtNombreCliente.getText());
        p.setApellidoPaterno(txtApellidoPaternoCliente.getText());
        p.setApellidoMaterno(txtApellidoMaternoCliente.getText());
        if(cmbGeneroCliente.getSelectionModel().getSelectedItem().toString() == "Masculino"){
            p.setGenero("M");
        }else if(cmbGeneroCliente.getSelectionModel().getSelectedItem().toString() == "Femenino"){
            p.setGenero("F");
        }else{
            p.setGenero("O");
        }

        p.setFechaNacimiento(txtFechaNacCliente.getText());
        p.setCalle(txtCalleCliente.getText());
        p.setNumero(txtNumDireccionCliente.getText());
        p.setColonia(txtColoniaCliente.getText());
        p.setCiudad(txtCiudadCliente.getText());
        p.setEstado(cmbEstadoCliente.getValue());
        p.setCp(txtCodigoPostalCliente.getText());
        p.setEmail(txtCorreoElectronicoCliente.getText());
        p.setRfc(txtRfcCliente.getText());
        p.setTelCasa(txtTelefonoCasaCliente.getText());
        p.setTelMovil(txtTelefonoMovilCliente.getText());

        c.setIdCliente(Integer.parseInt(lblIdCliente.getText()));
        c.setEstatus(1);
        c.setPersona(p);
        c.setNumeroUnico(lblNumUnico.getText());

        cc.saveCliente(c);

        tblClientes.setItems(cc.getAll());
        tblClientes.refresh();

        clean();
    }

    public void clean(){
        lblIdPersona.setText("0");
        lblIdCliente.setText("0");
        lblNumUnico.setText("");
        txtNombreCliente.setText("");
        txtApellidoPaternoCliente.setText("");
        txtApellidoMaternoCliente.setText("");
        cmbGeneroCliente.setValue("Genero");
        txtFechaNacCliente.setText("");
        txtCalleCliente.setText("");
        txtNumDireccionCliente.setText("");
        txtColoniaCliente.setText("");
        txtCiudadCliente.setText("");
        cmbEstadoCliente.setValue("Estado");
        txtCodigoPostalCliente.setText("");
        txtCorreoElectronicoCliente.setText("");
        txtRfcCliente.setText("");
        txtTelefonoCasaCliente.setText("");
        txtTelefonoMovilCliente.setText("");
    }

    public void select(){
        Cliente clienteSelected = tblClientes.getSelectionModel().getSelectedItem();

        lblIdCliente.setText(String.valueOf(clienteSelected.getIdCliente()));
        lblNumUnico.setText(clienteSelected.getNumeroUnico());
        lblIdPersona.setText(String.valueOf(clienteSelected.getPersona().getIdPersona()));
        txtNombreCliente.setText(clienteSelected.getPersona().getNombre());
        txtApellidoPaternoCliente.setText(clienteSelected.getPersona().getApellidoPaterno());
        txtApellidoMaternoCliente.setText(clienteSelected.getPersona().getApellidoMaterno());

        if(clienteSelected.getPersona().getGenero().equalsIgnoreCase("M")){
            cmbGeneroCliente.setValue("Masculino");
        }else if(clienteSelected.getPersona().getGenero().equalsIgnoreCase("F")){
            cmbGeneroCliente.setValue("Femenino");
        }else{
            cmbGeneroCliente.setValue("Otro");
        }

        txtFechaNacCliente.setText(clienteSelected.getPersona().getFechaNacimiento());
        txtCalleCliente.setText(clienteSelected.getPersona().getCalle());
        txtNumDireccionCliente.setText(clienteSelected.getPersona().getNumero());
        txtColoniaCliente.setText(clienteSelected.getPersona().getColonia());
        txtCiudadCliente.setText(clienteSelected.getPersona().getCiudad());
        cmbEstadoCliente.setValue(clienteSelected.getPersona().getEstado());
        txtCodigoPostalCliente.setText(clienteSelected.getPersona().getCp());
        txtCorreoElectronicoCliente.setText(clienteSelected.getPersona().getEmail());
        txtRfcCliente.setText(clienteSelected.getPersona().getRfc());
        txtTelefonoCasaCliente.setText(clienteSelected.getPersona().getTelCasa());
        txtTelefonoMovilCliente.setText(clienteSelected.getPersona().getTelMovil());
    }

    public void delete(){
        Cliente clienteSelected = tblClientes.getSelectionModel().getSelectedItem();

        if(clienteSelected == null){

        }else{
            int id = clienteSelected.getIdCliente();
            try{
                cc.deleteCliente(id);
            }catch(Exception e){
                e.printStackTrace();
            }
            clean();
            tblClientes.setItems(cc.getAll());
            tblClientes.refresh();
        }
    }

    public void validate(){

    }

}
