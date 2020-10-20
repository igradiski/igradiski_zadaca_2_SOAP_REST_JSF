/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.igradiski.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.igradiski.ws.serveri.AvionLeti;
import org.foi.nwtis.igradiski.ws.serveri.LetPozicija;


/**
 *
 * @author Korisnik
 */
@Named(value = "pregledAviona")
@SessionScoped
public class pregledAviona implements Serializable{

    
    @Inject
    PregledAerodroma pregledAerodroma; 
 
    @Inject
    PrijavaKorisnika prijavaKorisnika; 
    
    @Getter
    @Setter
    String korisnik="";
    
    @Getter
    @Setter
    String lozinka="";
    
    @Getter
    @Setter
    String icaoAerodrom="";
    
    @Getter
    @Setter
    List<AvionLeti> avioni= new ArrayList<>();
    
    @Getter
    @Setter
    String srednjeVrijeme="";
    
    @Getter
    @Setter
    String srednjeVrijemeIzracun="";
    
    
    @Getter
    @Setter
    float sirina;
    
    @Getter
    @Setter
    float duzina;
    
    @Getter
    @Setter
    float visina;
    
    
    /**
     * Creates a new instance of pregledAviona
     */
    public pregledAviona() {
    }
    /**
     * Metoda za preuzimanje podataka o korisniku
     */
    public void preuzmiPodatkeKorisnika() {
        this.korisnik = prijavaKorisnika.getKorisnik();
        this.lozinka = prijavaKorisnika.getLozinka();
    }
    /**
     * Metoda za dohvacanje aviona i prikaz
     */
    public void dohvatiAvione() {
        avioni = pregledAerodroma.getAvioniAerodroma();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pregledAviona.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Metoda za povratak na proslu stranicu
     * @return 
     */
    public String vratiNazad() {
        avioni.clear();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pregledAerodroma.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    /**
     * Metoda za preracunavanje u unix format vremena
     * @param broj unix sekunde
     * @return datum preracunat iz unixa
     */
    public String preracunajUnixUDatum(long broj) {
        long unixSeconds = broj;
        Date date = new java.util.Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    /**
     * Metoda za izracunavanje srednjeg vremena leta
     * @param vrijemeOD pocetno vrijeme
     * @param vrijemeDO krajnje vrijeme
     * @return srednje vrijeme
     */
    public long izracunajSrednjeVrijeme(long vrijemeOD, long vrijemeDO) {
        long srednje = (vrijemeOD + vrijemeDO) / 2;
        return srednje;
    }
    /**
     * Metoda za dohvacanje visine leta i njezinih koordinata
     * @param icao24 icao aviona
     * @param vrijemeOD vrijeme leta od 
     * @param vrijemeDO vrijeme leta do
     * @return 
     */
    public String dohvatiLetVisinu(String icao24, long vrijemeOD, long vrijemeDO) {
        preuzmiPodatkeKorisnika();
        long srednjeVrijeme2 = izracunajSrednjeVrijeme(vrijemeOD, vrijemeDO);
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        LetPozicija lp = zadaca2_1WS.dajVisinuLokacijuAviona(korisnik, lozinka, icao24, srednjeVrijeme2);
        visina = lp.getBaroAltitude();
        sirina = lp.getLatitude();
        duzina = lp.getLongitude();
        srednjeVrijeme = preracunajUnixUDatum(lp.getTime());
        srednjeVrijemeIzracun = preracunajUnixUDatum(srednjeVrijeme2);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pregledAviona.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
