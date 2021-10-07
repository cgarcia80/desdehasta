package com.ar.desdehasta.pojo;

public class Circuito {
        private String uid;
        private String nombre;
        private double kilometros;
        private double tiempo;
        private double latitude_ori;
        private double longitude_ori;
        private double latitude_des;
        private double longitude_des;


    public Circuito(String uid, String nombre, double kilometros, double tiempo, double latitude_ori, double longitude_ori, double latitude_des, double longitude_des) {
        this.uid = uid;
        this.nombre = nombre;
        this.kilometros = kilometros;
        this.tiempo = tiempo;
        this.latitude_ori = latitude_ori;
        this.longitude_ori = longitude_ori;
        this.latitude_des = latitude_des;
        this.longitude_des = longitude_des;
    }


    public Circuito(String nombre, double kilometros, double tiempo, double latitude_ori, double longitude_ori) {
        this.nombre = nombre;
        this.kilometros = kilometros;
        this.tiempo = tiempo;
        this.longitude_ori = latitude_ori;
        this.latitude_ori = longitude_ori;
    }
    public Circuito() {}

    public String getNombre() {
        return nombre;
    }

    public double getKilometros() {
        return kilometros;
    }

    public double getTiempo() {
        return tiempo;
    }

    public double getLongitude_ori() {return longitude_ori;}

    public void setLongitude_ori(double longitude_ori) { this.longitude_ori = longitude_ori; }

    public double getLatitude_ori() { return latitude_ori; }

    public void setLatitude_ori(double latitude_ori) { this.latitude_ori = latitude_ori;}

    public double getLatitude_des() { return latitude_des;}

    public void setLatitude_des(double latitude_des) { this.latitude_des = latitude_des; }

    public double getLongitude_des() { return longitude_des; }

    public void setLongitude_des(double longitude_des) { this.longitude_des = longitude_des; }

    public String getUid() { return uid; }

    public void setUid(String uid) {this.uid = uid; }
}
