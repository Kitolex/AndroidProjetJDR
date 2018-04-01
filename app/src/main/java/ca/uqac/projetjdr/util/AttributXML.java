package ca.uqac.projetjdr.util;

/**
 * Created by Lucas on 23/03/2018.
 */

public class AttributXML {
    private String nom;
    private String donne;
    public AttributXML(String nom, String donne) {
        super();
        this.nom = nom;
        this.donne = donne;
    }
    @Override
    public String toString() {
        return "AttributXML [nom=" + nom + ", donne=" + donne + "]";
    }
    public String getNom() {
        return nom;
    }
    public String getDonne() {
        return donne;
    }

}
