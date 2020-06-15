package com.example.azertyt.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Plat
{
    private String idPlat;
    private String nom;
    private int nbrePlat;
    private String taille;

    public Plat(String idPlat,String nom, int nbrePlat, String taille) {
        this.idPlat=idPlat;
        this.nom = nom;
        this.nbrePlat = nbrePlat;
        this.taille = taille;
    }

    public String getIdPlat() {
        return idPlat;
    }

    public void setIdPlat(String idPlat) {
        this.idPlat = idPlat;
    }

    public int getNbrePlat() {
        return nbrePlat;
    }

    public String getTaille() {
        return taille;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNbrePlat(int nbrePlat) {
        this.nbrePlat = nbrePlat;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

}
