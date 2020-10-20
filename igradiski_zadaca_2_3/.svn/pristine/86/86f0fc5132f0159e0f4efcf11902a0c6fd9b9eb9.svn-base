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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.igradiski.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.OdgovorAerodrom;
import javax.servlet.ServletContext;
import org.foi.nwtis.igradiski.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.igradiski.ws.serveri.AvionLeti;

/**
 *
 * @author Korisnik
 */
@Named(value = "pregledAerodroma")
@SessionScoped
public class PregledAerodroma implements Serializable {

    
    @Inject
    PrijavaKorisnika prijavaKorisnika; 
    
    String korisnik="";
    String lozinka="";
    
    @Inject
    ServletContext context;
    
    @Getter
    List<Aerodrom> aerodromi= new ArrayList<>();
    
    @Getter
    @Setter
    String datumOd="";
    
    @Getter
    @Setter
    String datumDo="";
    
    @Getter
    @Setter
    Date datumDoDate = new Date();
    
    @Getter
    @Setter
    Date datumOdDate=new Date();
    
    @Getter
    @Setter
    Date datumDoDate2;
    
    @Getter
    @Setter
    Date datumOdDate2;
    
    @Getter
    @Setter
    String icao="";
    
    @Getter
    @Setter
    String geoSirina="";
     
    @Getter
    @Setter
    String geoDuzina="";
    
    @Getter
    @Setter
    String geoSirinaServis="";
     
    @Getter
    @Setter
    String geoDuzinaServis="";
    
    @Getter
    @Setter
    String vlaga="";
    
    @Getter
    @Setter
    String temperatura="";
    
    @Getter
    @Setter
    List<AvionLeti> avioniAerodroma = new ArrayList<AvionLeti>();
    
    @Getter
    @Setter
    long UNIXod;
    
    @Getter
    @Setter
    long UNIXdo;
    
    
    /**
     * Creates a new instance of PregledAerodroma
     */
    public PregledAerodroma() {
        
    }
    
    /**
     * metoda za preuzimanje podataka korisnika
     */
    public void preuzmiPodatkeKorisnika(){
        korisnik=prijavaKorisnika.getKorisnik(); 
        lozinka = prijavaKorisnika.getLozinka();
    }
    
    /**
     * Metoda za dohvacanje mojih aerodroma
     * @return 
     */
    public String dohvatiMojeAerodrome() {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromi = Arrays.asList(odgovor.getOdgovor());
        return "";
    }
    /**
     * Funkcija za obradu datuma i promjenu formata 
     */
    public void obradiDatum() {
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        if (datumDo != null && !datumDo.isEmpty() && datumOd != null && !datumOd.isEmpty()) {
            try {
                datumDoDate2 = formatter1.parse(datumDo);
                datumOdDate2 = formatter1.parse(datumOd);
            } catch (ParseException ex) {
                Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * Preuzimanje lokacije iz baze podataka za aerodrom
     */
    public void preuzmiLokacijuIzBaze() {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zadaca2_2RS.dajTrazeniAerodrom(OdgovorAerodrom.class, icao);
        List<Aerodrom> aerodromi2 = Arrays.asList(odgovor.getOdgovor());
        if (aerodromi2.size() > 0) {
            Aerodrom a = aerodromi2.get(0);
            System.out.println("BAZA: " + a.getLokacija().getLatitude() + "Ime: " + a.getNaziv());
            geoSirina = a.getLokacija().getLongitude();
            geoDuzina = a.getLokacija().getLatitude();
        }
    }
    /**
     * preuzimanje geo sirine i duzine preko LIQ klijenta
     */
    public void preuzmiLokacijuLIQKlijent() {
        preuzmiPodatkeKorisnika();
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        org.foi.nwtis.igradiski.ws.serveri.Aerodrom aerodrom = zadaca2_1WS.preuzmiLokacijuLIQKlijent(korisnik, lozinka, icao);
        System.out.println("Klijent: " + aerodrom.getNaziv());
        geoDuzinaServis = aerodrom.getLokacija().getLongitude();
        geoSirinaServis = aerodrom.getLokacija().getLatitude();
    }
    /**
     * Metoda za preuzimanje meteo podataka za geo lokaciju
     * @param sirina geo sirina
     * @param duzina geo duzina
     */
    public void preuzmiMeteoPodatke(String sirina, String duzina) {
        preuzmiPodatkeKorisnika();
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        org.foi.nwtis.igradiski.ws.serveri.MeteoPodaci mp = zadaca2_1WS.preuzmiTemperaturu(korisnik, lozinka, sirina, duzina);
        temperatura = mp.getTemperatureValue().toString();
        vlaga = mp.getHumidityValue().toString();
        temperatura += mp.getTemperatureUnit();
        vlaga += mp.getHumidityUnit();
    }
    /**
     * pruzimanje lokacije za odredeni aerodrom
     * 2 metode 1. za lokaciju baze,a 2. preko servisa i GPS-a
     * 
     * @param icao2 aerodrom za koji se preuzima lokacija
     * @return 
     */
    public String preuzmiLokaciju(String icao2) {
        icao = icao2;
        preuzmiLokacijuIzBaze();
        preuzmiLokacijuLIQKlijent();
        preuzmiMeteoPodatke(geoSirina, geoDuzina);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pregledAerodroma.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    /**
     * Metoda koja dohvaca avione aerodroma prema zadanom vremenu i pretvara timestamp
     * u unix timestamp
     */
    public void DohvatiAvioneAerodroma(){
        System.out.println("DO: "+datumDoDate2.getTime());
        System.out.println("OD: "+datumOdDate2.getTime());
        UNIXod= datumOdDate2.getTime()/1000;
        UNIXdo= datumDoDate2.getTime()/1000;
        preuzmiPodatkeKorisnika();
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        avioniAerodroma = zadaca2_1WS.poletjeliAvioniAerodrom(korisnik, lozinka, icao, UNIXod, UNIXdo);
        System.out.println(avioniAerodroma.size());
    }
    /**
     * Metoda koja se poziva za prikaz letova aerodroma
     * @param icaoAerodrom aerodrom koji zelimo
     * @return 
     */
    public String PrikaziLetove(String icaoAerodrom) {
        icao = icaoAerodrom;
        obradiDatum();
        DohvatiAvioneAerodroma();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pregledAviona.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    
}
