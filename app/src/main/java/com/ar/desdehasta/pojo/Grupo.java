package com.ar.desdehasta.pojo;

public class Grupo {
    private String uid;
    private String nombre;
    private String circuito;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = nombre;
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        circuito = circuito;
    }



    @Override
    public String toString() {
        return getNombre();
    }
}
