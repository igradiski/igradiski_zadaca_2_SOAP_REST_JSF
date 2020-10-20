
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

/**
 * Web application lifecycle listener.
 *
 * @author Korisnik
 */

@WebListener
public class SlusacAplikacije implements ServletContextListener {      
    /**
     * Metoda koja se pali kod pokrtanja konteksta i preuzima konfiguraciju
     * @param sce  servelet kontekst
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String datoteke = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("/WEB-INF")+File.separator+datoteke;
        try {
            BP_Konfiguracija konf = new BP_Konfiguracija(putanja);
            sc.setAttribute("BP_Konfig", konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Aplikacija je pokrenuta");
    }
    /**
     * Metoda kod unistavanja konteksta
     * @param sce 
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
