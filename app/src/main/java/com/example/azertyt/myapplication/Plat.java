package com.example.azertyt.myapplication;

public class Plat
{
    private String nbrePlat;
    private String taille;


    public String getNbrePlat() {
        return nbrePlat;
    }

    public String getTaille() {
        return taille;
    }


    public Plat(String nbrePlat,String taille)
    {
        this.nbrePlat=nbrePlat;
        this.taille=taille;
    }
}
