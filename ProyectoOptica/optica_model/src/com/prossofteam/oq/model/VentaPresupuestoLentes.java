/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.model;


public class VentaPresupuestoLentes {
    private PresupuestoLentes presupuestoLentes;
    private int cantidad;
    private double precioUnitario;
    private int descuento;

    public VentaPresupuestoLentes() {
    }

    public VentaPresupuestoLentes(PresupuestoLentes presupuestoLentes, int cantidad, double precioUnitario, int descuento) {
        this.presupuestoLentes = presupuestoLentes;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
    }

    public PresupuestoLentes getPresupuestoLentes() {
        return presupuestoLentes;
    }

    public void setPresupuestoLentes(PresupuestoLentes presupuestoLentes) {
        this.presupuestoLentes = presupuestoLentes;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }
    
    
}
