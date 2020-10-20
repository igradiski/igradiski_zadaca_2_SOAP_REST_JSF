/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.web.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.igradiski.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Korisnik;


public class KorisnikDAO {
    
    /**
     * Metoda za dohvacanje korisnika iz baze podatak
     * @param korisnik korisnicko ime
     * @param lozinka lozinka 
     * @param prijava priavljen korisni true false
     * @param bpk konfiguracija baze podataka
     * @return  objekt korisnik
     */
    public Korisnik dohvatiKorisnika(String korisnik, String lozinka, Boolean prijava, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA FROM korisnici WHERE "
                + "KOR_IME = '" + korisnik + "'";
        if (prijava) {
            upit += " and LOZINKA = '" + lozinka + "'";
        }
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    return obradiPodatkeKorisnika(rs);
                }
            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Metoda obraduje podatka vracene iz upita i stvara objekt korinsika
     * @param rs result set red upita
     * @return  objekt korisnika dobivenog iz upita
     */
    public Korisnik obradiPodatkeKorisnika(ResultSet rs) {
        String korisnik1;
        try {
            korisnik1 = rs.getString("kor_ime");
            String ime = rs.getString("ime");
            String prezime = rs.getString("prezime");
            String email = rs.getString("email_adresa");
            Timestamp kreiran = rs.getTimestamp("datum_kreiranja");
            Timestamp promjena = rs.getTimestamp("datum_promjene");
            Korisnik k = new Korisnik(korisnik1, ime, prezime, "******", email, kreiran, promjena);
            return k;
        } catch (SQLException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Dohvaca korisnike koji imaju vlastiti aerodromu bazi podataka
     * @param username korisncko ime korisnika
     * @param bpk konfiguracija baze podataka
     * @return  vraca listu korisnika
     */
    public List<Korisnik> dohvatiKorisnikeKojiImajuAerodrome(String username, BP_Konfiguracija bpk) {
        List<Korisnik> lista = new ArrayList<>();
        String upit = "SELECT DISTINCT USERNAME FROM MYAIRPORTS";
        List<String> imenaKorisnika = dohvatiImenaKorisnika(upit, bpk);
        for (String ime : imenaKorisnika) {
            lista.add(DohvatiKorisnika(ime, bpk));
        }
        return lista;
    }
    
    /**
     * Dohvaca sve korisnike iz baze podataka
     * @param username username korisnika koji je pozvao metodu
     * @param bpk konfiguracija baze podataka
     * @return lista objekata korisnika
     */
    public List<Korisnik> dohvatiSveKorisnike(String username, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        List<Korisnik> lista = new ArrayList<Korisnik>();
        String upit = "SELECT * FROM KORISNICI";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    lista.add(kreirajKorisnika(rs));
                }

                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    /**
     * Dohvaca korisnika iz baze podataka prema korisnickom imenu
     * @param ime korisnicko ime za dohvat
     * @param bpk konf baze podataka
     * @return objekt korisnika koji je dohvacen
     */
    public Korisnik DohvatiKorisnika(String ime, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT * FROM KORISNICI WHERE KOR_IME='" + ime + "'";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    return kreirajKorisnika(rs);
                }

                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Kreira se korisnik prema rezultatu upita
     * @param rs rezultat upita 
     * @return objekt korisnika
     */
    public Korisnik kreirajKorisnika(ResultSet rs) {
        try {
            String korisnik1 = rs.getString("kor_ime");
            String ime = rs.getString("ime");
            String prezime = rs.getString("prezime");
            String email = rs.getString("email_adresa");
            Timestamp kreiran = rs.getTimestamp("datum_kreiranja");
            Timestamp promjena = rs.getTimestamp("datum_promjene");
            Korisnik k = new Korisnik(korisnik1, ime, prezime, "******", email, kreiran, promjena);
            return k;
        } catch (SQLException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dohvaca  dohvaca imena korisnika iz proslijedenog upita
     * @param upit upit za bazu podataka
     * @param bpk konfiguracija baze podataka
     * @return lista korisnickih imena
     */
    public List<String> dohvatiImenaKorisnika(String upit, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        List<String> lista = new ArrayList<>();
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                while (rs.next()) {
                    String username = rs.getString("USERNAME");
                    lista.add(username);
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    /**
     * Metoda provjerava postojanje korisnika u bazi
     * @param username koriscnicko ime
     * @param password lozinka
     * @param bpk konfiguracija baze podataka
     * @return true postoji false ne postoji
     */
    public boolean provjeraKorisnika(String username, String password, BP_Konfiguracija bpk) {
        boolean postoji = false;
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT * FROM KORISNICI WHERE KOR_IME='" + username + "' AND LOZINKA='" + password + "'";
        postoji = ProvjeriPostojanje(upit, url, bpkorisnik, bplozinka, bpk);
        return postoji;
    }
    /**
     * Izvrsava upit o postojanju korisnika
     * @param upit kreiran upit za provjeru korisnika
     * @param url putanja do baze
     * @param bpkorisnik podaci za bazu o korisniku
     * @param bplozinka podaci za bazu lozinka
     * @param bpk konfiguracija baze podataka
     * @return true postoji false ne postoji
     */
    public boolean ProvjeriPostojanje(String upit, String url, String bpkorisnik, String bplozinka, BP_Konfiguracija bpk) {
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
                    postoji = true;
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return postoji;
    }
    
    /**
     * PRovjera lozinke kod azuriranja korisnika
     *
     * @param k objekt korisnika
     * @param username korisnicko ime
     * @return vraća upit za azuriranje korisnika
     */
    public String provjeriLozinku(Korisnik k, String username) {
        String upit = "";
        if (k.getLozinka() == null || k.getLozinka().isEmpty()) {
            upit = "UPDATE korisnici SET IME = '" + k.getIme() + "', PREZIME = '" + k.getPrezime()
                    + "', EMAIL_ADRESA = '" + k.getEmailAdresa() + "' WHERE "
                    + "KOR_IME = '" + username + "'";
        } else {
            upit = "UPDATE korisnici SET IME = '" + k.getIme() + "', PREZIME = '" + k.getPrezime()
                    + "', EMAIL_ADRESA = '" + k.getEmailAdresa() + "', LOZINKA = '" + k.getLozinka() + "' WHERE "
                    + "KOR_IME = '" + username + "'";
        }
        return upit;
    }
    
    /**
     * Metoda za azuriranje korisnika u bazi podataka
     * @param k objekt korisnika za azuriranje
     * @param username koriscniko ime
     * @param lozinka lozinka
     * @param bpk konfiguracija baze podataka
     * @return azuriran true false nije azuriran
     */
    public boolean azuriranjeKorisnikaBaza(Korisnik k, String username, String lozinka, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = provjeriLozinku(k, username);
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement()) {
                int brojAzuriranja = s.executeUpdate(upit);
                return brojAzuriranja == 1;
            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    /**
     * Metoda koja se poziva kod azuriranja korisnika
     * @param k objekt korisnika za azuriranje
     * @param username korisnicko ime
     * @param lozinka lozinka
     * @param bpk konfiguracija za bazu podataka
     * @return  true azuriran false ne azuriran
     */
    public boolean azurirajKorisnika(Korisnik k, String username, String lozinka, BP_Konfiguracija bpk) {
        if (provjeraKorisnika(username, lozinka, bpk)) {
            return azuriranjeKorisnikaBaza(k, username, lozinka, bpk);
        } else {
            return false;
        }
    }
    /**
     * Metoda provjerava atribute korisnika
     * @param k objekt korisnik za provjeru
     * @return  true za ispravne parametre false za ne isproavne
     */
    public boolean provjeraKorisnika(Korisnik k) {
        boolean tocno = true;
        if (k.getIme() == null || k.getIme().isEmpty()) {
            tocno = false;
        }
        if (k.getPrezime() == null || k.getPrezime().isEmpty()) {
            tocno = false;
        }
        if (k.getKorIme() == null || k.getKorIme().isEmpty()) {
            tocno = false;
        }
        if (k.getLozinka() == null || k.getLozinka().isEmpty()) {
            tocno = false;
        }
        return tocno;
    }
    /**
     * Metoda koja kreira upit za unos korisnika te izvrsava
     * @param k objekt korisnika
     * @param lozinka lozinka korisnika
     * @param bpk konfiguracija za bazu podataka
     * @return true dodan false nije dodan
     */
    public boolean unosKorisnika(Korisnik k, String lozinka, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "INSERT INTO korisnici (IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA, DATUM_KREIRANJA, DATUM_PROMJENE) VALUES ("
                + "'" + k.getIme() + "', '" + k.getPrezime()
                + "', '" + k.getEmailAdresa() + "', '" + k.getKorIme() + "', '" + k.getLozinka() + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try {
            Class.forName(bpk.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement()) {
                int brojAzuriranja = s.executeUpdate(upit);
                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    /**
     * Metoda koja se poziva kod za pocetak unosa korisnika
     * @param k objekt korisnika
     * @param lozinka lozinka
     * @param bpk konf baze podataka
     * @return  true unesen false nije unesen
     */
    public boolean dodajKorisnika(Korisnik k, String lozinka, BP_Konfiguracija bpk) {
        boolean uspioUnos = false;
        if (provjeraKorisnika(k)) {
            uspioUnos = unosKorisnika(k, lozinka, bpk);
        } else {
            System.out.println("Podaci nisu ispravni!");
        }
        return uspioUnos;
    }

}
