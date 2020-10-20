
package org.foi.nwtis.igradiski.web.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.igradiski.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;


public class AirportDAO {

    String url="";
    String bpkorisnik = "";
    String bplozinka = "";
    BP_Konfiguracija bpk;
    
    /**
     * Kreiranje objekta klase Airport dao i preuzimanje parametara za bazu
     * podataka
     *
     * @param bpkk
     */
    public AirportDAO(BP_Konfiguracija bpkk) {
        this.url = bpkk.getServerDatabase() + bpkk.getUserDatabase();
        this.bpkorisnik = bpkk.getUserUsername();
        this.bplozinka = bpkk.getUserPassword();
        this.bpk = bpkk;
    }
    
    /**
     * Metoda koja iz stringa koordinata stvara objekt lokacija
     *
     * @param koordinate koordinate u stringu (sirina;duzina)
     * @return
     */
    public Lokacija izracunajLokaciju(String koordinate) {
        String[] parts = koordinate.split(",");
        String prva = parts[0];
        String druga = parts[1];
        Lokacija l = new Lokacija(prva, druga);
        return l;
    }
    
    /**
     * metoda koja dohvaca aerodrome prema identu i prema nazivu
     *
     * @param naziv naziv aerodroma
     * @return
     */
    public List<Aerodrom> dajAerodrome(String naziv) {
        List<Airport> listaPomocna = dajAerodromePoNazivu(naziv);
        List<Aerodrom> lista = new ArrayList<Aerodrom>();
        for (Airport airport : listaPomocna) {
            Lokacija l = izracunajLokaciju(airport.getCoordinates());
            Aerodrom a = new Aerodrom(airport.getIdent(), airport.getName(), airport.getIso_country(), l);
            lista.add(a);
        }
        return lista;
    }

    /**
     * Dohvaca aerodrome u listu prema nazivu(KRATICI) drzave
     *
     * @param naziv kratica drzave DE,CA,HR
     * @return
     */
    public List<Aerodrom> dajAerodromeDrzava(String naziv) {
        List<Airport> listaPomocna = dajAerodromePoNazivuDrzave(naziv);
        List<Aerodrom> lista = new ArrayList<Aerodrom>();
        for (Airport airport : listaPomocna) {
            Lokacija l = izracunajLokaciju(airport.getCoordinates());
            Aerodrom a = new Aerodrom(airport.getIdent(), airport.getName(), airport.getIso_country(), l);
            lista.add(a);
        }
        return lista;
    }
    
    /**
     * Dodaje aerodrom u vlastite aerodrome korisnika
     *
     * @param korisnik korisnicko ime kome dodajemo
     * @param icao icao aerodroma koji dodajemo
     * @return dodan/true nije dodan/false
     */
    public boolean dodajAerodromVlastitim(String korisnik, String icao) {
        boolean dodano = false;
        String upit = "SELECT * FROM AIRPORTS WHERE IDENT= '" + icao + "'";
        Airport airport = DohvatiAerodrom(upit);
        if (airport == null) {
            dodano = false;
        } else {
            Timestamp spremljeno = new Timestamp(System.currentTimeMillis());
            String upitDodavanje = "INSERT INTO MYAIRPORTS VALUES(DEFAULT,'" + icao + "','" + korisnik + "','" + spremljeno + "')";
            dodano = IzvrsiDodavanjeAerodroma(upitDodavanje);
        }
        return dodano;
    }
    
    /**
     * DOhvaca polijetanja za odredeni interval
     *
     * @param icao icao aerodroma
     * @param od UNIX timestamp od kad (sekunde)
     * @param doKad UNIX timestamp do kad (sekunde)
     * @return
     */
    public List<AvionLeti> dohvatiPolijetanja(String icao, long od, long doKad) {
        List<AvionLeti> lista = new ArrayList<AvionLeti>();
        List<AvionLeti> listaAvionaAerodroma = new ArrayList<AvionLeti>();
        listaAvionaAerodroma = dohvatiAvioneAerodroma(icao, od, doKad);
        return listaAvionaAerodroma;
    }
    
