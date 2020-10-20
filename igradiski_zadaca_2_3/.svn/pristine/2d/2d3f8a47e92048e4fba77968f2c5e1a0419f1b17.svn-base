/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.igradiski.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.lang.String;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Jersey REST client generated for REST resource:Zadaca2RestAerodromi
 * [aerodromi]<br>
 * USAGE:
 * <pre>
 *        Zadaca2_2RS client = new Zadaca2_2RS();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Korisnik
 */
public class Zadaca2_2RS {
    
    private  String korisnik="";
    private  String lozinka="";

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = "http://localhost:8080/igradiski_zadaca_2_2/webresources";

    public Zadaca2_2RS(String korisnik, String lozinka) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
        this.korisnik=korisnik;
        this.lozinka=lozinka;
    }
    /**
     * put metoda
     * @param requestEntity
     * @throws ClientErrorException 
     */
    public void putJson(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }
    
    /**
     * Metoda koja post re
     * @param <T>
     * @param responseType
     * @param icao
     * @return
     * @throws ClientErrorException 
     */
    public <T> T dodajMojAerodrom(Class<T> responseType, String icao) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.queryParam("icao", icao);
        return (T) resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)
                .header("lozinka", lozinka)
                .post(Entity.entity(icao, MediaType.APPLICATION_JSON));

    }
    /**
     * 
     * @param <T>
     * @param responseType
     * @return
     * @throws ClientErrorException 
     */
    public <T> T dajAerodomeKorisnika(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)
                .header("lozinka", lozinka)
                .get(responseType);
    }
        /**
         * 
         * @param <T>
         * @param responseType
         * @param icao
         * @return
         * @throws ClientErrorException 
         */
     public <T> T dajTrazeniAerodrom(Class<T> responseType, String icao) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(icao);
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)
                .header("lozinka", lozinka)
                .get(responseType);
    }
    /**
     * Request za dohvacanje aerodroma
     * @param <T>
     * @param responseType
     * @param naziv
     * @param drzava
     * @return
     * @throws ClientErrorException 
     */
    public <T> T dajAerodome(Class<T> responseType, String naziv, String drzava) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (drzava != null) {
            resource = resource.queryParam("drzava", drzava);
        }
        if (naziv != null) {
            resource = resource.queryParam("naziv", naziv);
        }
        resource = resource.path("svi");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).get(responseType);
    }
    /**
     * metoda za primanje j son 
     * @param <T> response klasa
     * @param responseType
     * @return json
     * @throws ClientErrorException 
     */
    public <T> T getJson(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * zatvara klijenta
     */
    public void close() {
        client.close();
    }
    
}
