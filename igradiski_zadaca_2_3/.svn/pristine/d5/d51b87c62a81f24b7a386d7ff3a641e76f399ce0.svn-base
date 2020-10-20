/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.Application;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.igradiski.ws.klijenti.Zadaca2_1WS;


/**
 *
 * @author Korisnik
 */
@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @Getter
    @Setter
    private String korisnik;
    @Getter
    @Setter
    private String lozinka;
    @Getter
    @Setter
    private boolean prijavljen;
    @Getter
    @Setter
    private String login="Prijava";
    /**
     * Creates a new instance of PrijavaKorisnika
     */
    public PrijavaKorisnika() {
    }
    /**
     * Metoda koja prijavljuje korisnika u aplikaciju
     */
    public void prijaviSe() {
        if (prijavljen == false) {
            Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
            boolean postoji = zadaca2_1WS.provjeraKorisnika(korisnik, lozinka);
            if (postoji) {
                prijavljen = true;
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            login = "Prijava";
            prijavljen = false;
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("");
            } catch (IOException ex) {
                Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        prijavljeniKorisnik();
    }
    /**
     * Metoda koja odjavljuje korisnika iz aplikacije
     */
    public void odjaviKorisnika() {
        prijavljen = false;
        prijavljeniKorisnik();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Metoda koja priprema string za gumb prijave ili odjave
     * @return Prijava/Odjava
     */
    public String prijavljeniKorisnik() {
        if (prijavljen) {
            login = "Odjava";
        } else {
            login = "Prijava";
        }
        return login;
    }
    
    /**
     * Metoda za vracanje korisnika na prosli ekran
     */
    public void VratiNazad(){
        try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    
}
