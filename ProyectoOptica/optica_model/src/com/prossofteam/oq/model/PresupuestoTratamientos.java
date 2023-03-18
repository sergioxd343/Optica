package com.prossofteam.oq.model;

public class PresupuestoTratamientos {
    private PresupuestoLentesconArmazon presupuestoLentes;
    private Tratamiento tratamiento;
    
    public PresupuestoTratamientos(){
        
    }

    public PresupuestoLentesconArmazon getPresupuestoLentes() {
        return presupuestoLentes;
    }

    public void setPresupuestoLentes(PresupuestoLentesconArmazon presupuestoLentes) {
        this.presupuestoLentes = presupuestoLentes;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }
    
}
