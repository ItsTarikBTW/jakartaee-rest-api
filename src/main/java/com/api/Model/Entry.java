package com.api.Model;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Entry implements Serializable {
    @XmlElement
    private String nom;
    @XmlElement
    private Address address;
    
    public Entry(String nom, Address address) {
        this.nom = nom;
        this.address = address;
    }
    public Address getAddress(){
        return this.address;
    }
    public String getNom(){
        return this.nom;
    }
    public void setAddress(Address address){
        this.address=address;
    }
    public void setNom(String nom){
        this.nom=nom;
    }
}