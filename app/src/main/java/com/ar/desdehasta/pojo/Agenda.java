package com.ar.desdehasta.pojo;

public class Agenda {
    String fecha;
    String Circuito;

    public Agenda() {
    }

    public Agenda(String fecha, String circuito) {
        this.fecha = fecha;
        Circuito = circuito;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCircuito() {
        return Circuito;
    }

    public void setCircuito(String circuito) {
        Circuito = circuito;
    }
}
