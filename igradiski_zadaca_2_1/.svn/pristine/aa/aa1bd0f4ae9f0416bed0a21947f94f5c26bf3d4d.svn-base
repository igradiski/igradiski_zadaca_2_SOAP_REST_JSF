/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.ws.serveri;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import org.foi.nwtis.igradiski.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.igradiski.web.podaci.AirportDAO;
import org.foi.nwtis.igradiski.web.podaci.KorisnikDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.AvionLetiPozicije;
import org.foi.nwtis.rest.podaci.LetPozicija;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author Korisnik
 */
@WebService(serviceName = "Zadaca2")
public class Zadaca2 {

    @Inject
    ServletContext context;

    /**
     * This is a sample web service operation
     *
     * @param noviKorisnik
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dodajKorisnika")
    public boolean dodajKorisnika(@WebParam(name = "noviKorisnik") Korisnik noviKorisnik, @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        String pass = noviKorisnik.getLozinka();
        return korisnikDAO.dodajKorisnika(noviKorisnik, pass, bpk);
    }
    /**
     *metoda koja dohvaca korisnike aerodroma
     * @param korisnik koriscnicko ime
     * @param lozinka lozinka
     * @return  lista korisnika aerdoroma
     */
    @WebMethod(operationName = "korisniciAerodroma")
    public List<Korisnik> korisniciAerodroma(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        List<Korisnik> lista = new ArrayList<>();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            lista = korisnikDAO.dohvatiKorisnikeKojiImajuAerodrome(korisnik, bpk);
        }
        return lista;
    }
    /**
     * Metoda za dohvacanje svih korisnika
     * @param korisnik koriscnicko ime
     * @param lozinka lozinka
     * @return  lista svih korisnika
     *
     */
    @WebMethod(operationName = "sviKorisnici")
    public List<Korisnik> sviKorisnici(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        List<Korisnik> lista = new ArrayList<>();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            lista = korisnikDAO.dohvatiSveKorisnike(korisnik, bpk);
        }
        return lista;
    }
    /**
     * Dohvacanje visine i lokacije aviona
     * @param korisnik koriscnicko ime
     * @param lozinka lozinka
     * @param icao24 icao aerodroma polijetanja
     * @param vrijeme srednje vrijeme
     * @return  vraca objekt let pozicija
     */
    @WebMethod(operationName = "dajVisinuLokacijuAviona")
    public LetPozicija dajVisinuLokacijuAviona(
            @WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao24") String icao24,
            @WebParam(name = "vrijeme") Long vrijeme) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        LetPozicija lp = new LetPozicija();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            String korisnickoImeOS = bpk.getKonfig().dajPostavku("OpenSkyNetwork.korisnik");
            String lozinkaOS = bpk.getKonfig().dajPostavku("OpenSkyNetwork.lozinka");
            OSKlijent oSKlijent = new OSKlijent(korisnickoImeOS, lozinkaOS);
            lp = dohvatiLetPoziciju(oSKlijent, icao24, vrijeme);
        }
        return lp;
    }
    /**
     * Dohvaća let i poziciju sa servera
     * @param oSKlijent os klijent s podacima za dohvacanje
     * @param icao icao aerodroma
     * @param vrijeme srednje vrijeme
     * @return vraća objekt let pozicija
     */
    public LetPozicija dohvatiLetPoziciju(OSKlijent oSKlijent, String icao, Long vrijeme) {
        LetPozicija lp = new LetPozicija();
        AvionLetiPozicije alp = oSKlijent.getTracks(icao, vrijeme);
        if (alp != null) {
            ArrayList<LetPozicija> listaLetPozicija = new ArrayList<>(alp.getPath());
            lp = dohvatiNajveciVisinuLeta(listaLetPozicija);
        }
        return lp;
    }
    /**
     * Dohvacanje najvise visine u letu
     * @param listaLetPozicija lista objekata letPozicija
     * @return  vraća objekt letPozicija koji ima najveću visinu
     */
    public LetPozicija dohvatiNajveciVisinuLeta(ArrayList<LetPozicija> listaLetPozicija) {
        LetPozicija max = new LetPozicija();
        for (LetPozicija l : listaLetPozicija) {
            if (max.getBaro_altitude() < l.getBaro_altitude()) {
                max = l;
            }
        }
        return max;
    }
    
    /**
     * Metoda koja vraća odgovor da li korisnik ima svoj aerodrom
     *  @param korisnik koriscnicko ime
     * @param lozinka lozinka
     * @param icao
     * @return true ima aerodrom false nema aerodrom
     */
    @WebMethod(operationName = "imamAerodrom")
    public boolean imamAerodrom(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        AirportDAO airportDAO = new AirportDAO(bpk);
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            return airportDAO.provjeriPripadanostKolekciji(korisnik, icao);
        } else {
            return false;
        }

    }
    /**
     * Metoda koja vraća lokaciju trazenog aerodroma
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param icao icao aerodroma
     * @return  vraća aerodrom s lokacijom  preko GPS-a
     */
    @WebMethod(operationName = "dajTrazeniAerodromLokacija")
    public Aerodrom dajTrazeniAerodromLokacija(
            @WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String apikey = bpk.getKonfig().dajPostavku("LocationIQ.token");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        AirportDAO airportDAO = new AirportDAO(bpk);
        LIQKlijent lIQKlijent = new LIQKlijent(apikey);
        Aerodrom a = new Aerodrom();
        Aerodrom aerodrom = new Aerodrom();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            a = airportDAO.dajAerodromICAO(icao);
            if (a.getNaziv() != null && !a.getNaziv().isEmpty()) {
                String naziv = a.getNaziv();
                Lokacija l = lIQKlijent.getGeoLocation(naziv);
                aerodrom.setLokacija(l);
            }
        }
        aerodrom.setNaziv(a.getNaziv());
        return aerodrom;
    }
    
    
    
    /**
     * Dohvaca aerodrom preka icao-u
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param icao icao aerodroma koji se trazi
     * @return  vraca trazeni objek aerodrom
     */
    @WebMethod(operationName = "dajTrazeniAerodrom")
    public Aerodrom dajTrazeniAerodrom(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        AirportDAO airportDAO = new AirportDAO(bpk);
        Aerodrom a = new Aerodrom();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            a = airportDAO.dajTrenutniAerodrom(korisnik, icao);
        }
        return a;
    }
    /**
     * Dodaje aerodrom u moje aerodrome
     *
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param icao
     * @return true dodan false nije dodan
     */
    @WebMethod(operationName = "dodajMojAerodrom")
    public boolean dodajMojAerodrom(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        AirportDAO airportDAO = new AirportDAO(bpk);
        boolean dodano = false;
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            dodano = airportDAO.dodajAerodromVlastitim(korisnik, icao);
        }
        return dodano;
    }
    
    /**
     * Dohvaca poletjele avione sa aerodroma
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param icao icao aerodroma
     * @param od vrijeme od
     * @param doKad vrijeme do
     * @return lita poletjelih aviona objekti AvionLeti
     */
    @WebMethod(operationName = "poletjeliAvioniAerodrom")
    public List<AvionLeti> poletjeliAvioniAerodrom(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao,
            @WebParam(name = "od") long od,
            @WebParam(name = "do") long doKad) {
        List<AvionLeti> lista = new ArrayList<AvionLeti>();
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        AirportDAO airportDAO = new AirportDAO(bpk);
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            lista = airportDAO.dohvatiPolijetanja(icao, od, doKad);
        }
        return lista;
    }
    
    /**
     * preuzima temmperaturu  prema geo lokaciji
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param sirina geo sirina
     * @param duzina geo duzina
     * @return vraca objekt MeteoPodaci 
     */
    @WebMethod(operationName = "preuzmiTemperaturu")
    public MeteoPodaci preuzmiTemperaturu(
            @WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "sirina") String sirina,
            @WebParam(name = "duzina") String duzina) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String apikey = bpk.getKonfig().dajPostavku("OpenWeatherMap.apikey");
        OWMKlijent owmk = new OWMKlijent(apikey);
        MeteoPodaci mp = owmk.getRealTimeWeather(sirina, duzina);
        if (mp != null) {
            return mp;
        } else {
            return new MeteoPodaci();
        }
    }

    /**
     * dohvaca aerodrome prema nazivu
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param naziv naziv aerodroma
     * @return lista objekata aerodrom
     */
    @WebMethod(operationName = "dohvatiAerodromeNaziv")
    public List<Aerodrom> dohvatiAerodromeNaziv(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "naziv") String naziv) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDao = new AirportDAO(bpk);
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            List<Aerodrom> lista = airportDao.dajAerodrome(naziv);
            if (lista == null || lista.isEmpty()) {
                return null;
            } else {
                return lista;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Dohvaca aerodrome prema trazenoj drzavi
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param drzava drzava EU,CA,GB
     * @return lista objekata Aerodrom
     */
    @WebMethod(operationName = "dohvatiAerodromeDrzava")
    public List<Aerodrom> dohvatiAerodromeDrzava(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "iso_drzave") String drzava) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDao = new AirportDAO(bpk);
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            List<Aerodrom> lista = airportDao.dajAerodromeDrzava(drzava);
            if (lista == null || lista.isEmpty()) {
                return null;
            } else {
                return lista;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Metoda koja se poziva za azuriranje korinsika
     * @param noviKorisnik objekt korisnik
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @return true azuriran false nije azuriran
     */
    @WebMethod(operationName = "azurirajKorisnika")
    public boolean azurirajKorisnika(@WebParam(name = "azuriraniKorisnik") Korisnik noviKorisnik,
            @WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        boolean azuriran = false;
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
            azuriran = korisnikDAO.azurirajKorisnika(noviKorisnik, korisnik, lozinka, bpk);
        }
        return azuriran;
    }
    
    /**
     * Provjerava postojanje korisnika
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @return true postoji false ne postoji
     */
    @WebMethod(operationName = "provjeraKorisnika")
    public boolean provjeraKorisnika(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        boolean postoji =false;
        postoji=korisnikDAO.provjeraKorisnika(korisnik, lozinka,bpk);
        return postoji;
    }

    /**
     * Metoda za dohvacanje najvece visine letaAviona
     * @param korisnik username korisnika
     * @param lozinka lozinka korisnika
     * @param icao24 icao avioana
     * @param zaVrijeme vrijeme 
     * @return  vraca objekt LetPozicija
     */
    @WebMethod(operationName = "najvecaVisinaLetaAviona")
    public LetPozicija najvecaVisinaLetaAviona(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao24") String icao24,
            @WebParam(name = "zaVrijeme") long zaVrijeme) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String osnKorisnik = bpk.getKonfig().dajPostavku("OpenSkyNetwork.korisnik");
        String osnLozinka = bpk.getKonfig().dajPostavku("OpenSkyNetwork.lozinka");
        OSKlijent osKlijent = new OSKlijent(osnKorisnik, osnLozinka);
        AvionLetiPozicije pozicijeLetaAviona = osKlijent.getTracks(icao24, zaVrijeme);
        LetPozicija letPozicija = new LetPozicija();
        return letPozicija;
    }
    
    
    
    /**
     * Metoda za dohvacanje mojih aerodroma
     *
     * @param noviKorisnik username korisnika
     * @param lozinka lozinka korisnika
     * @return lista objekata Aerodrom
     */
    @WebMethod(operationName = "mojiAerodromi")
    public List<Aerodrom> mojiAerodromi(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDao = new AirportDAO(bpk);
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        List<Aerodrom> aerodromi = new ArrayList<>();
        if (korisnikDAO.provjeraKorisnika(korisnik, lozinka, bpk)) {
                    aerodromi= airportDao.dohvatiAerodromeKorisnika(korisnik);
        }     
        return aerodromi;
    }
    
    /**
     * Test metoda za dohvacanje aerodroma prema nazivu
     * @param noviKorisnik korisnciko ime
     * @param lozinka lozinka
     * @param naziv naziv aerodorma
     * @return  lista objekata aerodrom
     */
    @WebMethod(operationName = "dajAerodromeNaziv")
    public List<Aerodrom> dajAerodromeNAziv(@WebParam(name = "noviKorisnik") String noviKorisnik,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "naziv") String naziv) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");

        // TODO dodati autentifikaciju korinsika SVAGDE
        //TODO preuzeti podatke iz baze podataka
        List<Aerodrom> aerodromi = new ArrayList<>();
        aerodromi.add(new Aerodrom(
                "LDAZ", "Zagreb", "HR", new Lokacija("0.0", "0.0")));
        aerodromi.add(new Aerodrom(
                "LOWW", "Vienna", "AT", new Lokacija("0.0", "0.0")));
        aerodromi.add(new Aerodrom(
                "EDDF", "Frankfurt/M", "AT", new Lokacija("0.0", "0.0")));
        aerodromi.add(new Aerodrom(
                "LQSA", "Sarajevo", "BA", new Lokacija("0.0", "0.0")));
        aerodromi.add(new Aerodrom(
                "OMAA", "aBU DHABI", "AT", new Lokacija("0.0", "0.0")));
        return aerodromi;
    }
    
    
}
