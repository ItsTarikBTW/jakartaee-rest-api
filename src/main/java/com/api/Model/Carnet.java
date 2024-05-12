package com.api.Model;

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@XmlRootElement
public class Carnet implements Serializable {
    private int id;
    private String nom;
    private ArrayList<Entry> ensemble;

    public Carnet(int id) {
        this.id = id;
        this.nom = "Carnet" + id;
        this.ensemble = new ArrayList<Entry>();
    }

    public Carnet(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.ensemble = new ArrayList<Entry>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @XmlElement
    public int getId() {
        return this.id;
    }

    @XmlElement
    public String getNom() {
        return this.nom;
    }
    //setter and getter for ensemble
    public void setEnsemble(ArrayList<Entry> ensemble) {
        this.ensemble = ensemble;
    }
    @XmlElement
    public ArrayList<Entry> getEnsemble() {
        return this.ensemble;
    }

    public void enregistrer(String name, Address newAddress) {
        try {
            ensemble.add(new Entry(name, newAddress));
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void effacer(String name) {
        try {
            ensemble.removeIf(entry -> entry.getNom().equals(name));
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public Address chercher(String name) {
        for (Entry e : ensemble) {
            if (e.getNom().equals(name)) {
                return e.getAddress();
            }
        }
        return null;
    }

    public void lister() {

        for (Entry e : ensemble) {
            System.out.println(e.getNom() + " : " + e.getAddress());
        }
        if (ensemble.isEmpty()) {
            System.out.println("Le carnet est vide");
        }
    }

    public String toString() {
        String header = "Carnet " + this.id + " : " + this.nom + "\n";
        String body = "";
        for (Entry e : ensemble) {
            body += "\t" + e.getNom() + " : " + e.getAddress() + "\n";
        }
        return header + body;
    }

}
