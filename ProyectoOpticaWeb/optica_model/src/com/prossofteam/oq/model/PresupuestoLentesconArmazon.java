package com.prossofteam.oq.model;

public class PresupuestoLentesconArmazon {
    private int idPresupuestoleentes;
    private Presupuesto presupuesto;
    private int alturaOblea;
    private TipoMica tipoMica;
    private Material material;
    private Armazon armazon;
    
    public PresupuestoLentesconArmazon(){
        
    }

    public int getIdPresupuestoleentes() {
        return idPresupuestoleentes;
    }

    public void setIdPresupuestoleentes(int idPresupuestoleentes) {
        this.idPresupuestoleentes = idPresupuestoleentes;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public int getAlturaOblea() {
        return alturaOblea;
    }

    public void setAlturaOblea(int alturaOblea) {
        this.alturaOblea = alturaOblea;
    }

    public TipoMica getTipoMica() {
        return tipoMica;
    }

    public void setTipoMica(TipoMica tipoMica) {
        this.tipoMica = tipoMica;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Armazon getArmazon() {
        return armazon;
    }

    public void setArmazon(Armazon armazon) {
        this.armazon = armazon;
    }
    
    
}
