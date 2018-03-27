package ca.uqac.projetjdr.jdr;

public abstract class Valeur<T> {

    public T valeur;

    public Valeur(T valeur){
        this.valeur = valeur;
    }
}
