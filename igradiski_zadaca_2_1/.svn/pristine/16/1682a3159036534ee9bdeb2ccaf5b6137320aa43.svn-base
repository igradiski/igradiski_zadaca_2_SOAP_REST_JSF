/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.web.dretve;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.igradiski.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.igradiski.web.podaci.AirportDAO;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author Korisnik
 */
public class PreuzimanjeLetovaAvionaAerodroma extends Thread{
    
    private BP_Konfiguracija konf;
    
    //TODO potrebne varijable/atributi
    private OSKlijent klijentOS;
    private String korisnickoImeOS="";
    private String lozinkaOS="";
    private Timestamp datumPreuzimanja;
    private Timestamp doDatum;
    private int trajanjePauze=0;
    private int trajanjeCiklusaDretve=0;
    boolean krajPreuzimanja=false;
    private String bpkorisnik="";
    private String bplozinka="";
    private String url="";

    public PreuzimanjeLetovaAvionaAerodroma(BP_Konfiguracija konf) {
        this.konf = konf;
    }
    
    
    @Override
    public void interrupt() {
        super.interrupt();
    }
    
   /**
    * Funkcija zaduzena za rad dretve u intervalima 
    * i izvrsava funkcije dodavanja aviona i aerodroma
    */
    
    @Override
    public void run() {
        int brojac = 0;
        IzracunajDatumDo();
        while (!krajPreuzimanja) {
            System.out.println("Brojac: " + brojac);
            try {

                long pocetakDretve = System.currentTimeMillis();
                SveFunkcijeDretve();
                long trajanjeOperacija = System.currentTimeMillis() - pocetakDretve;
                long interval = trajanjeCiklusaDretve * 1000;
                Thread.sleep(interval - trajanjeOperacija);
                PovecajDanDretve();
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    /**
     * Funkcija koja pokreže dretvu i vrši preuzimanje konfiguracije i datuma
     */
    @Override
    public synchronized void start() {
        PreuzmiKonfiguraciju();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
        IzracunajDatumDo();
        PreuzmiPodatkeZaBazu();
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Funkcija koja povećava dan do kojeg se vrsi pretrazivanje, a izvrsi se na
     * kraju ciklusa dretve
     */
    public void PovecajDanDretve() {
        int dan = 86400000;
        Timestamp noviDatum = new Timestamp(datumPreuzimanja.getTime() + dan);
        datumPreuzimanja = noviDatum;
        if (ProvjeriNoviDatum(datumPreuzimanja)) {
            try {
                Thread.sleep(86400000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        doDatum = new Timestamp(datumPreuzimanja.getTime() + dan);

    }
    /**
     * Funckija za provjeru novog datuma da li treba zaustaviti dretu
     *
     * @param datum- datum koji se provjerava
     * @return vraca da li je kraj (DA NE)
     */
    private boolean ProvjeriNoviDatum(Timestamp datum) {
        boolean doKraja = false;
        String datumKraj = konf.getKonfig().dajPostavku("preuzimanje.kraj");
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date datumProvjere = new Date(datum.getTime());
        try {
            Date date = formatter.parse(datumKraj);
            if (date.equals(datumProvjere)) {
                doKraja = true;
            }
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doKraja;
    }
    /**
     *
     * funkcija koja preuzima podatke za spajanje na bazu podataka
     *
     */
    private void PreuzmiPodatkeZaBazu() {
        bpkorisnik = konf.getUserUsername();
        bplozinka = konf.getUserPassword();
        url = konf.getServerDatabase() + konf.getUserDatabase();
    }
    /**
     * Funkcija koja vrsi preuzimanje konfiguracijskih parametara iz datoteke i
     * kreira OSKlijenta
     */
    public void PreuzmiKonfiguraciju() {
        korisnickoImeOS = konf.getKonfig().dajPostavku("OpenSkyNetwork.korisnik");
        lozinkaOS = konf.getKonfig().dajPostavku("OpenSkyNetwork.lozinka");
        trajanjePauze = Integer.parseInt(konf.getKonfig().dajPostavku("preuzimanje.pauza"));
        trajanjeCiklusaDretve = Integer.parseInt(konf.getKonfig().dajPostavku("preuzimanje.ciklus"));
        String datumPreuzimanjaStr = konf.getKonfig().dajPostavku("preuzimanje.pocetak");
        ObradiDatumPreuzimanja(datumPreuzimanjaStr);
        if (ProvjeriAtribute()) {
            KreirajOSKlijenta();
        }
    }

    /**
     *
     * Provjera atributa za OS klijenta prije kreiranja
     *
     * @return true tocni atribu false ne tocni atributi
     */
    public boolean ProvjeriAtribute() {
        boolean atributiTocni = true;
        if (korisnickoImeOS.isEmpty() || korisnickoImeOS == null) {
            atributiTocni = false;
        }
        if (lozinkaOS.isEmpty() || lozinkaOS == null) {
            atributiTocni = false;
        }
        return atributiTocni;
    }
    
    
    /**
     * Metoda za kreiranje os klijenta s preuzetim podacima
     */
    public void KreirajOSKlijenta() {
        //klijentOS = new OSKlijent(korisnickoImeOS, lozinkaOS);
        klijentOS = new OSKlijent(korisnickoImeOS, lozinkaOS);
    }

    /**
     * Funkcija koja mijenja format datuma u željeni radi daljnje obrade
     *
     * @param datumPreuzimanjaStr datum koji se mijenja
     */
    private void ObradiDatumPreuzimanja(String datumPreuzimanjaStr) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = formatter.parse(datumPreuzimanjaStr);
            Timestamp timeStampDate = new Timestamp(date.getTime());
            System.out.println(timeStampDate.toString());
            datumPreuzimanja = timeStampDate;
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }

    }
    /**
     * Funkcija koja izracunava datum do kojeg se vrsi preuzimanje
     */
    private void IzracunajDatumDo() {
        int dan = 86400000;
        Timestamp danDo = new Timestamp(datumPreuzimanja.getTime() + dan);
        doDatum = danDo;
    }
    
    /**
     * Funkcija kojom se ukljucuju razne funkcije dretve
     */
    private void SveFunkcijeDretve() {
        //preuzmi aerodrome koji su u my airports 
        DohvatiPodatkeAerodroma();
    }
    
    /**
     * Funckija koja preuzima aerodrome , zapisuje njihove podatke i preuzima
     * avione za dani aerodrom nakon toga spava odredeni interval
     */
    private void DohvatiPodatkeAerodroma() {
        ArrayList<Airport> aerodromi = PreuzmiAerodromeKorisnika();
        boolean aerodromPostoji = false;
        for (Airport airport : aerodromi) {
            AirportDAO airportDao = new AirportDAO(konf);
            Date datum = new Date(datumPreuzimanja.getTime());
            System.out.println("Provjerava aerodrom!");
            aerodromPostoji = airportDao.ProvjeriPostojanjeAerodromaUBazi(airport, datum);
            if (aerodromPostoji == false) {
                System.out.println("Aerodrom ne postoji,slijedi preuzimanje!");
                PreuzmiPodatkeZaAerodrom(airport);
                UpisiDaJeAerodromPreuzet(airport, datumPreuzimanja);
                try {
                    System.out.println("spava za aerodrom");
                    Thread.sleep(trajanjePauze);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Vec je upisan aerodrom!");
            }
        }
    }
    
    /**
     * Funkcija koja upisuje u bazu da je aerodrom preuzet
     *
     * @param aerodrom aerodrom koji se preuzima
     * @param datum datum kada je preuzet aerodrom
     */
    private void UpisiDaJeAerodromPreuzet(Airport aerodrom, Timestamp datum) {
        AirportDAO airportDao = new AirportDAO(konf);
        airportDao.UpisiAerodromULog(aerodrom, datum);
    }
    
    /**
     * Preuzima avione za objekt airport
     *
     * @param airport aerodrom koji za koji zelimo avione
     */
    private void PreuzmiPodatkeZaAerodrom(Airport airport) {
        List<AvionLeti> listaAviona = klijentOS.getDepartures(airport.getIdent(), datumPreuzimanja, doDatum);
        AirportDAO airportDao = new AirportDAO(konf);
        if (listaAviona != null) {
            for (AvionLeti avion : listaAviona) {
                airportDao.DodajAvionUBazu(avion);
            }
        } else {
            System.out.println("Lista aviona prazna!");
        }
    }

    /**
     * Preuzimanje aerodroma u listu, a aerodromi se dohvacaju preko liste
     * idenata
     *
     * @return vraca listu aerodroma koje korisnik prati
     */
    private ArrayList<Airport> PreuzmiAerodromeKorisnika() {
        AirportDAO airportDao = new AirportDAO(konf);
        ArrayList<String> aerodromiIdent = new ArrayList<String>();
        aerodromiIdent = airportDao.dajSveIdenteAerodromeKorisnika(konf);
        ArrayList<Airport> aerodromi = airportDao.dajAerodromePrekoIdenta(aerodromiIdent);
        return aerodromi;
    }
}
