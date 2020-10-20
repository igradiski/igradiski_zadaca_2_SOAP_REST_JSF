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
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.igradiski.ws.klijenti.Zadaca2_1WS;

/**
 *
 * @author Korisnik
 */
@Named(value = "registracijaKorisnika")
@SessionScoped
public class RegistracijaKorisnika implements Serializable {

    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String ime;
    @Getter
    @Setter
    private String prezime;
    @Getter
    @Setter
    private String mail;
    @Getter
    @Setter
    private String lozinka;
    
         
    /**
     * Creates a new instance of RegistracijaKorisnika
     */
    public RegistracijaKorisnika() {
    }
    /*
    Metoda koja registrira korisnika u sustav
    */
    public void RegistrirajKorisnika() {
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        boolean registriran = zadaca2_1WS.dodajKorisnika(username, lozinka, ime, prezime, mail);
        if (registriran) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Nije registriran");
        }
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
