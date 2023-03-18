package com.prossofteam.oq.model;

public class Compra {
    private int idCompra;
    private Empleado empleado;
    
    public Compra(){
        
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    
}
