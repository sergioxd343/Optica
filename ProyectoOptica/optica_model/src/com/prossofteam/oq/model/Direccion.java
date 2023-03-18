/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.oq.model;


public class Direccion {
    private String estado;
    private String municipio;
    private String [] colonias;

    public Direccion(String estado, String municipio, String[] colonias) {
        this.estado = estado;
        this.municipio = municipio;
        this.colonias = colonias;
    }

    public Direccion() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String[] getColonias() {
        return colonias;
    }

    public void setColonias(String[] colonias) {
        this.colonias = colonias;
    }
}
