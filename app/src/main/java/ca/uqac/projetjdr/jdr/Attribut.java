package ca.uqac.projetjdr.jdr;

import java.util.ArrayList;
import java.util.List;

public class Attribut {

    public String nom;
    public Valeur valeur;
    public List<Attribut> listeSousAttributs;

    public Attribut(){
        this.listeSousAttributs = new ArrayList<>();
    }
}
