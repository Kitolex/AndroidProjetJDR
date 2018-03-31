package ca.uqac.projetjdr.jdr;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.projetjdr.jdr.exception.ValeurImpossibleException;

public class Attribut {

    private String nom;
    private String valeur;
    private List<Attribut> listeSousAttributs;

    public Attribut(String nom, String valeur) throws ValeurImpossibleException {
        this.setNom(nom);
        this.valeur = valeur;
        this.listeSousAttributs = new ArrayList<>();
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) throws ValeurImpossibleException {
        if(nom == null || nom.equals("")){
            throw new ValeurImpossibleException("Le nom de l'attribut doit être renseigné.");
        }
        this.nom = nom;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public List<Attribut> getListeSousAttributs() {
        return listeSousAttributs;
    }
}
