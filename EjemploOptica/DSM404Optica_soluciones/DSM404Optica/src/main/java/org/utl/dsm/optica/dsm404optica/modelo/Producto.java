package org.utl.dsm.optica.dsm404optica.modelo;

public class Producto {
    private int idProducto;
    private String codigoBarras;
    private String nombre;
    private String marca;
    private double precioCompra;
    private double precioVenta;
    private int existencias;
    private int estatus;

    public Producto() {
    }

    public int getIdProducto() {
        return this.idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoBarras() {
        return this.codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecioCompra() {
        return this.precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return this.precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getExistencias() {
        return this.existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public int getEstatus() {
        return this.estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
