
package servlet;

import classi.Gruppo;
import classi.Utente;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import services.MetodiGruppi;
import services.MetodiPost;

public class Gruppi extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession();
            Utente ute = (Utente) session.getAttribute("utente");
            List<Integer> gruppi = MetodiGruppi.listaGruppiIscritto(ute.getId());
            
            if(!gruppi.isEmpty()){
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("    <head>");
                    out.println("        <title>Gruppi</title>");
                    out.println("        <meta charset=\"UTF-8\">");
                    out.println("        <meta name=\"viewport\" content=\"width=device-width\">");
                    out.println("        <link rel=\"stylesheet\" type=\"text/css\" href= \"Css/bootstrap.css \" media=\"screen\" />");
                    out.println("    </head>");
                    out.println("    <body>");
                    out.println("        <div class=\"container\">");
                    out.println("           <div class=\"col-md-offset-2 col-md-8\"><h1>Gruppi</h1></div>");
                    out.println("           <div class=\"col-md-offset-2 col-md-8\" style=\"margin-top:50px; margin-bottom:20px; overflow-y:scroll; max-height:400px;\"> ");
                    out.println("            <table class=\"table table-striped\" >");
                    out.println("                <thead>");
                    out.println("                    <tr>");
                    out.println("                        <th>Gruppi</th>");
                    out.println("                        <th>Data</th>");
                    out.println("                        <th>Link</th>");
                    out.println("                        <th><img src=\"Images/downloadicon.png\" style=\"width:40px; height:30px;\"></th> ");
                    out.println("                    </tr>");
                    out.println("               </thead>");
                    out.println("               <tbody>");

                    Iterator i = gruppi.iterator(); 
                            while(i.hasNext()) {
                                int ID = (int) i.next();
                                Gruppo g = MetodiGruppi.searchGruppoById(ID);
                                out.println("                    <tr>");


                                //solo se sono il proprietario risulta clicckabil eil link per andare a modifica gruppo
                                if(g.getProprietario() == ute.getId()){
                                    out.println("");
                                    out.println("<td>"+ g.getNome()+ "");
                                    out.println("<form  action=\"modificaGruppo\">");
                                    out.println("<input  type=\"submit\" name=\""+g.getID()+"\" value=\"\" style=\" float:left; background-image: url(Images/miniediticon.png); height: 30px; width: 30px; background-repeat: no-repeat; border-style: none;\"></br>");      
                                    out.println("</form></td>");
                                }else{
                                    out.println("                        <td>"+ g.getNome()+ "</td>");
                                }

                                String data = MetodiPost.dataUltimoPost(g);
                                if(data == null)
                                    out.println("                        <td> Nessun Post </td>");
                                else
                                    out.println("                        <td>"+data+"</td>");
                                
                                
                                out.println("                        <td> <a href=\"Forum?id="+g.getID()+"\">Vai al Gruppo!</a></td>");
                                if(g.getProprietario() == ute.getId()){
                                    out.println("");
                                    out.println("<td>");
                                    out.println("<form  action=\"PDF_report\" target='blank' >");
                                    out.println("<input type=\"submit\" value=\""+g.getID()+"\" name=\"nomeGruppo\" style=\" float:left; background-image: url(Images/pdficonred.png); height: 50px; width: 50px; background-repeat: no-repeat; border-style: none; font-size:0px;\"></br>");      
                                    out.println("</form></td>");

                                }
                                else
                                {
                                    out.println("");
                                    out.println("<td>");
                                    out.println("<form  action=\"PDF_report\" target='blank' >");
                                    out.println("<img style=\" float:left; background-image: url(Images/pdficon.png); height: 50px; width: 50px; background-repeat: no-repeat; border-style: none;\"></br>");      
                                    out.println("</form></td>");
                                }
                                out.println("  </tr>");
                            }


                    out.println("                </tbody>");
                    out.println("            </table>");
                    out.println("       </div>");
                    out.println("        </div>");
                    out.println("            <a href=\"Home\"><button action=\"Home\" class=\"btn btn-primary col-md-offset-3\" style='margin-bottom:30px;'>Home</button></a>");

                    out.println("    </body>");
                    out.println("</html>");
                    out.println("");
                }else{
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("    <head>");
                    out.println("        <title>Inviti</title>");
                    out.println("        <meta charset=\"UTF-8\">");
                    out.println("        <meta name=\"viewport\" content=\"width=device-width\">");
                    out.println("        <link rel=\"stylesheet\" type=\"text/css\" href= \"Css/bootstrap.css \" media=\"screen\" />");
                    out.println("    </head>");
                    out.println("    <body>");
                    out.println("    <div class=\"container\"><div class=\"col-md-6 col-md-offset-3\" style=\"margin-top:40px;margin-bottom:20px; text-align:center;\"><h1>La tua lista dei gruppi è vuota!</h1></div>"
                            + "                             <div class=\"col-md-2 col-md-offset-4\" ><a href=\"Home\"<button class=\"btn btn-primary\">Home</button></a></div></div>");
                    out.println("    </body>");
                    out.println("   </html>");
                }

        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Gruppi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Gruppi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
