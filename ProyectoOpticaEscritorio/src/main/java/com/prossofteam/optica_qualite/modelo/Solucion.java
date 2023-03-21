/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prossofteam.optica_qualite.modelo;

/**
* Retorno de valores
* Esta clase funciona para asignarle los valores de la solucion, estas podran mostrar o regresar los valores
* @author Sergio Hernandez
* @version 0.1, 2022/11/09
*/
public class
Solucion{

    private int idSolucion;
    private Producto producto;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Metodo para regresar el id de la solucion
     * @return Regresa el id de la solucion
     *
     */


    public int getIdSolucion() {
        return idSolucion;
    }

    public void setIdSolucion(int idSolucion) {
        this.idSolucion = idSolucion;
    }
    /**
     * Metodo para regresar toda la clase Producto
     * @return Regresa el Produto
     */




}
