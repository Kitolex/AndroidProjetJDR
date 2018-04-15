package ca.uqac.projetjdr.jdr;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.projetjdr.jdr.exception.ValeurImpossibleException;

public class Attribut {

    private int id;
    private String nom;
    private String valeur;
    private List<Attribut> listeSousAttributs;

    public Attribut(String nom, String valeur) throws ValeurImpossibleException {
        this.setNom(nom);
        this.valeur = valeur;
        this.listeSousAttributs = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
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

    public void setListeSousAttributs(List<Attribut> l){
		this.listeSousAttributs = l;
	}

    @Override
    public String toString(){

        String result = nom + " : " + valeur + "\n";

        for(Attribut a : listeSousAttributs){
            result += a.toString();
        }

        return result;
    }

}
