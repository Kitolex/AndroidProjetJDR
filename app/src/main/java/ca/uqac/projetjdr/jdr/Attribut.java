package ca.uqac.projetjdr.jdr;

import java.util.ArrayList;
import java.util.List;

public class Attribut<T> {

    public String nom;
    public T valeur;
    public List<Attribut> listeSousAttributs;

    public Attribut(){
        this.listeSousAttributs = new ArrayList<>();
    }
}
