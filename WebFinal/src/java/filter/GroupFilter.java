/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filter;

import classi.Utente;
import classi.Gruppo;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import services.MetodiGruppi;

/**
 *
 * @author FMalesani
 */

public class GroupFilter implements Filter {
    
    private static final boolean debug = false;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public GroupFilter() {
    }    

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        Utente utente = (Utente)((HttpServletRequest)request).getSession().getAttribute("utente");
        String groupID = ((HttpServletRequest)request).getParameter("id");
        
        Gruppo gruppo = null;
        try {
            gruppo=MetodiGruppi.searchGruppoById(Integer.parseInt(groupID));
        } catch (SQLException ex) {
            Logger.getLogger(GroupFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean userIsInTheGroup=false;
        try {
            userIsInTheGroup = MetodiGruppi.uteIntoTheGroup(utente, gruppo);
        } catch (SQLException ex) {
            Logger.getLogger(GroupFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (userIsInTheGroup == false){
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<head>");
            out.println("<meta http-equiv=\"refresh\" content=\"3; Gruppi\">");
            out.println("<div align=’center’><H3>IMPOSSIBILE ACCEDERE: NON FAI PARTE DI QUESTO GRUPPO<br><br>");
            out.println("Redirezione alla pagina di principale in corso</H3></div>");
            out.close();
            
        } else 
            chain.doFilter(request, response);
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
