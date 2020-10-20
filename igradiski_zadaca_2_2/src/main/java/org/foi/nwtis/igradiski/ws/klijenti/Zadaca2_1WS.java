/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.ws.klijenti;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.igradiski.ws.serveri.Aerodrom;

/**
 *2.2
 * @author Korisnik
 */
public class Zadaca2_1WS {

    /**
     * poziv za metodu servera koja dohvaca aerodrome korisnika
     * @param korisnik username
     * @param lozinka lozinka
     * @return  lista aerodroma 
     */
    public List<Aerodrom> dajAerodromeKorisnika(String korisnik, String lozinka) {
        List<Aerodrom> aerodromi = new ArrayList<Aerodrom>();
        try {
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            aerodromi = port.mojiAerodromi(korisnik, lozinka);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return aerodromi;
    }
    /**
     * poziv za metodu servera koja dodaje aerodrome korisnika
     * @param korisnik username
     * @param lozinka lozinka
     * @param icao
     * @return true dodano false nije dodano
     */
    public boolean dodajMojAerodrom(String korisnik, String lozinka, String icao) {
        boolean dodan = false;
        try {
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            dodan = port.dodajMojAerodrom(korisnik, lozinka, icao);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return dodan;
    }
    
    
    /**
     * poziv za metodu servera koja dohvaca aerodrome korisnika prema nazivu
     * @param korisnik username
     * @param lozinka lozinka
     * @param naziv naziv aerodroma
     * @return lista aerodroma
     */
    public List<Aerodrom> dajAerodromeNaziv(String korisnik, String lozinka, String naziv) {
        List<Aerodrom> aerodromi = null;
        try {

            org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            aerodromi = port.dohvatiAerodromeNaziv(korisnik, lozinka, naziv);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return aerodromi;

    }
    /**
     * poziv za metodu servera koja dohvaca lokaciju aerodroma
     * @param korisnik username
     * @param lozinka lozinka
     * @param icao icao aerodroma
     * @return  objekt aerodrom
     */
    public Aerodrom dajTrazeniAerodromLokacija(String korisnik, String lozinka, String icao) {
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
        Aerodrom a = port.dajTrazeniAerodromLokacija(korisnik, lozinka, icao);
        return a;
    }
    /**
     * poziv za metodu servera koja dohvaca aerodrome korisnika
     * @param korisnik username
     * @param lozinka lozinka
     * @param icao icao aerodroma
     * @return objekt aerodroma
     */
    public Aerodrom dajTrazeniAerodrom(String korisnik, String lozinka, String icao) {
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
        org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
        Aerodrom a = port.dajTrazeniAerodrom(korisnik, lozinka, icao);
        return a;
    }
    
    /**
     * poziv za metodu servera koja dohvaca aerodrome korisnika prema drzavi
     * @param korisnik username
     * @param lozinka lozinka
     * @param drzava drzava u kojoj se trazi aerodrom
     * @return  lista objekata aerodrom
     */
    public List<Aerodrom> dajAerodromeDrzava(String korisnik, String lozinka, String drzava) {
        List<Aerodrom> aerodromi = null;
        try {
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.igradiski.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.igradiski.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            aerodromi = port.dohvatiAerodromeDrzava(korisnik, lozinka, drzava);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return aerodromi;
    }
}
