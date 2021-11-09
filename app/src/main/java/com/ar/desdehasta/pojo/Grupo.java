package com.ar.desdehasta.pojo;

import java.io.Serializable;

public class Grupo{
    private String uid;
    private String Nombre;
    private String Circuito;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCircuito() {
        return Circuito;
    }

    public void setCircuito(String circuito) {
        Circuito = circuito;
    }



    @Override
    public String toString() {
        return getNombre();
    }
}
