/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.igradiski.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.OdgovorAerodrom;

/**
 *
 * @author Korisnik
 */
@Named(value = "dodavanjeAerodroma")
@SessionScoped
public class DodavanjeAerodroma implements Serializable{
    
    @Inject
    PrijavaKorisnika prijavaKorisnika; 
    String korisnik="";
    String lozinka="";
    
    @Getter
    @Setter
    String naziv;
    
    @Getter
    @Setter
    String drzava;
    
    @Getter
    List<Aerodrom> aerodromi= new ArrayList<>();
    
    @Getter
    List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
    
    @Getter
    List<Aerodrom> aerodromi2= new ArrayList<>();
    
    @Getter
    @Setter
    String icaoOdabrani;
    
    
    

    /**
     * Creates a new instance of DodavanjeAerodroma
     */
    public DodavanjeAerodroma() {
   
    }
    /**
     * Metoda za preuzimanje podataka korisnika
     */
    public void preuzmiPodatkeKorisnika(){
        korisnik=prijavaKorisnika.getKorisnik(); 
        lozinka = prijavaKorisnika.getLozinka();
    }
    /**
     * Dohvacanje aerodroma po nazivu sa servera
     * @return 
     */
    public String dajAerodromaNaziv() {
        System.out.println("Radi");
        if (naziv.length() >= 3) {
            preuzmiPodatkeKorisnika();
            aerodromi = dajAerodrome(korisnik, lozinka, naziv, "");
            Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
            OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
            aerodromiKorisnika = Arrays.asList(odgovor.getOdgovor());
            aerodromi = obradiMojeAerodrome(aerodromi, aerodromiKorisnika);
        }
        return "";
    }
    /**
     * Metoda koja dohvaca aerodrome trazene drzave
     * @return 
     */
    public String dajAerodromaDrzava() {
        if (drzava.length() >= 2) {
            preuzmiPodatkeKorisnika();
            aerodromi = dajAerodrome(korisnik, lozinka, "", drzava);
            Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
            OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
            aerodromiKorisnika = Arrays.asList(odgovor.getOdgovor());
            aerodromi = obradiMojeAerodrome(aerodromi, aerodromiKorisnika);
        }
        return "";
    }
    /**
     * Metoda koja obraduje aerodrome koji nisu u kolekciji moji aerdoromi
     * @param aerodromi dohvaceni aerodromi 
     * @param aerodromiKorisnika aerodromi korisnika
     * @return  lista aerodroma koji nisu u kolekciji korisnikovih
     */
    private List<Aerodrom> obradiMojeAerodrome(List<Aerodrom> aerodromi, List<Aerodrom> aerodromiKorisnika) {
        List<Aerodrom> lista = new ArrayList<Aerodrom>();
        for (Aerodrom ostali : aerodromi) {
            boolean dodaj = true;
            for (Aerodrom moji : aerodromiKorisnika) {
                if (ostali.getIcao().equals(moji.getIcao())) {
                    dodaj = false;
                }
            }
            if (dodaj) {
                lista.add(ostali);
            }
        }
        return lista;
    }
    /**
     * Dohvaca aerodrome 
     * @param korisnik username
     * @param lozinka lozinka
     * @param naziv nazic aerodroma
     * @param drzava drzava aerodroma
     * @return lista objekata aerodrom
     */
    private List<Aerodrom> dajAerodrome(String korisnik, String lozinka, String naziv, String drzava) {
        preuzmiPodatkeKorisnika();
        List<Aerodrom> aerodromiPreuzeti = new ArrayList<>();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodome(OdgovorAerodrom.class, naziv, drzava);
        aerodromiPreuzeti = Arrays.asList(odgovor.getOdgovor());
        return aerodromiPreuzeti;
    }
    /**
     * Test metoda za dovcanaje icao oznake aerodroma
     * @param icaoString icao oznaka
     * @return 
     */
    public String testGumb(String icaoString){
        preuzmiPodatkeKorisnika();
        System.out.println(icaoString);
       return "";
    }
    /**
     * Metoda za brisanje aerodroma korisnika
     * @return 
     */
    public String obrisiAerodromeKorisnika() {
        preuzmiPodatkeKorisnika();
        System.out.println("Brisanje");
        return "";
    }
    /**
     * Metoda za dodavanje aerodroma korisniku
     * @param icao icao aerodroma
     * @return 
     */
    public String dodajAerodromKorisniku(String icao) {
        preuzmiPodatkeKorisnika();
        boolean nema = true;
        icaoOdabrani = icao;
        for (Aerodrom a : aerodromi) {
            if (a.getIcao().compareTo(icaoOdabrani) == 0) {
                for (Aerodrom b : aerodromiKorisnika) {
                    if (a.getIcao().compareTo(b.getIcao()) == 0) {
                        nema = false;
                        break;
                    }
                }
                if (nema) {
                    DodajAvionUBazuPodataka(icao);
                }
                break;
            }
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dodavanjeAerodroma.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    /**
     * Dodavanje aviona u bazu podataka
     * @param icao 
     */
    public void DodajAvionUBazuPodataka(String icao) {
        System.out.println("Dodavanje u bazu krece!");
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        zadaca2_2RS.dodajMojAerodrom(OdgovorAerodrom.class, icao);
    }
    /**
     * Metoda koja odraduje vracanje na prosli ekran
     */
    public void vratiSe() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
