package com.ar.desdehasta.pojo;

import java.io.Serializable;

public class Store implements Serializable {
    private String uid;
    public String color;
    public String nombre;
    public String direccion;
    public String barrio;
    public String geo1;
    public String geo2;
    public String telefono;
    public String web;


    public Store(String color, String nombre, String direccion, String barrio, String geo1, String geo2, String telefono, String web) {
        this.color = color;
        this.nombre = nombre;
        this.direccion = direccion;
        this.barrio = barrio;
        this.geo1 = geo1;
        this.geo2 = geo2;
        this.telefono = telefono;
        this.web = web;
    }
    public Store() {
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getGeo1() {
        return geo1;
    }

    public void setGeo1(String geo1) {
        this.geo1 = geo1;
    }

    public String getGeo2() {
        return geo2;
    }

    public void setGeo2(String geo2) {
        this.geo2 = geo2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
    @Override
    public String toString() {
        return getNombre();
    }
}
