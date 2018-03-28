package ca.uqac.projetjdr.util;

import java.util.ArrayList;

/**
 * Created by Lucas on 23/03/2018.
 */

public class NoeudXML {


    private ArrayList<NoeudXML> listNoeud;
    private String nom;
    private String donne;
    private AttributXML attribute;

    public NoeudXML(String nom) {
        this(nom,null,null,new ArrayList<NoeudXML>());
    }

    public NoeudXML(String nom, String donne, AttributXML attribute) {
        this(nom,donne,attribute,new ArrayList<NoeudXML>());
    }

    public NoeudXML(String nom, String donne, AttributXML attribute,ArrayList<NoeudXML> listNoeud) {
        super();
        this.listNoeud = listNoeud;
        this.nom = nom;
        this.donne = donne;
        this.attribute = attribute;
    }

    public void addNoeud(NoeudXML noeudXML){
        listNoeud.add(noeudXML);
    }

    public ArrayList<NoeudXML> getListNoeud() {
        return listNoeud;
    }

    public void setListNoeud(ArrayList<NoeudXML> listNoeud) {
        this.listNoeud = listNoeud;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDonne() {
        return donne;
    }

    public void setDonne(String donne) {
        this.donne = donne;
    }

    public AttributXML getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributXML attribute) {
        this.attribute = attribute;
    }

    public boolean haveSousElement() {
        return !listNoeud.isEmpty();
    }

    @Override
    public String toString() {
        String res = "";

        res+="[nom="+getNom()+", donne="+getDonne()+", attribute="+getAttribute()+"]\n";
        if(haveSousElement()){
            for (NoeudXML noeudXML : getListNoeud()) {
                res+=noeudXML.toString();
            }
        }


        return res;

    }

    public void addAllNoeud(ArrayList<NoeudXML> lisNoeudXMLs) {
        listNoeud.addAll(lisNoeudXMLs);

    }



}
