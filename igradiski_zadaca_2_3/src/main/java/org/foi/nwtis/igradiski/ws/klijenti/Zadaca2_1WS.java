/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.ws.klijenti;

import java.util.List;
import org.foi.nwtis.igradiski.ws.serveri.Aerodrom;
import org.foi.nwtis.igradiski.ws.serveri.AvionLeti;
import org.foi.nwtis.igradiski.ws.serveri.LetPozicija;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.MeteoPodaci;



/**
 * 2.3
 * @author Korisnik
 */
public class Zadaca2_1WS {

    /**
     * Metoda koja poziva WS i vraca aerodrome korisnika
     * @param korisnik korisnik username
     * @param lozinka lozinka korisnika
     * @return  lista objekata aerodrom
     */
    public List<Aerodrom> dajAerodromeKorisnika(String korisnik, String lozinka) {
        List<Aerodrom> aerodromi = null;
        try {
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service =
                    new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            aerodromi = port.mojiAerodromi(korisnik, lozinka);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return aerodromi;
    }
    /**
     * Metoda koja poziva WS za projveru korisnika
     * @param korisnik username
     * @param lozinka lozinka
     * @return  true postoji false ne postopji
     */
    public boolean provjeraKorisnika(String korisnik, String lozinka){
        boolean postoji=false;
        try {
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service =
                    new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            postoji= port.provjeraKorisnika(korisnik, lozinka);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return postoji;
    }
    /**
     * Metoda koja se koristi za dodavanje korisnika i poziva WS metodu
     * @param username korisnicko ime
     * @param lozinka lozinka
     * @param ime ime 
     * @param prezime prezime
     * @param mail email
     * @return  true dodan false nije dodan
     */
    public boolean dodajKorisnika(String username, String lozinka, String ime, String prezime, String mail) {
        boolean dodan = false;
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = 
                new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port(); 
        Korisnik k = new Korisnik(username, ime, prezime, lozinka, mail, null, null);
        org.foi.nwtis.igradiski.ws.serveri.Korisnik noviKorisnik =  
                new org.foi.nwtis.igradiski.ws.serveri.Korisnik();
        noviKorisnik.setKorIme(username);
        noviKorisnik.setIme(ime);
        noviKorisnik.setLozinka(lozinka);
        noviKorisnik.setPrezime(prezime);
        noviKorisnik.setEmailAdresa(mail);
        dodan = port.dodajKorisnika(noviKorisnik, lozinka);
        return dodan;
    }
    
    /**
     * Metoda koja poziva WS za preuzimanje lokacije preko LIQ klijenta
     * @param korisnik username
     * @param lozinka lozinka
     * @param icao icao aerodroma
     * @return  objekt aerodrom
     */
    public Aerodrom preuzmiLokacijuLIQKlijent(String korisnik,String lozinka,String icao){
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service =
                new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
        org.foi.nwtis.igradiski.ws.serveri.Aerodrom aerodrom = 
                port.dajTrazeniAerodromLokacija(korisnik, lozinka, icao);
        return aerodrom;
    }
    /**
     * Metoda koja preuzima meteo podatke za geo lokaciju
     * @param korisnik username
     * @param lozinka lozinka
     * @param sirina geo sirina
     * @param duzina geo duzina
     * @return  objekt meteo podaci
     */
    public org.foi.nwtis.igradiski.ws.serveri.MeteoPodaci preuzmiTemperaturu(String korisnik,String lozinka,String sirina,String duzina){
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = 
                new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
        org.foi.nwtis.igradiski.ws.serveri.MeteoPodaci mp =
                port.preuzmiTemperaturu(korisnik, lozinka, sirina, duzina);
        return mp;
                
    }
    /**
     * Metoda koja poziva WS da dobi poletjele avione s aerodroma
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao icao aerodroma
     * @param od vrijeme od
     * @param doKad vrijeme do
     * @return 
     */
    public List<AvionLeti> poletjeliAvioniAerodrom(String korisnik, String lozinka,String icao, long od, long doKad){
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service =
                new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
        List<AvionLeti> avioni = port.poletjeliAvioniAerodrom(korisnik, lozinka, icao, od, doKad);
        return avioni;
    }
    /**
     * Metoda koja poziva WS da dobi LetPoziciju
     * @param korisnik koriscniko ime
     * @param lozinka lozinka
     * @param icao icao aviona
     * @param vrijeme vrijeme srednje
     * @return objekt let pozicija
     */
    public LetPozicija dajVisinuLokacijuAviona(String korisnik,String lozinka,String icao, long vrijeme){
        LetPozicija lp = new LetPozicija();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = 
                new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
        lp=port.dajVisinuLokacijuAviona(korisnik, lozinka, icao, vrijeme);
        return lp;
    }
    /**
     * Metoda koja sluzi za poziv WS za azuriranje korisnika
     * @param korisnikData objekt korisnik
     * @param username korisnicko ime
     * @param lozinka lozinka
     * @return  true azuriran false nije azuriran
     */
    public boolean AzurirajKorisnika(Korisnik korisnikData, String username, String lozinka) {
        boolean azuriran = false;
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = 
                new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
        org.foi.nwtis.igradiski.ws.serveri.Korisnik korisnik = 
                new org.foi.nwtis.igradiski.ws.serveri.Korisnik();
        korisnik.setEmailAdresa(korisnikData.getEmailAdresa());
        korisnik.setIme(korisnikData.getIme());
        korisnik.setKorIme(korisnikData.getKorIme());
        korisnik.setLozinka(korisnikData.getLozinka());
        korisnik.setPrezime(korisnikData.getPrezime());
        azuriran = port.azurirajKorisnika(korisnik, username, lozinka);
        return azuriran;
    }
    
    
    
    
    
     
    
    
    
}
