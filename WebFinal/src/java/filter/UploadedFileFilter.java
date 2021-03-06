package filter;

import classi.Utente;
import classi.Gruppo;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import services.MetodiGruppi;

public class UploadedFileFilter implements Filter {
    
    private static final boolean debug = false;
    private FilterConfig filterConfig = null;
    
    public UploadedFileFilter() {
    }    

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        Gruppo gruppo=null;
        boolean userIsInTheGroup =false;
        Utente utente = null;
        System.err.println("filtro1");
        utente = (Utente)((HttpServletRequest)request).getSession().getAttribute("utente");
        System.err.println("filtro2");
        if (utente!=null) {

            System.err.println(utente.getUsername() + " " + utente.getId());

            String myURL = ((HttpServletRequest)request).getRequestURI();

            int secondToLastSlashIndex = 23;
            int lastSlashIndex = myURL.lastIndexOf("/");

            String groupIDstring= myURL.substring(secondToLastSlashIndex, lastSlashIndex);
            System.err.println("groupIDstring= " + groupIDstring);

            int groupIDint = Integer.parseInt(groupIDstring);
            System.err.println("groupIDint= " + groupIDint);

            

            try {
                gruppo = (Gruppo)MetodiGruppi.searchGruppoById(groupIDint);
            } catch (SQLException ex) {
                Logger.getLogger(UploadedFileFilter.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (gruppo!=null) {
                try {
                    userIsInTheGroup = MetodiGruppi.uteIntoTheGroup(utente, gruppo);
                } catch (SQLException ex) {
                    Logger.getLogger(UploadedFileFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        if (userIsInTheGroup == false){  
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<head>");
            out.println("<meta http-equiv=\"refresh\" content=\"3; /WebFinal/Home \">");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href= \"Css/bootstrap.css \" media=\"screen\" />");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"container\">\n" +
"                           <div class=\"col-md-6 col-md-offset-3\" style=\"margin-top:40px;margin-bottom:10px; text-align:center;\">\n" +
"                               <h1>Impossibile Accedere. </h1>\n" +
"                           </div>\n" +
"                           <div class=\"col-md-6 col-md-offset-3\" style=\"margin-bottom:30px; text-align:center;\">\n" +
"                               <h1><small>Non puoi visualizzare questo file.</small></h1>\n" +
"                           </div>"
                    + "  </div>");
            out.println("</body>");
            out.close();
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {  
        this.filterConfig = null;
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
    }
}
