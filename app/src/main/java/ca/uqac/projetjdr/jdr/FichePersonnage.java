package ca.uqac.projetjdr.jdr;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.projetjdr.jdr.exception.ValeurImpossibleException;

public class FichePersonnage {

    private String nomPersonnage;
    private List<Attribut> listeAttributs;

    public FichePersonnage(String nomPersonnage) throws ValeurImpossibleException {
        this.setNomPersonnage(nomPersonnage);
        this.listeAttributs = new ArrayList<>();
    }

    public String getNomPersonnage() {
        return this.nomPersonnage;
    }

    public void setNomPersonnage(String nomPersonnage) throws ValeurImpossibleException {
        if(nomPersonnage == null || nomPersonnage.equals("")){
            throw new ValeurImpossibleException("Le nom du personnage doit être renseigné.");
        }
        this.nomPersonnage = nomPersonnage;
    }

    public List<Attribut> getListeAttributs() {
        return this.listeAttributs;
    }
}