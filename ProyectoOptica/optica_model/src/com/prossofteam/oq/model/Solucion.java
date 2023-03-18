package com.prossofteam.oq.model;

public class Solucion{
    private int idSolucion;
    private Producto producto;
    
    public Solucion(){
        
    }
    
    public int getIdSolucion() {
        return idSolucion;
    }

    public void setIdSolucion(int idSolucion) {
        this.idSolucion = idSolucion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    
}
