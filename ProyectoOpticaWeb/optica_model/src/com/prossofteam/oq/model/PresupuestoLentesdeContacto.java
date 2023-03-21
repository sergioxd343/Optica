package com.prossofteam.oq.model;

public class PresupuestoLentesdeContacto {
    private int idPrespuestoLentesContacto;
    private ExamenVista examenVista;
    private LenteContacto lenteContacto;
    private Presupuesto presupuesto;
    private String clave;
    
    public PresupuestoLentesdeContacto(){
        
    }

    public int getIdPrespuestoLentesContacto() {
        return idPrespuestoLentesContacto;
    }

    public void setIdPrespuestoLentesContacto(int idPrespuestoLentesContacto) {
        this.idPrespuestoLentesContacto = idPrespuestoLentesContacto;
    }

    public ExamenVista getExamenVista() {
        return examenVista;
    }

    public void setExamenVista(ExamenVista examenVista) {
        this.examenVista = examenVista;
    }

    public LenteContacto getLenteContacto() {
        return lenteContacto;
    }

    public void setLenteContacto(LenteContacto lenteContacto) {
        this.lenteContacto = lenteContacto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }
       
}
