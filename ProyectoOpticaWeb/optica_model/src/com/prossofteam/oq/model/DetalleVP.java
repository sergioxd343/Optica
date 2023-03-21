/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.model;

import java.util.ArrayList;
import java.util.List;


public class DetalleVP {
    private Venta venta;
    private List<VentaProducto> listaProductos;

    public DetalleVP() {
    }

    public DetalleVP(Venta venta, List<VentaProducto> listaProductos) {
        this.venta = venta;
        this.listaProductos = listaProductos;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public List<VentaProducto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<VentaProducto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @Override
    public String toString() {
        String mensaje="";
        for (VentaProducto listaProducto : listaProductos) {
            mensaje += listaProducto.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DetalleVP{");
        sb.append("venta=").append(venta);
        sb.append(", listaProductos=").append(mensaje);
        sb.append('}');
        return sb.toString();
    }
    
    
    
    
}
