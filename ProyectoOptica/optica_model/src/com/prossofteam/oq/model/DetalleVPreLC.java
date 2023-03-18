/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.model;

import java.util.List;


public class DetalleVPreLC {
    private Venta venta;
    private List<VentaPresupuestoLC> ventaPresupuestosLC;

    public DetalleVPreLC() {
    }

    public DetalleVPreLC(Venta venta, List<VentaPresupuestoLC> listaVPre) {
        this.venta = venta;
        this.ventaPresupuestosLC = listaVPre;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public List<VentaPresupuestoLC> getVentaPresupuestosLC() {
        return ventaPresupuestosLC;
    }

    public void setVentaPresupuestosLC(List<VentaPresupuestoLC> ventaPresupuestosLC) {
        this.ventaPresupuestosLC = ventaPresupuestosLC;
    }

    @Override
    public String toString() {
        String mensaje="";
        for (VentaPresupuestoLC listaPresupuesto : ventaPresupuestosLC) {
            mensaje += listaPresupuesto.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DetalleVPre{");
        sb.append("venta=").append(venta);
        sb.append(", listaVPre=").append(ventaPresupuestosLC);
        sb.append('}');
        return sb.toString();
    }
    
    
}
