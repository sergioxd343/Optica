package com.prossofteam.oq.model;

public class Armazon {

    private int idArmazon;
    private String modelo;
    private String color;
    private String dimensiones;
    private String descripcion;
    private String fotografia;
    private Producto producto;

    public Armazon() {

    }

    // Getters y setters
    public int getIdArmazon() {
        return idArmazon;
    }

    public void setIdArmazon(int idArmazon) {
        this.idArmazon = idArmazon;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
