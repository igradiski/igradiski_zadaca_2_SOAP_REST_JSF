/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.igradiski.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.podaci.Korisnik;


/**
 *
 * @author Korisnik
 */
@Named(value = "azuriranjeKorisnika")
@SessionScoped
public class AzuriranjeKorisnika implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika; 
    
    @Getter
    @Setter
    String korisnickoIme="";
    
    @Getter
    @Setter
    String lozinka="";
    
    @Getter
    @Setter
    String user="";
    
    @Getter
    @Setter
    String ime="";
    
    @Getter
    @Setter
    String prezime="";
    
    @Getter
    @Setter
    String lozinkaUpdate="";
    
    @Getter
    @Setter
    String mail="";
    
    
    /**
     * Creates a new instance of AzuriranjeKorisnika
     */
    public AzuriranjeKorisnika() {
    }
    /**
     * Metoda za preuzimanje podataka o korisniku
     */
    public void preuzmiPodatkeKorisnika(){
        korisnickoIme=prijavaKorisnika.getKorisnik();
        lozinka=prijavaKorisnika.getLozinka();
    }
    /**
     * Metoda koja se koristi za poziv azuriranja korisnika
     * @return 
     */
    public String azurirajKorisnika() {
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        Korisnik k = new Korisnik();
        preuzmiPodatkeKorisnika();
        k = kreirajObjektKorisnik();
        boolean azuriran = zadaca2_1WS.AzurirajKorisnika(k, korisnickoIme, lozinka);
        if (azuriran) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
    /**
     * MEtoda koja kreira objekt korisnika
     * @return  objekt korisnika
     */
    public Korisnik kreirajObjektKorisnik() {
        Korisnik k = new Korisnik();
        k.setKorIme(user);
        k.setEmailAdresa(mail);
        k.setPrezime(prezime);
        k.setIme(ime);
        k.setLozinka(lozinkaUpdate);
        return k;
    }
    /**
     * Metoda koja se poziva te vraća na prethodnu stranu
     */
    public void vratiNazad() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
