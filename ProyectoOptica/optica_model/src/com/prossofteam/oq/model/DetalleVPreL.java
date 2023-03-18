/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.model;

import java.util.List;


public class DetalleVPreL {
    private Venta venta;
    private List<VentaPresupuestoLentes> ventaPresupuestoLentes;

    public DetalleVPreL() {
    }

    public DetalleVPreL(Venta venta, List<VentaPresupuestoLentes> ventaPresupuestoLentes) {
        this.venta = venta;
        this.ventaPresupuestoLentes = ventaPresupuestoLentes;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public List<VentaPresupuestoLentes> getVentaPresupuestoLentes() {
        return ventaPresupuestoLentes;
    }

    public void setVentaPresupuestoLentes(List<VentaPresupuestoLentes> ventaPresupuestoLentes) {
        this.ventaPresupuestoLentes = ventaPresupuestoLentes;
    }
    
    
}
