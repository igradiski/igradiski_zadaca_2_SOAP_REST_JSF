
package org.foi.nwtis.igradiski.web.slusaci;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.igradiski.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.igradiski.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.igradiski.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.igradiski.web.dretve.PreuzimanjeLetovaAvionaAerodroma;

/**
 * Web application lifecycle listener.
 *
 * @author Korisnik
 */

@WebListener
public class SlusacAplikacije implements ServletContextListener {

    
    
    PreuzimanjeLetovaAvionaAerodroma plaa = null;
            
    /**
     * Metoda koja pokrece dretvu za preuzimanje aerodroma letova i ostalo
     * @param sce  servelt kontekst
     */      
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String datoteke = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("/WEB-INF") + File.separator + datoteke;
        try {
            BP_Konfiguracija konf = new BP_Konfiguracija(putanja);
            sc.setAttribute("BP_Konfig", konf);
            plaa = new PreuzimanjeLetovaAvionaAerodroma(konf);
            plaa.start();

        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Aplikacija je pokrenuta");
    }

    /**
     * Kada je kontekst unisten gasi dretvu da ne ostane daemon
     * @param sce 
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (plaa != null) {
            plaa.interrupt();
        }
        System.out.println("Aplikacija je zaustavljena");
    }
}
