package com.prossofteam.oq.model;

public class VentaProducto {
    private Producto producto;
    private int cantidad;
    private Double precioUnitario;
    private Double descuento;
    
    public VentaProducto(){
        
    }

    public VentaProducto(Producto producto, int cantidad, Double precioUnitario, Double descuento) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
    }
    
    

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }
    
    
}
