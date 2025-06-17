package com.ness.zkworkshop.web.model;

public class Subjekt {

    private long id;
    private String kod;
    private String nazev;

    public Subjekt(long id, String kod, String nazev) {
        this.id = id;
        this.kod = kod;
        this.nazev = nazev;
    }

    public long getId() {
        return id;
    }
    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }
    public String getNazev() {
        return nazev;
    }
    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

}