    /**
     * Dohvaca avione koji su poletjeli s aerodroma
     *
     * @param icao icao aerodroma
     * @param od vrijeme od
     * @param doKad vrijeme do
     * @return
     */
    public List<AvionLeti> dohvatiAvioneAerodroma(String icao, long od, long doKad) {
        List<AvionLeti> lista = new ArrayList<AvionLeti>();
        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT ='" + icao + "'";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    AvionLeti a = obradiPodatkeAviona(rs);
                    if (a != null) {
                        lista.add(a);
                    }
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return AvioniUIntervalue(lista, od, doKad);
    }

    /**
     * Filtiranje aviona koji u poletjeli u odredenom intervalu
     *
     * @param lista avioni za filtirranje
     * @param od vrieme od
     * @param doKad vrijeme do
     * @return
     */
    public List<AvionLeti> AvioniUIntervalue(List<AvionLeti> lista, long od, long doKad) {
        List<AvionLeti> listaReturn = new ArrayList<AvionLeti>();
        for (AvionLeti a : lista) {
            if (a.getFirstSeen() >= od && a.getFirstSeen() <= doKad) {
                listaReturn.add(a);
            }
        }
        return listaReturn;
    }
    
    
    /**
     * Sprema podatke u objekt avion leti iz podataka koje je upit dohvatio
     *
     * @param rs podaci dohvaceni upitom
     * @return objekt avion leti
     */
    public AvionLeti obradiPodatkeAviona(ResultSet rs) {
        AvionLeti a = null;
        try {
            String icao24 = rs.getString("ICAO24");
            int firstSeen = Integer.parseInt(rs.getString("FIRSTSEEN"));
            String estDepartureAirport = rs.getString("ESTDEPARTUREAIRPORT");
            int lastSeen = Integer.parseInt(rs.getString("LASTSEEN"));
            String estArrivalAirport = rs.getString("estArrivalAirport");
            String callsign = rs.getString("callsign");
            int estDepartureAirportHorizDistance = Integer.parseInt(rs.getString("estDepartureAirportHorizDistance"));
            int estDepartureAirportVertDistance = Integer.parseInt(rs.getString("estDepartureAirportVertDistance"));
            int estArrivalAirportHorizDistance = Integer.parseInt(rs.getString("estArrivalAirportHorizDistance"));
            int estArrivalAirportVertDistance = Integer.parseInt(rs.getString("estArrivalAirportVertDistance"));
            int departureAirportCandidatesCount = Integer.parseInt(rs.getString("departureAirportCandidatesCount"));
            int arrivalAirportCandidatesCount = Integer.parseInt(rs.getString("arrivalAirportCandidatesCount"));
            a = new AvionLeti(icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport,
                    callsign, estDepartureAirportHorizDistance,
                    estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,
                    departureAirportCandidatesCount, arrivalAirportCandidatesCount);
        } catch (SQLException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }
    
    /**
     * Metoda za unos aerodroma u bazu
     *
     * @param upitDodavanje INsert upit
     * @return true-dodano false-nije dodano
     */
    public boolean IzvrsiDodavanjeAerodroma(String upitDodavanje) {
        boolean dodano = false;
        try {
            Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
            Statement s = con.createStatement();
            s.executeUpdate(upitDodavanje);
            dodano = true;
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dodano;
    }
    
    /**
     * Metoda koja iz objekta airport kreira objekt aerodrom
     *
     * @param a objekt airport
     * @return objekt aerodrom
     */
    public Aerodrom promijeniAirportUAerodrom(Airport a) {
        Aerodrom aerodrom = new Aerodrom();
        aerodrom.setIcao(a.getIdent());
        aerodrom.setDrzava(a.getIso_country());
        aerodrom.setNaziv(a.getName());
        Lokacija l = izracunajLokaciju(a.getCoordinates());
        aerodrom.setLokacija(l);
        return aerodrom;
    }
    
    /**
     * Metoda za dohvacanje aerodroma prema icao oznaci
     *
     * @param icao oznaka aerodroma
     * @return aerodrom objekt
     */
    public Aerodrom dajAerodromICAO(String icao) {
        Aerodrom aerodrom = new Aerodrom();
        String upit = "SELECT * FROM AIRPORTS WHERE IDENT='" + icao + "'";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    Airport a = ObradiPodatkeAerodroma(rs);
                    aerodrom = promijeniAirportUAerodrom(a);
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aerodrom;
    }

    /**
     * Metoda koja dohvaca trenutni aerodrom korisnika
     *
     * @param username korisnicko ime
     * @param icao icao aerodroma
     * @return objekt aerodrom
     */
    public Aerodrom dajTrenutniAerodrom(String username, String icao) {
        Aerodrom a = new Aerodrom();
        String upit = "SELECT * FROM MYAIRPORTS WHERE USERNAME='" + username + "' AND IDENT='" + icao + "'";
        boolean postoji = dohvatiAerodromIzBaze(upit);
        if (postoji) {
            String upitDrugi = "SELECT * FROM AIRPORTS WHERE IDENT='" + icao + "'";
            Airport airport = DohvatiAerodrom(upitDrugi);
            Lokacija l = izracunajLokaciju(airport.getCoordinates());
            a = new Aerodrom(airport.getIdent(), airport.getName(), airport.getIso_country(), l);
        }
        return a;
    }
    
    /**
     * Metoda koja dohvaca aerodrom preko upita
     *
     * @param upit upit za dohvacanje aerodroma
     * @return true dodano false nije dodano
     */
    public boolean dohvatiAerodromIzBaze(String upit) {
        boolean postoji = false;
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    postoji = true;
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return postoji;
    }
    
    /**
     * Provjerava da li aerodrom pripada kolekciji mojih aerodroma korisnika
     *
     * @param korisnik username od korisnika
     * @param icao za aerodrom koji se provjerava
     * @return true postoji false ne postoji
     */
    public boolean provjeriPripadanostKolekciji(String korisnik, String icao) {
        String upit = "SELECT * FROM MYAIRPORTS WHERE USERNAME='" + korisnik + "' AND IDENT='" + icao + "'";
        int brojac = 0;
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    brojac++;
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (brojac > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * dohvaca aerodrome prema nazivu drzave iz baze
     *
     * @param naziv naziv drzave
     * @return lista aerodroma za drzavu
     */
    public List<Airport> dajAerodromePoNazivuDrzave(String naziv) {
        ArrayList<Airport> aerodromi = new ArrayList<>();
        String upit = "SELECT * FROM AIRPORTS WHERE ISO_COUNTRY='" + naziv + "'";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    aerodromi.add(ObradiPodatkeAerodroma(rs));
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aerodromi;
    }
    
        /**
     * Dohvaca aerodrome prema nazivu LIKE
     *
     * @param naziv naziv aerodroma
     * @return lista aerodroma dohvacenih prema nazivu
     */
    public List<Airport> dajAerodromePoNazivu(String naziv) {
        ArrayList<Airport> aerodromi = new ArrayList<>();
        String upit = "SELECT * FROM AIRPORTS WHERE NAME LIKE '" + naziv + "'";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    aerodromi.add(ObradiPodatkeAerodroma(rs));
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aerodromi;
    }

    /**
     * Metoda vraca idente korisnika koji imaju aerodrome u omiljenima
     *
     * @param bpk konfiguracija za pristup bazi podataka
     * @return lista korisnickih imena korisnika
     */
    public ArrayList<String> dajSveIdenteAerodromeKorisnika(BP_Konfiguracija bpk) {
        ArrayList<String> identiAerodroma = new ArrayList<>();
        String upit = "SELECT DISTINCT IDENT FROM MYAIRPORTS";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    String ident = rs.getString("ident");
                    identiAerodroma.add(ident);
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return identiAerodroma;
    }
     
     /**
     * Metoda za formatiranje datuma u drugi format
     *
     * @param datumFormat datum za formatiranje
     * @return string datum drugog formata yyyy-MM-dd
     */
    public String FormatirajDatum(Date datumFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(datumFormat);
        return date;
    }
    
     /**
     * Metoda provjerava postojanje aerodroma u bazi podataka tablica my
     * airports
     *
     * @param aerodromProvjera objekt aerodrom koji se provjerava
     * @param datumZaPromjenu datum za koji se provjerava
     * @return
     */
    public boolean ProvjeriPostojanjeAerodromaUBazi(Airport aerodromProvjera, Date datumZaPromjenu) {
        boolean postoji = false;
        String datum = FormatirajDatum(datumZaPromjenu);
        String upit = "SELECT * FROM MYAIRPORTSLOG WHERE IDENT= " + "'" + aerodromProvjera.getIdent() + "'"
                + " AND FLIGHTDATE=" + "'" + datum + "'";
        postoji = DohvatiLogAerodroma(upit);
        return postoji;
    }
    
     
     /**
     * Dohvaća postojanje aerodroma u bazi my airports log
     *
     * @param upit upit za dohvacanje
     * @return postoji true ne postoji false
     */
    public boolean DohvatiLogAerodroma(String upit) {
        boolean postoji = false;
        int brojac = 0;
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    brojac++;
                }
                if (brojac > 0) {
                    return true;
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return postoji;
    }
    
     /**
     * dohvacanje liste aerodroma preko identa
     *
     * @param identiAerodroma string lista idenata aerodroma
     * @return vraća listu airport(aerodroma)
     */
    public ArrayList<Airport> dajAerodromePrekoIdenta(ArrayList<String> identiAerodroma) {
        ArrayList<Airport> listaAerodroma = new ArrayList<>();
        for (String ident : identiAerodroma) {
            String upit = "SELECT ident, type, name,elevation_ft,continent,"
                    + "iso_country,iso_region,municipality,gps_code,iata_code,"
                    + "local_code,coordinates  "
                    + "FROM airports WHERE ident =" + "'" + ident + "'";
            listaAerodroma.add(DohvatiAerodrom(upit));
        }
        return listaAerodroma;
    }
    
     /**
     * Metoda za insert aviona u bazu koja provjerava da li ima odrediste
     *
     * @param avionLeti objekt avion leti
     */
    public void DodajAvionUBazu(AvionLeti avionLeti) {
        if (avionLeti != null) {
            if (avionLeti.getEstArrivalAirport() != null) {
                if (!avionLeti.getEstArrivalAirport().isEmpty()) {
                    String upit = KreirajUpitZaDodavanjeAviona(avionLeti);
                    IzvrsiInsertOrUpdate(upit);
                }
            }
        }
    }

     /**
     * Kreiranje upita za unos aerodroma u log
     *
     * @param aerodrom objekt aerodroma
     * @param datum datum kada se upisuje
     */
    public void UpisiAerodromULog(Airport aerodrom, Timestamp datum) {
        Date date = new Date(datum.getTime());
        String datumSad = FormatirajDatum(date);
        Timestamp trenutnoVrijeme = new Timestamp(System.currentTimeMillis());
        String upit = "INSERT INTO MYAIRPORTSLOG VALUES ('" + aerodrom.getIdent() + "','" + datumSad + "','" + trenutnoVrijeme + "')";
        IzvrsiInsertOrUpdate(upit);
    }
    
    /**
     * Metoda za izvrsavanje insert i update upita
     *
     * @param upit insert ili update upit
     */
    public void IzvrsiInsertOrUpdate(String upit) {
        try {
            Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
            Statement s = con.createStatement();
            s.executeUpdate(upit);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * Metoda koja se koristi da kreira upit za dodavanje aviona u bazu
     *
     * @param avionLeti objekt avion leti
     * @return upit koji sluzi za dodavanje aviona u bazu
     */
    public String KreirajUpitZaDodavanjeAviona(AvionLeti avionLeti) {
        Timestamp vrijemeSpremanja = new Timestamp(System.currentTimeMillis());
        String icao24 = avionLeti.getIcao24();
        int firstSeen = avionLeti.getFirstSeen();
        String estDepartureAirport = avionLeti.getEstDepartureAirport();
        int lastSeen = avionLeti.getLastSeen();
        String estArrivalAirport = avionLeti.getEstArrivalAirport();
        String callsign = avionLeti.getCallsign();
        int estDepartureAirportHorizDistance = avionLeti.getEstDepartureAirportHorizDistance();
        int estDepartureAirportVertDistance = avionLeti.getEstDepartureAirportVertDistance();
        int estArrivalAirportHorizDistance = avionLeti.getEstArrivalAirportHorizDistance();
        int estArrivalAirportVertDistance = avionLeti.getEstArrivalAirportVertDistance();
        int departureAirportCandidatesCount = avionLeti.getDepartureAirportCandidatesCount();
        int arrivalAirportCandidatesCount = avionLeti.getArrivalAirportCandidatesCount();
        String upit = "INSERT INTO AIRPLANES VALUES (" + "DEFAULT," + "'" + icao24 + "'," + firstSeen + "," + "'" + estDepartureAirport + "'"
                + "," + lastSeen + "," + "'" + estArrivalAirport + "'," + "'" + callsign + "'," + estDepartureAirportHorizDistance
                + "," + estDepartureAirportVertDistance + "," + estArrivalAirportHorizDistance + ","
                + estArrivalAirportVertDistance + "," + departureAirportCandidatesCount + "," + arrivalAirportCandidatesCount + ",'" + vrijemeSpremanja + "')";
        return upit;
    }
     
     
    /**
     * Dohvaća aerodrom iz baze preko upita
     *
     * @param upit upit koji sluzi za dohvacanje aerodroma
     * @return vraća objekt airport
     */
    public Airport DohvatiAerodrom(String upit) {
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    return ObradiPodatkeAerodroma(rs);
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     
     /**
     * Meetoda za obradu podataka aerodroma preko rezultata iz upita
     *
     * @param rs dobiveni rezultat upita
     * @return vraća popunjen objekt airport
     * @throws SQLException
     */
    public Airport ObradiPodatkeAerodroma(ResultSet rs) throws SQLException {
        String ident = rs.getString("ident");
        String type = rs.getString("type");
        String name = rs.getString("name");
        String elevation_ft = rs.getString("elevation_ft");
        String continent = rs.getString("continent");
        String iso_country = rs.getString("iso_country");
        String iso_region = rs.getString("iso_region");
        String municipality = rs.getString("municipality");
        String gps_code = rs.getString("gps_code");
        String iata_code = rs.getString("iata_code");
        String local_code = rs.getString("local_code");
        String coordinates = rs.getString("coordinates");
        Airport a = new Airport(ident, type, name, elevation_ft,
                continent, iso_country, iso_region, municipality,
                gps_code, iata_code, local_code, coordinates);
        return a;
    }
    
     /**
     * dohvaca aerodrome preko usernamea korisnika
     *
     * @param korisnik username korisnika
     * @return lista aerodroma
     */
    public List<Aerodrom> dohvatiAerodromeKorisnika(String korisnik) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        ArrayList<Airport> pomocna = new ArrayList<>();
        ArrayList<String> imenaAerodroma = dohvatiImenaAerodromaIzMyAirports(korisnik);
        pomocna = dajAerodromePrekoIdenta(imenaAerodroma);
        for (Airport airport : pomocna) {
            Lokacija l = izracunajLokaciju(airport.getCoordinates());
            Aerodrom a = new Aerodrom(airport.getIdent(), airport.getName(), airport.getIso_country(), l);
            aerodromi.add(a);
        }
        return aerodromi;
    }
     
     /**
     * Dohvaca imena aerodroma iz tablice my airports za danog korisnika
     *
     * @param korisnik username korisnika
     * @return imena mojih aerodroma u listi stringova
     */
    public ArrayList<String> dohvatiImenaAerodromaIzMyAirports(String korisnik) {
        String upit = "SELECT * FROM MYAIRPORTS WHERE USERNAME='" + korisnik + "'";
        ArrayList<String> imenaAerodroma = new ArrayList<>();
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    imenaAerodroma.add(rs.getString("IDENT"));
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imenaAerodroma;
    }
    
}
