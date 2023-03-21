package org.utl.dsm.optica.dsm404optica.modelo;


public class Solucion {
    private int idSolucion;
    private Producto producto;

    public Solucion() {
    }

    public int getIdSolucion() {
        return this.idSolucion;
    }

    public void setIdSolucion(int idSolucion) {
        this.idSolucion = idSolucion;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
