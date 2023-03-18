package com.prossofteam.oq.model;

public class VentaPresupuestoLC {
    private Venta venta;
    private PresupuestoLentesdeContacto presupuestoLC;
    private int cantidad;
    private Double precioUnitario;
    private Double descuento;
    
    public VentaPresupuestoLC(){
        
    }

    public VentaPresupuestoLC(Venta venta, PresupuestoLentesdeContacto presupuestoLC, int cantidad, Double precioUnitario, Double descuento) {
        this.venta = venta;
        this.presupuestoLC = presupuestoLC;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
    }
    
    

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public PresupuestoLentesdeContacto getPresupuestoLC() {
        return presupuestoLC;
    }

    public void setPresupuestoLC(PresupuestoLentesdeContacto presupuestoLC) {
        this.presupuestoLC = presupuestoLC;
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
